package br.com.pontoceub.domain.dto;

import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Data
public class ClassesByDayDTO {
    private String day;
    private List<ClassesDTO> classes;

    public static void sortClassesBySchedule(List<ClassesByDayDTO> list) {
        for (ClassesByDayDTO response : list) {
            response.getClasses().sort(new Comparator<ClassesDTO>() {
                @Override
                public int compare(ClassesDTO class1, ClassesDTO class2) {
                    return class1.getSchedule().compareTo(class2.getSchedule());
                }
            });
        }
    }

}
