package br.com.pontoceub.repository;

import br.com.pontoceub.domain.dto.TimeRegistryDTO;
import br.com.pontoceub.domain.entities.TimeRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRegistryRepository extends JpaRepository<TimeRegistry, Long> {
    List<TimeRegistry> findByUserId(Long id);

    Page<TimeRegistry> findByUserId(Pageable pageable,Long id);
}
