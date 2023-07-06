package br.com.ceub.timesheet.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "classes")
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "course", length = 5, nullable = false)
    private String course;

    @Column(name = "discipline", length = 75, nullable = false)
    private String discipline;

    @Column(name = "campus", length = 15, nullable = false)
    private String campus;

    @Column(name = "turn", length = 10, nullable = false)
    private String turn;

    @Column(name = "scheduleFirstClass", length = 32, nullable = false)
    private String scheduleFirstClass;

    @Column(name = "scheduleSecondClass", length = 32, nullable = false)
    private String scheduleSecondClass;

    @JsonFormat(pattern="dd-MM-yyyy")
    @Column(name = "toBegin", length = 10, nullable = false)
    private Date begin;

    @JsonFormat(pattern="dd-MM-yyyy")
    @Column(name = "toEnd", length = 10, nullable = false)
    private Date end;
}
