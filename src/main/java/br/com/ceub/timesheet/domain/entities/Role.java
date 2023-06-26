package br.com.ceub.timesheet.domain.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    private Integer id;

    @Column(length = 20, unique = true, nullable = false)
    private String name;
}
