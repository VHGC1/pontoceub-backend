package br.com.pontoceub.service;

import br.com.pontoceub.domain.dto.TimeRegistryDTO;
import br.com.pontoceub.domain.dto.UserDTO;
import br.com.pontoceub.domain.entities.Classes;
import br.com.pontoceub.domain.entities.TimeRegistry;
import br.com.pontoceub.repository.TimeRegistryRepository;
import br.com.pontoceub.utils.ActivityType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeRegistryService extends AbstractDTOService<TimeRegistryDTO, TimeRegistry, Long, TimeRegistryRepository> {
    private final UserService userService;

    private final ClassesService classesService;

    public TimeRegistryService(UserService userService, ClassesService classesService, TimeRegistryRepository timeRegistryRepository) {
        super(timeRegistryRepository);
        this.userService = userService;
        this.classesService = classesService;
    }

    public TimeRegistryDTO createPointRegistry() {
        UserDTO user = userService.findById(userService.getUserIdFromRequest());

//        if (!positionService.checkPosition(userRegistry.getPosition())) {
//            // Lançar uma exceção caso a localização não esteja dentro dos limites aceitaveis
//        }

        if (user.getClasses().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não tem aulas registradas!");
        }

        TimeRegistry timeRegistry = new TimeRegistry();
        LocalDateTime now = LocalDateTime.now();

        timeRegistry.setUser(userService.dtoToEntity(user));
        timeRegistry.setDateTimeRegistry(now);

        var classes = user.getClasses().stream().map(classesService::dtoToEntity).toList();

        setActivityEntryType(now, classes, timeRegistry);

        TimeRegistry savedRegistry = repo.save(timeRegistry);

        return entityToDTO(savedRegistry);
    }

    public void setActivityEntryType(LocalDateTime localDateTime, List<Classes> userClasses, TimeRegistry timeRegistry) {
        String today = String.valueOf(localDateTime.getDayOfWeek());

        List<Classes> todayClasses = new ArrayList<>();

        for (Classes userClass : userClasses) {
            if (userClass.getClassDay().toUpperCase().equals(dayOfTheWeek(today))) {
                todayClasses.add(userClass);
            }
        }

        Classes currentClass = currentClass(todayClasses, localDateTime);

        if (currentClass == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não existem aulas cadastradas para o horario atual!");
        }

        Classes classes = classesService.dtoToEntity(currentClass);

        timeRegistry.setClassName(classes.getDiscipline());
        timeRegistry.setClasses(classes);
        timeRegistry.setRegistryType(activityType(classes, localDateTime));
    }

    public static Classes currentClass(List<Classes> classesAuxes, LocalDateTime localDateTime) {
        Classes objetoMaisProximo = null;
        Duration menorDiferenca = Duration.ofDays(1);

        for (Classes objeto : classesAuxes) {
            String[] horarios = objeto.getSchedule().split("-");

            LocalTime inicioAula = LocalTime.parse(horarios[0]);
            LocalTime fimAula = LocalTime.parse(horarios[1]);

            Duration diferencaInicio = Duration.between(inicioAula, localDateTime).abs();
            Duration diferencaFim = Duration.between(fimAula, localDateTime).abs();

            if (diferencaInicio.compareTo(menorDiferenca) < 0 || diferencaFim.compareTo(menorDiferenca) < 0) {
                menorDiferenca = diferencaInicio.compareTo(diferencaFim) < 0 ? diferencaInicio : diferencaFim;
                objetoMaisProximo = objeto;
            }
        }

        if (objetoMaisProximo != null) {
            String[] horarios = objetoMaisProximo.getSchedule().split("-");

            LocalTime inicioAula = LocalTime.parse(horarios[0]);
            LocalTime fimAula = LocalTime.parse(horarios[1]);

            long diferencaBegin = ChronoUnit.MINUTES.between(inicioAula, localDateTime);
            long diferencaEnd = ChronoUnit.MINUTES.between(fimAula, localDateTime);

            if (diferencaBegin >= -15 && diferencaEnd < 0) {
                return objetoMaisProximo;
            }
        }

        return null;
    }

    public ActivityType activityType(Classes classes, LocalDateTime localDateTime) {
        String[] schedule = classes.getSchedule().split("-");

        LocalTime begin = LocalTime.parse(schedule[0]);

        long diferencaBegin = ChronoUnit.MINUTES.between(begin, localDateTime);

        List<TimeRegistry> pointRegistries = repo.findByUserId(classes.getUser().getId());

        if (pointRegistries.isEmpty()) {
            if (diferencaBegin > 15) {
                return ActivityType.ATRASO;
            }

            return ActivityType.ENTRADA;
        }

        TimeRegistry lastPointRegistry = pointRegistries.get(pointRegistries.size() - 1);

        LocalDateTime lastRegistryTime = lastPointRegistry.getDateTimeRegistry();

        long diferencaLastRegistry = ChronoUnit.MINUTES.between(lastRegistryTime, localDateTime);

        if (diferencaLastRegistry >= 15) {
            if (lastPointRegistry.getClasses().getId().equals(classes.getId()) &&
                    lastPointRegistry.getRegistryType().equals(ActivityType.ENTRADA) ||
                    lastPointRegistry.getRegistryType().equals(ActivityType.ATRASO)) {
                return ActivityType.SAIDA;
            }
        }

        if (diferencaBegin > 15) {
            return ActivityType.ATRASO;
        }

        if (lastPointRegistry.getClasses().getId().equals(classes.getId()) &&
                lastPointRegistry.getRegistryType().equals(ActivityType.ENTRADA) ||
                lastPointRegistry.getRegistryType().equals(ActivityType.ATRASO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrada ou Atraso já registrado!");
        }
        return ActivityType.ENTRADA;
    }
    public String dayOfTheWeek(String engDayOfWeek) {
        Map<String, String> day = new HashMap<>();
        day.put("MONDAY", "SEGUNDA-FEIRA");
        day.put("TUESDAY", "TERÇA-FEIRA");
        day.put("WEDNESDAY", "QUARTA-FEIRA");
        day.put("THURSDAY", "QUINTA-FEIRA");
        day.put("FRIDAY", "SEXTA-FEIRA");
        day.put("SATURDAY", "SABADO");
        day.put("SUNDAY", "DOMINGO");

        return day.get(engDayOfWeek);
    }
}
