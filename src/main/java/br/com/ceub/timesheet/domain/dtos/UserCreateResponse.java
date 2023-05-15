package br.com.ceub.timesheet.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateResponse {
    private Long id;
    private String name;
    private String email;
}
