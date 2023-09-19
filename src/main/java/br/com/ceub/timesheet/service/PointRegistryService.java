package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.Utils.ActivityType;
import br.com.ceub.timesheet.Utils.ClassesAux;
import br.com.ceub.timesheet.domain.dtos.PointRegistryCreateRequest;
import br.com.ceub.timesheet.domain.entities.Classes;
import br.com.ceub.timesheet.domain.entities.PointRegistry;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.repository.PointRegistryRepository;
import br.com.ceub.timesheet.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PositionService positionService;

    public PointRegistryService(PointRegistryRepository pointRegistryRepository, UserRepository userRepository, PositionService positionService) {
        this.pointRegistryRepository = pointRegistryRepository;
        this.userRepository = userRepository;
        this.positionService = positionService;
    }

    public ResponseEntity<List<PointRegistry>> userPointRegistries(Long id) {
        List<PointRegistry> pointRegistry = pointRegistryRepository.findByUserId(id);

        return ResponseEntity.ok(pointRegistry);
    }

    public ResponseEntity<PointRegistry> createPointRegistry(PointRegistryCreateRequest userRegistry) {
        User user = userRepository.findById(userRegistry.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado ou inexistente!"));

        if (!positionService.checkPosition(userRegistry.getPosition())) {
            // Lançar uma exceção aqui caso a localização não esteja dentro dos limites aceitaveis
        }

        PointRegistry pointRegistry = new PointRegistry();
        LocalDateTime now = LocalDateTime.now();

        pointRegistry.setUserId(user.getId());
        pointRegistry.setDateTimeRegistry(now);

        pointRegistry.setActivityType(activityEntryType(now, user.getClasses()));

        PointRegistry savedRegistry = pointRegistryRepository.save(pointRegistry);

        return ResponseEntity.ok(savedRegistry);
    }

    public ActivityType activityEntryType(LocalDateTime localDateTime, List<Classes> userClasses) {
        String today = String.valueOf(localDateTime.getDayOfWeek());

        List<Classes> todayClasses = new ArrayList<>();

        for (Classes userClass : userClasses) {
            if (userClass.getDayFirstClass().toUpperCase().equals(dayOfTheWeek(today)) ||
                    userClass.getDaySecondClass().toUpperCase().equals(dayOfTheWeek(today))) {
                todayClasses.add(userClass);
            }
        }
        return entryType(todayClasses, localDateTime, today);
    }


    private ActivityType entryType(List<Classes> userClass, LocalDateTime localDateTime, String today) {
        List<ClassesAux> aux = new ArrayList<>();

        for (int i = 0; i < userClass.size(); i++) {
            ClassesAux classesAux = new ClassesAux();

            if (userClass.get(i).getDayFirstClass().toUpperCase().equals(dayOfTheWeek(today))) {
                classesAux.setDay(today);
                classesAux.setDiscipline(userClass.get(i).getDiscipline());
                classesAux.setClassHour(userClass.get(i).getHourFirstClass());

                aux.add(classesAux);
            }
            if (userClass.get(i).getDaySecondClass().toUpperCase().equals(dayOfTheWeek(today))) {
                classesAux.setDay(today);
                classesAux.setDiscipline(userClass.get(i).getDiscipline());
                classesAux.setClassHour(userClass.get(i).getHourSecondClass());

                aux.add(classesAux);
            }
        }
        ClassesAux actualClass = actualClass(aux, localDateTime);

        return closerClassHour(actualClass, localDateTime);
    }

    public static ClassesAux actualClass(List<ClassesAux> classesAuxes, LocalDateTime localDateTime) {
        ClassesAux objetoMaisProximo = null;
        Duration menorDiferenca = Duration.ofDays(1);

        for (ClassesAux objeto : classesAuxes) {
            String[] horarios = objeto.getClassHour().split("-");

            LocalTime inicioAula = LocalTime.parse(horarios[0]);
            LocalTime fimAula = LocalTime.parse(horarios[1]);

            Duration diferencaInicio = Duration.between(inicioAula, localDateTime).abs();
            Duration diferencaFim = Duration.between(fimAula, localDateTime).abs();

            if (diferencaInicio.compareTo(menorDiferenca) < 0 || diferencaFim.compareTo(menorDiferenca) < 0) {
                menorDiferenca = diferencaInicio.compareTo(diferencaFim) < 0 ? diferencaInicio : diferencaFim;
                objetoMaisProximo = objeto;
            }
        }
        return objetoMaisProximo;
    }

    public static ActivityType closerClassHour(ClassesAux classesAuxes, LocalDateTime localDateTime) {
        String[] schedule = classesAuxes.getClassHour().split("-");
        int entradaSaida;

        LocalTime begin = LocalTime.parse(schedule[0]);
        LocalTime end = LocalTime.parse(schedule[1]);

        long diferencaBegin = ChronoUnit.MINUTES.between(begin, localDateTime);
        long diferencaEnd = ChronoUnit.MINUTES.between(end, localDateTime);

        if (diferencaBegin < diferencaEnd) {
            entradaSaida = 0;
        } else {
            entradaSaida = 1;
        }

        if (entradaSaida == 0) {
            if(diferencaBegin > 15) {
                return ActivityType.ATRASO;
            }
            return ActivityType.ENTRADA;
        }
        return ActivityType.SAIDA;
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
