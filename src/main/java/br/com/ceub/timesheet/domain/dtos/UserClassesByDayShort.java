package br.com.ceub.timesheet.domain.dtos;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserClassesByDayShort {
    private String discipline;
    private String campus;
    private String schedule;
}
