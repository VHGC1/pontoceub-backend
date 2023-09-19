package br.com.ceub.timesheet.domain.entities;

import br.com.ceub.timesheet.Utils.ActivityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "point_registry")
@Data
public class PointRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private ActivityType activityType;
    private LocalDateTime dateTimeRegistry;
    private String activity;
}
