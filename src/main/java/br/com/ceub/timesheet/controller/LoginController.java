package br.com.ceub.timesheet.controller;

import br.com.ceub.timesheet.domain.dtos.LoginRequest;
import br.com.ceub.timesheet.domain.dtos.LoginResponse;
import br.com.ceub.timesheet.service.LoginService;
import br.com.ceub.timesheet.service.UserClassesService;
import br.com.ceub.timesheet.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    private final UserService userService;

    private final UserClassesService userClassesService;

    public LoginController(LoginService loginService, UserService userService, UserClassesService userClassesService) {
        this.loginService = loginService;
        this.userService = userService;
        this.userClassesService = userClassesService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse response = loginService.login(loginRequest);

        return ResponseEntity.ok(response);
    }
}
