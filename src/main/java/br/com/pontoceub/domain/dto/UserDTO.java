package br.com.pontoceub.domain.dto;

import br.com.pontoceub.domain.entities.Classes;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private List<ClassesDTO> classes;
}
