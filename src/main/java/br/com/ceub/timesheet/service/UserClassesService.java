package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.domain.dtos.ClassCreateRequest;
import br.com.ceub.timesheet.domain.dtos.ClassesByDayResponse;
import br.com.ceub.timesheet.domain.dtos.UserClassesByDayShort;
import br.com.ceub.timesheet.domain.dtos.UserClassesResponse;
import br.com.ceub.timesheet.domain.entities.Classes;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.repository.ClassesRepository;
import br.com.ceub.timesheet.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserClassesService {

    private final UserRepository userRepository;

    private final ClassesRepository classesRepository;

    public UserClassesService(UserRepository userRepository, ClassesRepository classesRepository) {
        this.userRepository = userRepository;
        this.classesRepository = classesRepository;
    }

    public UserClassesResponse getUserClasses(Long id) {
        User result = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado ou inexistente!"));

        UserClassesResponse user = new UserClassesResponse();

        user.setId(result.getId());
        user.setName(result.getName());
        user.setEmail(result.getEmail());
        user.setClasses(result.getClasses());

        return user;
    }

    public ClassesByDayResponse getClassesByDay(Long id, String day) {
        User result = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado ou inexistente!"));

        List<Classes> classes = result.getClasses();

        ClassesByDayResponse classesByDayResponse = new ClassesByDayResponse();
        classesByDayResponse.setDay(day);

        List<UserClassesByDayShort> userClassesByDayShortList = new ArrayList<>();

        for(int i = 0; i < classes.size(); i++) {
            if(classes.get(i).getClassDay().equalsIgnoreCase(day)) {
                UserClassesByDayShort userClassesByDayShort = new UserClassesByDayShort();

                userClassesByDayShort.setDiscipline(classes.get(i).getDiscipline());
                userClassesByDayShort.setCampus(classes.get(i).getCampus());
                userClassesByDayShort.setSchedule(classes.get(i).getSchedule());

                userClassesByDayShortList.add(userClassesByDayShort);
            }
        }

        classesByDayResponse.setClasses(userClassesByDayShortList);

        return classesByDayResponse;
    }

    public UserClassesResponse createUserClasses(Long id, List<ClassCreateRequest> classes) {
        User result = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado ou inexistente!"));

        for (int i = 0; i < classes.size(); i++) {
            Classes newClasses = new Classes();

            newClasses.setUserId(id);
            newClasses.setDiscipline(classes.get(i).getDiscipline());
            newClasses.setCampus(classes.get(i).getCampus());
            newClasses.setClassDay(classes.get(i).getClassDay());
            newClasses.setSchedule(classes.get(i).getSchedule());

            result.getClasses().add(newClasses);
            classesRepository.save(newClasses);
        }

        var savedUserClasses = userRepository.save(result);

        return new UserClassesResponse(
                savedUserClasses.getId(),
                savedUserClasses.getName(),
                savedUserClasses.getEmail(),
                savedUserClasses.getClasses()
        );
    }
}
