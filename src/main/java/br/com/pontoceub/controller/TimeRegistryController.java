package br.com.pontoceub.controller;

import br.com.pontoceub.domain.dto.TimeRegistryDTO;
import br.com.pontoceub.domain.entities.Position;
import br.com.pontoceub.service.TimeRegistryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/time-registry")
public class TimeRegistryController extends AbstractController<TimeRegistryDTO, Long, TimeRegistryService> {
    public TimeRegistryController(TimeRegistryService service) {
        super(service);
    }

    @PostMapping("/create")
    public TimeRegistryDTO create() {
        return service.createPointRegistry();
    }

    @GetMapping("/registries")
    public Page<TimeRegistryDTO> getRegistries(Pageable pageable) {
        return service.getRegistries(pageable);
    }
}
