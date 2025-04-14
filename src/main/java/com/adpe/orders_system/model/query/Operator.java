package com.adpe.orders_system.model.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
    EQUALS("equals"),
    NOT_EQUALS("not_equals"),
    GREATER_THAN("greater_than"),
    LESS_THAN("less_than"),
    IN("in"),
    NOT_IN("not_in"),
    LIKE("like"),
    BETWEEN("between");

    private final String string;

    Operator(String string) {
        this.string = string;
    }

    @JsonValue
    public String getString() {
        return string; // Devuelve el valor como se espera en el JSON
    }

    @JsonCreator
    public static Operator fromString(String string) {
        for (Operator operator : Operator.values()) {
            if (operator.string.equalsIgnoreCase(string)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("No enum constant for string: " + string);
    }
}