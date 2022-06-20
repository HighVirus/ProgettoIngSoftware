package me.unipa.progettoingsoftware.gestionedati.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class Farmaco {
    private final String codAic;
    @Setter
    private String lotto;
    private final String farmacoName;
    private final String principioAttivo;
    private final boolean prescrivibile;
    private final Date scadenza;
    private final Double costo;
    @Setter
    private int unita;

    public Farmaco(String codAic, String farmacoName, String principioAttivo, Double costo){
        this(codAic, null, farmacoName, principioAttivo, false, null, costo, 0);
    }

    public Farmaco(String codAic, String farmacoName, int unita){
        this(codAic, null, farmacoName, null, false, null, 0D, unita);
    }

    public Farmaco(String codAic, String farmacoName, String principioAttivo, boolean isPrescrivibile, Double costo){
        this(codAic, null, farmacoName, principioAttivo, isPrescrivibile, null, costo, 0);
    }
    public Farmaco(String codAic, String lotto, String farmacoName, String principioAttivo, boolean isPrescrivibile, Date expireDate, int unita){
        this(codAic, lotto, farmacoName, principioAttivo, isPrescrivibile, expireDate, null, unita);
    }
}
