package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.domain.dtos.LoginRequest;
import br.com.ceub.timesheet.domain.dtos.LoginResponse;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.repository.UserRepository;
import br.com.ceub.timesheet.security.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LoginService {

    private final JwtHelper jwtHelper;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;


    private final UserRepository userRepository;

    public LoginService(JwtHelper jwtHelper, PasswordEncoder passwordEncoder, UserService userService, UserRepository userRepository) {
        this.jwtHelper = jwtHelper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public String tokenReturn(LoginRequest loginRequest) {
        Optional<User> user = userService.findByEmail(loginRequest.email());

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e/ou senha invalidos");
        }

        boolean passwordIsValid = passwordEncoder.matches(loginRequest.password(), user.get().getPassword());
        if (!passwordIsValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e/ou senha invalidos");

        return jwtHelper.createJwt(user.get());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e/ou senha invalidos");
        }

        return new LoginResponse(tokenReturn(loginRequest), user.get());
    }
}
