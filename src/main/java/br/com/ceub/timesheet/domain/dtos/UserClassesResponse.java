package br.com.ceub.timesheet.domain.dtos;

import br.com.ceub.timesheet.domain.entities.Classes;
import br.com.ceub.timesheet.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClassesResponse {
    private Long id;
    private String name;
    private String email;
    private List<Classes> classes;
}
