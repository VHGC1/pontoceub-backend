package br.com.ceub.timesheet.domain.dtos;

import br.com.ceub.timesheet.domain.entities.User;
import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private String token;

    public LoginResponse(String token, User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.token = token;
    }
}
