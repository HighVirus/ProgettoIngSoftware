package me.unipa.progettoingsoftware.utils;

import lombok.Getter;

public enum AlertType {
    AZIENDA(3, "Azienda"),
    FARMACIA_QUANTITY(1, "Quantità"),
    FARMACIA_CARICO(2, "Carico");

    @Getter
    private final int type;

    @Getter
    private final String value;

    AlertType(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public static AlertType getFromType(int type){
        return switch (type) {
            case 1 -> FARMACIA_QUANTITY;
            case 2 -> FARMACIA_CARICO;
            case 3 -> AZIENDA;
            default -> null;
        };
    }

    public static AlertType getFromValue(String value){
        return switch (value) {
            case "Quantità" -> FARMACIA_QUANTITY;
            case "Carico" -> FARMACIA_CARICO;
            case "Azienda" -> AZIENDA;
            default -> null;
        };
    }
}
