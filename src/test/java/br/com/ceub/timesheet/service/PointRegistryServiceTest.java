package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.Utils.ActivityType;
import br.com.ceub.timesheet.domain.dtos.PointRegistryCreateRequest;
import br.com.ceub.timesheet.domain.entities.Classes;
import br.com.ceub.timesheet.domain.entities.PointRegistry;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.repository.PointRegistryRepository;
import br.com.ceub.timesheet.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class PointRegistryServiceTest {

    @InjectMocks
    private PointRegistryService pointRegistryService;

    @Mock
    private PointRegistryRepository pointRegistryRepository;

    @Mock
    private UserRepository userRepository;



    @Test
    public void shouldReturnErrorUsuarioNaoTemAulasCadastradas() {
        User user = new User();
        user.setClasses(new ArrayList<>());

        PointRegistryCreateRequest pointRegistryCreateRequest = new PointRegistryCreateRequest();
        pointRegistryCreateRequest.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Exception thrown = assertThrows(Exception.class, () -> pointRegistryService.createPointRegistry(pointRegistryCreateRequest));

        String expectedException = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não tem aulas registradas!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }

    @Test
    public void shouldReturnEntrada() {
        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        List<PointRegistry> pointRegistries = new ArrayList<>();

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 5, 0);

        assertEquals(ActivityType.ENTRADA, pointRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnSaida() {
        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        PointRegistry pointRegistry = new PointRegistry();

        pointRegistry.setUserId(1L);
        pointRegistry.setActivityType(ActivityType.ENTRADA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 19, 5));
        pointRegistry.setActivityId(51);

        List<PointRegistry> pointRegistries = new ArrayList<>();
        pointRegistries.add(pointRegistry);

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 20, 50, 0);

        assertEquals(ActivityType.SAIDA, pointRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnAtraso() {
        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        List<PointRegistry> pointRegistries = new ArrayList<>();

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 26, 0);

        assertEquals(ActivityType.ATRASO, pointRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnEntradaAfterSaida() {
        PointRegistry pointRegistry = new PointRegistry();

        pointRegistry.setUserId(1L);
        pointRegistry.setActivityType(ActivityType.SAIDA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 20, 50));
        pointRegistry.setActivityId(52);

        List<PointRegistry> pointRegistries = new ArrayList<>();
        pointRegistries.add(pointRegistry);

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 21, 5);

        assertEquals(ActivityType.ENTRADA, pointRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnSaidaAfterAtraso() {
        PointRegistry pointRegistry = new PointRegistry();

        pointRegistry.setUserId(1L);
        pointRegistry.setActivityType(ActivityType.ATRASO);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 19, 50));
        pointRegistry.setActivityId(52);

        List<PointRegistry> pointRegistries = new ArrayList<>();
        pointRegistries.add(pointRegistry);

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 20, 55);

        assertEquals(ActivityType.SAIDA, pointRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnSaida15MinutesAfterEntrada() {
        PointRegistry pointRegistry = new PointRegistry();

        pointRegistry.setUserId(1L);
        pointRegistry.setActivityType(ActivityType.SAIDA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 20, 50));
        pointRegistry.setActivityId(52);

        List<PointRegistry> pointRegistries = new ArrayList<>();
        pointRegistries.add(pointRegistry);

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 21, 5);

        assertEquals(ActivityType.ENTRADA, pointRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnErrorEntradaJaRegistrada() {
        PointRegistry pointRegistry = new PointRegistry();
        pointRegistry.setUserId(1L);
        pointRegistry.setActivityType(ActivityType.ENTRADA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 21, 0));
        pointRegistry.setActivityId(51);

        List<PointRegistry> pointRegistries = new ArrayList<>();

        pointRegistries.add(pointRegistry);

        Classes classes = new Classes();
        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        when(pointRegistryRepository.findByUserId(1L)).thenReturn(pointRegistries);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 21, 5);

        Exception thrown = assertThrows(Exception.class, () -> {
            pointRegistryService.activityType(classes, now);
        });

        String expectedException = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrada ou Atraso já registrado!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }

    @Test
    public void shouldReturnErrorNaoExistemAulasNesseHorario1() {
        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 10);

        Classes classes = new Classes();

        classes.setId(51);
        classes.setUserId(1L);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        List<Classes> userClasses = new ArrayList<>();

        userClasses.add(classes);

        PointRegistry pointRegistry = new PointRegistry();

        Exception thrown = assertThrows(Exception.class, () -> {
            pointRegistryService.setActivityEntryType(now, userClasses, pointRegistry);
        });

        String expectedException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existem aulas cadastradas para o horario atual!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }

    @Test
    public void shouldReturnErrorNaoExistemAulasNesseHorario2() {
        LocalDateTime now = LocalDateTime.of(2023, 11, 23, 15, 39);

        Classes classe1 = new Classes();
        classe1.setId(1);
        classe1.setUserId(1L);
        classe1.setDiscipline("Gerencia de Projetos de Ti");
        classe1.setClassDay("Quinta-feira");
        classe1.setSchedule("08:00-09:40");

        Classes classe2 = new Classes();
        classe2.setId(1);
        classe2.setUserId(1L);
        classe2.setDiscipline("Gerencia de Projetos de Ti");
        classe2.setClassDay("Quinta-feira");
        classe2.setSchedule("10:00-10:50");

        Classes classe3 = new Classes();
        classe3.setId(1);
        classe3.setUserId(1L);
        classe3.setDiscipline("Sistemas de Comunicação");
        classe3.setClassDay("Quinta-feira");
        classe3.setSchedule("21:00-22:40");

        List<Classes> userClasses = new ArrayList<>();

        userClasses.add(classe1);
        userClasses.add(classe2);
        userClasses.add(classe3);

        PointRegistry pointRegistry = new PointRegistry();

        Exception thrown = assertThrows(Exception.class, () -> {
            pointRegistryService.setActivityEntryType(now, userClasses, pointRegistry);
        });

        String expectedException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existem aulas cadastradas para o horario atual!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }


}
