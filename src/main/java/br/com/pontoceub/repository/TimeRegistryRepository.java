package br.com.pontoceub.repository;

import br.com.pontoceub.domain.entities.TimeRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRegistryRepository extends JpaRepository<TimeRegistry, Long> {
    List<TimeRegistry> findByUserId(Long id);
}
