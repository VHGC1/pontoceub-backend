package br.com.pontoceub.domain.entities;

import br.com.pontoceub.utils.ActivityType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class TimeRegistry implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTimeRegistry;
    private ActivityType registryType;
    private String className;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classes_id")
    private Classes classes;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }
}
