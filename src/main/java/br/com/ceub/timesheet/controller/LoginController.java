package br.com.ceub.timesheet.controller;

import br.com.ceub.timesheet.domain.dtos.LoginRequest;
import br.com.ceub.timesheet.domain.dtos.LoginResponse;
import br.com.ceub.timesheet.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse response = loginService.login(loginRequest);

        return ResponseEntity.ok(response);
    }
}
