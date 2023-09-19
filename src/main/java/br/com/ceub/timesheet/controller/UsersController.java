package br.com.ceub.timesheet.controller;

import br.com.ceub.timesheet.domain.dtos.UserCreateRequest;
import br.com.ceub.timesheet.domain.dtos.UserCreateResponse;
import br.com.ceub.timesheet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUsers(@RequestBody UserCreateRequest user) {
        UserCreateResponse retorno = userService.createUser(user);

        return ResponseEntity.ok(retorno);
    }
}
