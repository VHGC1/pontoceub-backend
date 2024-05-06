package br.com.pontoceub.repository;

import br.com.pontoceub.domain.entities.Classes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassesRepository extends JpaRepository<Classes, Long> {
    List<Classes> findByUserId(Long id);
}
