package br.com.ceub.timesheet.domain.dtos;

import br.com.ceub.timesheet.domain.entities.Position;
import lombok.Data;

@Data
public class PointRegistryCreateRequest {
    private Long id;
    private Position position;
}
