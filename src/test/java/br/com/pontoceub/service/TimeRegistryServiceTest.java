package br.com.pontoceub.service;

import br.com.pontoceub.domain.dto.UserDTO;
import br.com.pontoceub.domain.entities.Classes;
import br.com.pontoceub.domain.entities.TimeRegistry;
import br.com.pontoceub.domain.entities.User;
import br.com.pontoceub.repository.TimeRegistryRepository;
import br.com.pontoceub.repository.UserRepository;
import br.com.pontoceub.utils.ActivityType;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeRegistryServiceTest {
    @InjectMocks
    private TimeRegistryService timeRegistryService;

    @Mock
    private TimeRegistryRepository timeRegistryRepository;

    @Mock UserService userService;

    @BeforeEach
    public void setContext() {

    }

    public void createEntrada() {
        Classes classes = new Classes();

        User user = new User();
        user.setId(1L);

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        List<TimeRegistry> timeRegistry = new ArrayList<>();

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 5, 0);

        timeRegistryService.activityType(classes, now);
    }


    @Test
    public void shouldReturnErrorUsuarioNaoTemAulasCadastradas() {
        // Arrange
        UserDTO user = new UserDTO();
        user.setClasses(new ArrayList<>());

        // Mock the userService to return the user DTO with no classes
        when(userService.findById(userService.getUserIdFromRequest())).thenReturn(user);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> timeRegistryService.createPointRegistry());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Usuario não tem aulas registradas!", exception.getReason());
    }

    @Test
    public void shouldReturnEntrada() {
        Classes classes = new Classes();

        User user = new User();
        user.setId(1L);

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        List<TimeRegistry> timeRegistry = new ArrayList<>();

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 5, 0);

        assertEquals(ActivityType.ENTRADA, timeRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnSaida() {
        createEntrada();

        Classes classes = new Classes();

        User user = new User();
        user.setId(1L);

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        TimeRegistry timeRegistry = new TimeRegistry();

        timeRegistry.setUser(user);
        timeRegistry.setRegistryType(ActivityType.ENTRADA);
        timeRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 19, 5));
        timeRegistry.setClassName(classes.getDiscipline());

        List<TimeRegistry> timeRegistries = new ArrayList<>();
        timeRegistries.add(timeRegistry);

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistries);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 20, 50, 0);

        assertEquals(ActivityType.SAIDA, timeRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnAtraso() {
        Classes classes = new Classes();

        User user = new User();
        user.setId(1L);

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("19:10-20:50");

        List<TimeRegistry> timeRegistry = new ArrayList<>();

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 26, 0);

        assertEquals(ActivityType.ATRASO, timeRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnEntradaAfterSaida() {
        TimeRegistry pointRegistry = new TimeRegistry();

        User user = new User();
        user.setId(1L);

        pointRegistry.setUser(user);
        pointRegistry.setRegistryType(ActivityType.SAIDA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 20, 50));
        pointRegistry.setClassName("");

        List<TimeRegistry> timeRegistry = new ArrayList<>();
        timeRegistry.add(pointRegistry);

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        Classes classes = new Classes();

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 21, 5);

        assertEquals(ActivityType.ENTRADA, timeRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnSaidaAfterAtraso() {
        TimeRegistry pointRegistry = new TimeRegistry();

        User user = new User();
        user.setId(1L);

        pointRegistry.setUser(user);
        pointRegistry.setRegistryType(ActivityType.ATRASO);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 19, 50));
        pointRegistry.setClassName("");

        List<TimeRegistry> timeRegistry = new ArrayList<>();
        timeRegistry.add(pointRegistry);

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        Classes classes = new Classes();

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 20, 55);

        assertEquals(ActivityType.SAIDA, timeRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnSaida15MinutesAfterEntrada() {
        TimeRegistry pointRegistry = new TimeRegistry();

        User user = new User();
        user.setId(1L);

        pointRegistry.setUser(user);
        pointRegistry.setRegistryType(ActivityType.SAIDA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 20, 50));
        pointRegistry.setClassName("");

        List<TimeRegistry> timeRegistry = new ArrayList<>();
        timeRegistry.add(pointRegistry);

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        Classes classes = new Classes();

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 21, 5);

        assertEquals(ActivityType.ENTRADA, timeRegistryService.activityType(classes, now));
    }

    @Test
    public void shouldReturnErrorEntradaJaRegistrada() {
        User user = new User();
        user.setId(1L);

        TimeRegistry pointRegistry = new TimeRegistry();
        pointRegistry.setUser(user);
        pointRegistry.setRegistryType(ActivityType.ENTRADA);
        pointRegistry.setDateTimeRegistry(LocalDateTime.of(2023, 11, 20, 21, 0));
        pointRegistry.setClassName("");

        List<TimeRegistry> timeRegistry = new ArrayList<>();

        timeRegistry.add(pointRegistry);

        Classes classes = new Classes();
        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        when(timeRegistryRepository.findByUserId(1L)).thenReturn(timeRegistry);

        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 21, 5);

        Exception thrown = assertThrows(Exception.class, () -> {
            timeRegistryService.activityType(classes, now);
        });

        String expectedException = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrada ou Atraso já registrado!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }

    @Test
    public void shouldReturnErrorNaoExistemAulasNesseHorario1() {
        LocalDateTime now = LocalDateTime.of(2023, 11, 20, 19, 10);

        User user = new User();
        user.setId(1L);

        Classes classes = new Classes();

        classes.setId(51L);
        classes.setUser(user);
        classes.setDiscipline("Sistemas de Comunicação");
        classes.setClassDay("Segunda-feira");
        classes.setSchedule("21:00-22:00");

        List<Classes> userClasses = new ArrayList<>();

        userClasses.add(classes);

        TimeRegistry timeRegistry = new TimeRegistry();

        Exception thrown = assertThrows(Exception.class, () -> {
            timeRegistryService.setActivityEntryType(now, userClasses, timeRegistry);
        });

        String expectedException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existem aulas cadastradas para o horario atual!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }

    @Test
    public void shouldReturnErrorNaoExistemAulasNesseHorario2() {
        LocalDateTime now = LocalDateTime.of(2023, 11, 23, 15, 39);

        User user = new User();
        user.setId(1L);

        Classes classe1 = new Classes();
        classe1.setId(1L);
        classe1.setUser(user);
        classe1.setDiscipline("Gerencia de Projetos de Ti");
        classe1.setClassDay("Quinta-feira");
        classe1.setSchedule("08:00-09:40");

        Classes classe2 = new Classes();
        classe2.setId(1L);
        classe2.setUser(user);
        classe2.setDiscipline("Gerencia de Projetos de Ti");
        classe2.setClassDay("Quinta-feira");
        classe2.setSchedule("10:00-10:50");

        Classes classe3 = new Classes();
        classe3.setId(1L);
        classe3.setUser(user);
        classe3.setDiscipline("Sistemas de Comunicação");
        classe3.setClassDay("Quinta-feira");
        classe3.setSchedule("21:00-22:40");

        List<Classes> userClasses = new ArrayList<>();

        userClasses.add(classe1);
        userClasses.add(classe2);
        userClasses.add(classe3);

        TimeRegistry pointRegistry = new TimeRegistry();

        Exception thrown = assertThrows(Exception.class, () -> {
            timeRegistryService.setActivityEntryType(now, userClasses, pointRegistry);
        });

        String expectedException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existem aulas cadastradas para o horario atual!").getMessage();

        assertEquals(expectedException, thrown.getMessage());
    }
}
