package br.com.ceub.timesheet.domain.dtos;

import lombok.Data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class ClassesByDayResponse {
    private String day;
    private List<UserClassesByDayShort> classes;

    public static void sortClassesBySchedule(List<ClassesByDayResponse> list) {
        for (ClassesByDayResponse response : list) {
            Collections.sort(response.getClasses(), new Comparator<UserClassesByDayShort>() {
                @Override
                public int compare(UserClassesByDayShort class1, UserClassesByDayShort class2) {
                    return class1.getSchedule().compareTo(class2.getSchedule());
                }
            });
        }
    }
}
