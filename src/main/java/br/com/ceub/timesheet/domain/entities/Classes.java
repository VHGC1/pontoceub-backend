package br.com.ceub.timesheet.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "classes")
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter(AccessLevel.NONE)
    @Column(name = "userId", nullable = false, insertable = false, updatable = false)
    private Long userId;

    @Column(name = "course", length = 5, nullable = false)
    private String course;

    @Column(name = "discipline", length = 75, nullable = false)
    private String discipline;

    @Column(name = "campus", length = 15, nullable = false)
    private String campus;

    @Column(name = "turn", length = 10, nullable = false)
    private String turn;

    @Column(name = "schedule", length = 65, nullable = false)
    private String schedule;

    @Column(name = "toBegin", length = 10, nullable = false)
    private Date begin;

    @Column(name = "toEnd", length = 10, nullable = false)
    private Date end;

    @Column(name = "students", length = 2, nullable = false)
    private Integer students;
}
