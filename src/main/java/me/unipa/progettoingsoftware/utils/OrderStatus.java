package me.unipa.progettoingsoftware.utils;

import lombok.Getter;

public enum OrderStatus {
    IN_CONSEGNA("In consegna", 1),
    CONSEGNATO_DA_CARICARE("Consegnato non ancora caricato", 2),
    CONSEGNATO_CARICATO("Consegnato e caricato", 3);

    @Getter
    private final String value;
    @Getter
    private final int statusInt;

    OrderStatus(String value, int statusInt){
        this.value = value;
        this.statusInt = statusInt;
    }

    public static OrderStatus getFromType(int type){
        return switch (type) {
            case 1 -> IN_CONSEGNA;
            case 2 -> CONSEGNATO_DA_CARICARE;
            case 3 -> CONSEGNATO_CARICATO;
            default -> null;
        };
    }
}
