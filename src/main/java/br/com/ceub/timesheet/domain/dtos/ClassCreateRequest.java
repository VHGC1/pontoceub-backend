package br.com.ceub.timesheet.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ClassCreateRequest {
    private String course;
    private String discipline;
    private String campus;
    private String turn;
    private String dayFirstClass;
    private String hourFirstClass;
    private String daySecondClass;
    private String hourSecondClass;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date begin;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date end;
}
