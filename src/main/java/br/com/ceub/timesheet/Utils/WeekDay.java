package br.com.ceub.timesheet.Utils;

public enum WeekDay {
    SEGUNDA("SEGUNDA-FEIRA"),
    TERÇA("TERÇA-FEIRA"),
    QUARTA("QUARTA-FEIRA"),
    QUINTA("QUINTA-FEIRA"),
    SEXTA("SEXTA-FEIRA"),
    SABADO("SABADO");

    private final String value;

    WeekDay(String value) {
        this.value = value;
    }

    public static WeekDay fromValue(String value) {
        for (WeekDay day : WeekDay.values()) {
            if (day.value.equals(value.toUpperCase())) return day;
        }
        throw new IllegalArgumentException("Unexpected Value → " + value);
    }
}
