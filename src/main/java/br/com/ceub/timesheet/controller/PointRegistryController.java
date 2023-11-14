package br.com.ceub.timesheet.controller;

import br.com.ceub.timesheet.domain.dtos.PointRegistryCreateRequest;
import br.com.ceub.timesheet.domain.entities.PointRegistry;
import br.com.ceub.timesheet.service.PointRegistryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/registries")
@RolesAllowed({"ROLE_NORMAL_USER", "ROLE_ADMIN_USER"})
public class PointRegistryController {
    private final PointRegistryService pointRegistryService;

    public PointRegistryController(PointRegistryService pointRegistryService) {
        this.pointRegistryService = pointRegistryService;
    }

    @PostMapping("/create")
    public ResponseEntity<PointRegistry> create(@RequestBody PointRegistryCreateRequest userRegistry) {
        return pointRegistryService.createPointRegistry(userRegistry);
    }

    @GetMapping("/{id}/{pageSize}/{pageNo}")
    public ResponseEntity<List<PointRegistry>> getAllUserPointRegistries(@PathVariable("id") Long id, @PathVariable("pageSize") int pageSize, @PathVariable("pageNo") int pageNo) {
        return pointRegistryService.userPointRegistriesPaginated(id, pageSize, pageNo);
    }
}
