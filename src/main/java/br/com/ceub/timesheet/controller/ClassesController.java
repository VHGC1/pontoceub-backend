package br.com.ceub.timesheet.controller;

import br.com.ceub.timesheet.domain.dtos.ClassCreateRequest;
import br.com.ceub.timesheet.domain.dtos.ClassesByDayResponse;
import br.com.ceub.timesheet.domain.dtos.UserClassesResponse;
import br.com.ceub.timesheet.service.UserClassesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
//@RolesAllowed({"ROLE_BACKOFFICE_USER", "ROLE_ADMIN_USER"})
public class ClassesController {

    private final UserClassesService userClassesService;

    public ClassesController(UserClassesService userClassesService) {
        this.userClassesService = userClassesService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserClassesResponse> createUserClasses(@PathVariable("id") Long id, @RequestBody List<ClassCreateRequest> classes) {
        return ResponseEntity.ok(userClassesService.createUserClasses(id, classes));
    }

    //return list
    @GetMapping("/{id}")
    public ResponseEntity<List<ClassesByDayResponse>> getUserClasses(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userClassesService.getUserClasses(id));
    }
}
