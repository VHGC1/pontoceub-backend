package br.com.pontoceub.utils;

public enum WeekDayEnum {
    SEGUNDA("SEGUNDA-FEIRA"),
    TERÇA("TERÇA-FEIRA"),
    QUARTA("QUARTA-FEIRA"),
    QUINTA("QUINTA-FEIRA"),
    SEXTA("SEXTA-FEIRA"),
    SABADO("SABADO");

    private final String value;

    WeekDayEnum(String value) {
        this.value = value;
    }

    public static WeekDayEnum fromValue(String value) {
        for (WeekDayEnum day : WeekDayEnum.values()) {
            if (day.value.equals(value.toUpperCase())) return day;
        }
        throw new IllegalArgumentException("Unexpected Value → " + value);
    }
}
