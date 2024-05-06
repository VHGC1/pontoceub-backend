package br.com.pontoceub.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String email;
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Classes> classes;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }
}
