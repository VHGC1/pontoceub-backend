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

    @Column(name = "course", length = 5, nullable = false)
    private String course;

    @Column(name = "discipline", length = 75, nullable = false)
    private String discipline;

    @Column(name = "campus", length = 15, nullable = false)
    private String campus;

    @Column(name = "turn", length = 10, nullable = false)
    private String turn;

    @Column(name = "day_first_class", length = 15, nullable = false)
    private String dayFirstClass;

    @Column(name = "hour_first_class", length = 15, nullable = false)
    private String hourFirstClass;

    @Column(name = "day_second_class", length = 15, nullable = false)
    private String daySecondClass;

    @Column(name = "hour_second_class", length = 15, nullable = false)
    private String hourSecondClass;
}
