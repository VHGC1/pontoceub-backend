package br.com.ceub.timesheet.repository;

import br.com.ceub.timesheet.domain.entities.PointRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRegistryRepository extends JpaRepository<PointRegistry, Long> {
    List<PointRegistry> findByUserId(Long userId);
}
