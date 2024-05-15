package br.com.pontoceub.controller;

import br.com.pontoceub.domain.dto.LoginRequestDTO;
import br.com.pontoceub.domain.dto.TokenDTO;
import br.com.pontoceub.service.AuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
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
    public TokenDTO authenticate(@RequestBody LoginRequestDTO loginRequest) {
        return authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @GetMapping("/authenticated")
    public Boolean authenticated() {
        return true;
    }
}
