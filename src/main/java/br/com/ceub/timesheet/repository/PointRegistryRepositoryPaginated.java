package br.com.ceub.timesheet.repository;

import br.com.ceub.timesheet.domain.entities.PointRegistry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PointRegistryRepositoryPaginated  extends PagingAndSortingRepository<PointRegistry, Long> {
    List<PointRegistry> findByUserId(Long userId, Pageable pageable);
}
