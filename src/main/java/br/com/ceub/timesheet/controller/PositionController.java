package br.com.ceub.timesheet.controller;

import br.com.ceub.timesheet.domain.entities.Position;
import br.com.ceub.timesheet.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/position-check")
@RolesAllowed({"ROLE_BACKOFFICE_USER", "ROLE_ADMIN_USER"})
public class PositionController {
    @Autowired
    private PositionService positionService;

    @GetMapping
    public ResponseEntity<Boolean> positionCheck(@RequestBody Position position) {
        return ResponseEntity.ok().body(positionService.checkPosition(position));
    }
}
