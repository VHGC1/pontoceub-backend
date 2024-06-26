package br.com.pontoceub.exceptions;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String entity, Object id) {
        super(String.format("Could not find %s %s", entity, id));
    }
}
