package br.com.ceub.timesheet.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ClassCreateRequest {
    private String discipline;
    private String campus;
    private String classDay;
    private String schedule;
}
