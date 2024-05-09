package br.com.pontoceub.controller;

import br.com.pontoceub.domain.dto.UserDTO;
import br.com.pontoceub.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<UserDTO, Long, UserService> {
    public UserController(UserService service) {
        super(service);
    }
}
