package br.com.pontoceub.controller;

import br.com.pontoceub.domain.dto.LoginRequestDTO;
import br.com.pontoceub.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public String authenticate(@RequestBody LoginRequestDTO loginRequest) {
        return authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
