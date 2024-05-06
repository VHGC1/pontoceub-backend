package br.com.pontoceub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, ID> {

    Page<T> findAll(Pageable pageable, T search);

    void delete(ID id);

    T save(T entity);

    T update(T entity, ID id);

    T findById(ID id);
}
