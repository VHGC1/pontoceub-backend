package br.com.ceub.timesheet.exception.dto;

public class Violation {
    private final String fieldName;
    private final String messge;

    public Violation(String fieldName, String messge) {
        this.fieldName = fieldName;
        this.messge = messge;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessge() {
        return messge;
    }
}
