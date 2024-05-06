package br.com.pontoceub.controller;

import br.com.pontoceub.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

public abstract class AbstractController<T, ID, SERVICE extends BaseService<T, ID>> {

    protected SERVICE service;

    public AbstractController(SERVICE service) {
        this.service = service;
    }

    @GetMapping
    Page<T> all(Pageable pageable, T search)
    {
        return service.findAll(pageable, search);
    }

    @GetMapping("/{id}")
    T one(@PathVariable ID id)
    {
        return service.findById(id);
    }

    @PostMapping
    T newEntity(@RequestBody T newEntity)
    {
        return service.save(newEntity);
    }

    @PutMapping("/{id}")
    T replace(@RequestBody T newEntity, @PathVariable ID id)
    {
        return service.update(newEntity, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable ID id)
    {
        service.delete(id);
    }

}
