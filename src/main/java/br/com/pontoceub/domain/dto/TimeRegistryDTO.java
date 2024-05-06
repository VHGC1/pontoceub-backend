package br.com.pontoceub.domain.dto;

import br.com.pontoceub.utils.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRegistryDTO {
    private Long id;
    private ActivityType activityType;
    private LocalDateTime dateTimeRegistry;
    private String activity;
    private Long userId;
    private Long classesId;
}
