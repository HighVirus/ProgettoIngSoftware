package me.unipa.progettoingsoftware.utils;

import lombok.Getter;

public enum AlertType {
    AZIENDA(3),
    FARMACIA_QUANTITY(1),
    FARMACIA_CARICO(2);

    @Getter
    private final int type;

    AlertType(int type) {
        this.type = type;
    }
}
