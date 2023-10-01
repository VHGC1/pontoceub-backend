package br.com.ceub.timesheet.domain.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "classes")
@Data
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "discipline", length = 75, nullable = false)
    private String discipline;

    @Column(name = "campus", length = 15, nullable = false)
    private String campus;

    @Column(name = "classDay", length = 15, nullable = false)
    private String classDay;

    @Column(name = "schedule", length = 15, nullable = false)
    private String schedule;
}