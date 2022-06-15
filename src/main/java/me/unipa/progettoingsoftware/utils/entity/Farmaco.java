package me.unipa.progettoingsoftware.utils.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class Farmaco {
    private final String codAic;
    private final String lotto;
    private final String farmacoName;
    private final String principioAttivo;
    private final boolean prescrivibile;
    private final Date scadenza;
    private final Double costo;
    private int unita;

    public Farmaco(String codAic, String farmacoName, String principioAttivo, boolean prescrivibile, Double costo){
        this(codAic, null, farmacoName, principioAttivo, prescrivibile, null, costo, 0);
    }
}
