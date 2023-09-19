package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.domain.dtos.UserClassesResponse;
import br.com.ceub.timesheet.domain.dtos.UserCreateRequest;
import br.com.ceub.timesheet.domain.dtos.UserCreateResponse;
import br.com.ceub.timesheet.domain.entities.Role;
import br.com.ceub.timesheet.domain.entities.User;
import br.com.ceub.timesheet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserCreateResponse createUser(UserCreateRequest user) {
        HashMap<String, Integer> rolesDic = new HashMap<>();
        rolesDic.put("ROLE_NORMAL_USER", 1);
        rolesDic.put("ROLE_ADMIN_USER", 2);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já existente");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        List<Role> roles = new ArrayList<>();

        Role role = new Role();

        role.setId(1);
        role.setName("ROLE_NORMAL_USER");
        roles.add(role);

        User novoUser = new User();

        novoUser.setName(user.getName());
        novoUser.setEmail(user.getEmail());
        novoUser.setPassword(encodedPassword);
        novoUser.setRoles(roles);

        User userSalvo = userRepository.save(novoUser);

        return new UserCreateResponse(
                userSalvo.getId(),
                userSalvo.getName(),
                userSalvo.getEmail()
        );
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
