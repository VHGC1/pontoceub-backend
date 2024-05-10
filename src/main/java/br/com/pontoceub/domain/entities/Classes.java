package br.com.pontoceub.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "classes")
public class Classes implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String campus;
    private String classDay;
    private String schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }
}
