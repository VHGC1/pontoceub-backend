package br.com.ceub.timesheet.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ClassesByDayResponse {
    private String day;
    private List<UserClassesByDayShort> classes;
}
