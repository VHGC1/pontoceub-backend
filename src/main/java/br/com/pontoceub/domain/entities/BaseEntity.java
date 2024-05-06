package br.com.pontoceub.domain.entities;

public interface BaseEntity<ID> {

    ID getId();

    void setId(ID id);
}
