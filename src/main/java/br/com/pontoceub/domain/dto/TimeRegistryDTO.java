package br.com.pontoceub.domain.dto;

import br.com.pontoceub.utils.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRegistryDTO {
    private Long id;
    private LocalDateTime dateTimeRegistry;
    private ActivityType activityType;
    private Long userId;
    private ClassesDTO classes;
}
