package br.com.ceub.timesheet.repository;

import br.com.ceub.timesheet.domain.entities.Classes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassesRepository extends JpaRepository<Classes, Long> {
}
