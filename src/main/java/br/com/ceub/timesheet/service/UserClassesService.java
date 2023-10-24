package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.Utils.WeekDay;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserClassesService {

    private final UserRepository userRepository;

    private final ClassesRepository classesRepository;

    public UserClassesService(UserRepository userRepository, ClassesRepository classesRepository) {
        this.userRepository = userRepository;
        this.classesRepository = classesRepository;
    }

    public List<ClassesByDayResponse> getUserClasses(Long id) {
        User result = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado ou inexistente!"));

        List<Classes> classes = result.getClasses();

        List<ClassesByDayResponse> classesByDayResponse = new ArrayList<>();

        List<String> uniqueClassDays = classes.stream()
                .map(Classes::getClassDay)
                .distinct()
                .map(WeekDay::fromValue)
                .sorted()
                .map(WeekDay::toString)
                .map(i -> i.toLowerCase() + "-feira").toList();
        ;

        for (int i = 0; i < uniqueClassDays.size(); i++) {
            ClassesByDayResponse classesByDay = new ClassesByDayResponse();
            classesByDay.setDay(uniqueClassDays.get(i));

            List<UserClassesByDayShort> userClassesByDayShortList = new ArrayList<>();

            for (int j = 0; j < classes.size(); j++) {
                if (classes.get(j).getClassDay().equalsIgnoreCase(uniqueClassDays.get(i))) {
                    UserClassesByDayShort userClassesByDayShort = new UserClassesByDayShort();

                    userClassesByDayShort.setDiscipline(classes.get(j).getDiscipline());
                    userClassesByDayShort.setCampus(classes.get(j).getCampus());
                    userClassesByDayShort.setSchedule(classes.get(j).getSchedule());

                    userClassesByDayShortList.add(userClassesByDayShort);
                }
            }
            classesByDay.setClasses(userClassesByDayShortList);
            classesByDayResponse.add(classesByDay);
        }

        ClassesByDayResponse.sortClassesBySchedule(classesByDayResponse);

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
