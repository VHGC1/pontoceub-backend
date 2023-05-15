package br.com.ceub.timesheet.domain.dtos;

import br.com.ceub.timesheet.domain.entities.Role;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserCreateRequest {
    @NotEmpty(message = "O nome deve ser preenchido!")
    private String name;

    @NotEmpty(message = "Email deve ser preenchido")
    private String email;

    @NotEmpty(message = "Senha deve ser preenchida")
    private String password;
    private List<Role> roles;
}
