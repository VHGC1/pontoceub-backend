package br.com.pontoceub.domain.dto;

import br.com.pontoceub.domain.entities.User;
import lombok.Data;

@Data
public class ClassesDTO {
    private Long id;
    private String discipline;
    private String campus;
    private String classDay;
    private String schedule;
    private Long userId;
}
