package com.adpe.orders_system.model;

public enum Rols {

    customer("customer"),
    admin("admin");
    
    private final String string;

    Rols(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public static Rols fromString(String string) {
        for (Rols Rols : Rols.values()) {
            if (Rols.string.equalsIgnoreCase(string)) {
                return Rols;
            }
        }
        throw new IllegalArgumentException("No enum constant for string: " + string);
    }
}
