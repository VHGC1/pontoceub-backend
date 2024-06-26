package br.com.pontoceub.service;

import br.com.pontoceub.domain.dto.ClassesByDayDTO;
import br.com.pontoceub.domain.dto.ClassesDTO;
import br.com.pontoceub.domain.dto.UserDTO;
import br.com.pontoceub.domain.entities.Classes;
import br.com.pontoceub.repository.ClassesRepository;
import br.com.pontoceub.utils.WeekDayEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassesService extends AbstractDTOService<ClassesDTO, Classes, Long, ClassesRepository> {

    private final UserService userService;

    public ClassesService(UserService userService, ClassesRepository classesRepository) {
        super(classesRepository);
        this.userService = userService;
    }

    public List<ClassesByDayDTO> getUserClassesByDay() {
        List<Classes> classes = repo.findByUserId(userService.getUserIdFromRequest());

        List<ClassesByDayDTO> classesByDay = new ArrayList<>();

        List<String> uniqueClassDays = classes.stream()
                .map(Classes::getClassDay)
                .distinct()
                .map(WeekDayEnum::fromValue)
                .sorted()
                .map(WeekDayEnum::toString)
                .map(i -> {
                    String day = i.toLowerCase();
                    System.out.println(day);
                    if (!day.equals("sabado")) {
                        day += "-feira";
                    }
                    return day;
                }).toList();

        for (String uniqueClassDay : uniqueClassDays) {
            ClassesByDayDTO classByDay = new ClassesByDayDTO();
            classByDay.setDay(uniqueClassDay);

            List<ClassesDTO> classesDTOS = new ArrayList<>();

            for (Classes aClass : classes) {
                if (aClass.getClassDay().equalsIgnoreCase(uniqueClassDay)) {
                    classesDTOS.add(entityToDTO(aClass));
                }
            }
            classByDay.setClasses(classesDTOS);
            classesByDay.add(classByDay);
        }

        ClassesByDayDTO.sortClassesBySchedule(classesByDay);

        return classesByDay;
    }

    public List<ClassesDTO> createUserClasses(List<ClassesDTO> classes) {
        Long userId = userService.getUserIdFromRequest();

        userService.findEntityById(userId);

        for (ClassesDTO aClass : classes) {
            Classes newClasses = dtoToEntity(aClass);

            repo.save(newClasses);
        }

        return getUserClasses(userId);
    }

    public List<ClassesDTO> getUserClasses(Long id) {
        return repo.findByUserId(id).stream().map(this::entityToDTO).collect(Collectors.toList());
    }
}
