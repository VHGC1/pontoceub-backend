package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.Utils.ActivityType;
import br.com.ceub.timesheet.domain.dtos.PointRegistryCreateRequest;
import br.com.ceub.timesheet.domain.entities.Classes;
import br.com.ceub.timesheet.domain.entities.PointRegistry;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.repository.PointRegistryRepository;
import br.com.ceub.timesheet.repository.PointRegistryRepositoryPaginated;
import br.com.ceub.timesheet.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class PointRegistryService {
    private final PointRegistryRepository pointRegistryRepository;

    private final PointRegistryRepositoryPaginated pointRegistryRepositoryPaginated;
    private final UserRepository userRepository;
    private final PositionService positionService;

    public PointRegistryService(
            PointRegistryRepository pointRegistryRepository,
            PointRegistryRepositoryPaginated pointRegistryRepositoryPaginated,
            UserRepository userRepository,
            PositionService positionService
    ) {
        this.pointRegistryRepository = pointRegistryRepository;
        this.pointRegistryRepositoryPaginated = pointRegistryRepositoryPaginated;
        this.userRepository = userRepository;
        this.positionService = positionService;
    }

    public ResponseEntity<List<PointRegistry>> userPointRegistries(Long id) {
        List<PointRegistry> pointRegistry = pointRegistryRepository.findByUserId(id);

        return ResponseEntity.ok(pointRegistry);
    }

    public ResponseEntity<Page<PointRegistry>> userPointRegistriesPaginated(Long id, int pageSize, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(
                Sort.Order.desc("id")));

        Page<PointRegistry> pointRegistry = pointRegistryRepositoryPaginated.findByUserId(id, pageable);

        return ResponseEntity.ok(pointRegistry);
    }

    public ResponseEntity<PointRegistry> createPointRegistry(PointRegistryCreateRequest userRegistry) {
        User user = userRepository.findById(userRegistry.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado ou inexistente!"));

//        if (!positionService.checkPosition(userRegistry.getPosition())) {
//            // Lançar uma exceção caso a localização não esteja dentro dos limites aceitaveis
//        }

        if (!(user.getClasses().size() > 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não tem aulas registradas!");
        }

        PointRegistry pointRegistry = new PointRegistry();
        LocalDateTime now = LocalDateTime.now();

        pointRegistry.setUserId(user.getId());
        pointRegistry.setDateTimeRegistry(now);

        setActivityEntryType(now, user.getClasses(), pointRegistry);

        PointRegistry savedRegistry = pointRegistryRepository.save(pointRegistry);

        return ResponseEntity.ok(savedRegistry);
    }

    public void setActivityEntryType(LocalDateTime localDateTime, List<Classes> userClasses, PointRegistry pointRegistry) {
        String today = String.valueOf(localDateTime.getDayOfWeek());

        List<Classes> todayClasses = new ArrayList<>();

        for (Classes userClass : userClasses) {
            if (userClass.getClassDay().toUpperCase().equals(dayOfTheWeek(today))) {
                todayClasses.add(userClass);
            }
        }

        Classes classes = actualClass(todayClasses, localDateTime);

        if (classes == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existem aulas cadastradas para o horario atual!");
        }

        pointRegistry.setActivity(classes.getDiscipline());
        pointRegistry.setActivityId(classes.getId());
        pointRegistry.setActivityType(activityType(classes, localDateTime));
    }

    public static Classes actualClass(List<Classes> classesAuxes, LocalDateTime localDateTime) {
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

        List<PointRegistry> pointRegistries = pointRegistryRepository.findByUserId(classes.getUserId());

        if (pointRegistries.size() == 0) {
            if (diferencaBegin > 15) {
                return ActivityType.ATRASO;
            }

            return ActivityType.ENTRADA;
        }

        PointRegistry lastPointRegistry = pointRegistries.get(pointRegistries.size() - 1);

        LocalDateTime lastRegistryTime = lastPointRegistry.getDateTimeRegistry();

        long diferencaLastRegistry = ChronoUnit.MINUTES.between(lastRegistryTime, localDateTime);

        if (diferencaLastRegistry >= 15) {
            if (lastPointRegistry.getActivityId().equals(classes.getId()) &&
                    lastPointRegistry.getActivityType().equals(ActivityType.ENTRADA) ||
                    lastPointRegistry.getActivityType().equals(ActivityType.ATRASO)) {
                return ActivityType.SAIDA;
            }
        }

        if (diferencaBegin > 15) {
            return ActivityType.ATRASO;
        }

        if (lastPointRegistry.getActivityId().equals(classes.getId()) &&
                lastPointRegistry.getActivityType().equals(ActivityType.ENTRADA) ||
                lastPointRegistry.getActivityType().equals(ActivityType.ATRASO)) {
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
