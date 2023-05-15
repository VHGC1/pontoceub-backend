package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.domain.dtos.LoginRequest;
import br.com.ceub.timesheet.domain.dtos.LoginResponse;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.exception.AppError;
import br.com.ceub.timesheet.exception.AppException;
import br.com.ceub.timesheet.exception.EmailInvalidoException;
import br.com.ceub.timesheet.repository.UserRepository;
import br.com.ceub.timesheet.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    public String tokenReturn(LoginRequest loginRequest) {
        Optional<User> user = userService.findByEmail(loginRequest.email());

        if(user.isEmpty()) {
            throw AppException.of(AppError.INVALID_CREDENTIALS);
        }

        boolean passwordIsValid = passwordEncoder.matches(loginRequest.password(), user.get().getPassword());
        if(!passwordIsValid) throw AppException.of(AppError.INVALID_CREDENTIALS);

        return jwtHelper.createJwt(user.get());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> findUserByEmail = userRepository.findByEmail(loginRequest.email());

        if(findUserByEmail.isEmpty()) {
            throw new EmailInvalidoException("Senha incorreta ou email inexistente");
        }

        User user = userService.findByEmail(loginRequest.email()).get();

        return new LoginResponse(tokenReturn(loginRequest), user);
    }
}
