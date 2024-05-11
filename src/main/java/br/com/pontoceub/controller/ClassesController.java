package br.com.pontoceub.controller;

import br.com.pontoceub.domain.dto.ClassesByDayDTO;
import br.com.pontoceub.domain.dto.ClassesDTO;
import br.com.pontoceub.domain.dto.UserDTO;
import br.com.pontoceub.service.ClassesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClassesController extends AbstractController<ClassesDTO, Long, ClassesService> {
    public ClassesController(ClassesService service) {
        super(service);
    }

    @PostMapping("/user/")
    public List<ClassesDTO> createUserClasses(@RequestBody List<ClassesDTO> classes) {
        return service.createUserClasses(classes);
    }

    @GetMapping("/user")
    public List<ClassesByDayDTO> getUserClasses() {
        return service.getUserClassesByDay();
    }
}
