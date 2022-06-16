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

    public Farmaco(String codAic, String farmacoName, String principioAttivo, Double costo){
        this(codAic, null, farmacoName, principioAttivo, false, null, costo, 0);
    }

    public Farmaco(String codAic, String farmacoName, int unita){
        this(codAic, null, farmacoName, null, false, null, 0D, unita);
    }

    public Farmaco(String codAic, String farmacoName, String principioAttivo, boolean isPrescribile, Double costo){
        this(codAic, null, farmacoName, principioAttivo, isPrescribile, null, costo, 0);
    }
}
