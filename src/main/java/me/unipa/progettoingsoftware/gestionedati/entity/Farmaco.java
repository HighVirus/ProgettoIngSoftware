package me.unipa.progettoingsoftware.gestionedati.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class Farmaco {
    @Setter
    private String orderCode;
    private final String codAic;
    @Setter
    private String lotto;
    private final String farmacoName;
    private final String principioAttivo;
    private final boolean prescrivibile;
    private final Date scadenza;
    @Setter
    private double costo;
    @Setter
    private int unita;

    public Farmaco(String codAic, String lotto, String farmacoName, String principioAttivo, boolean prescrivibile, Date scadenza, double costo, int unita) {
        this(null, codAic, lotto, farmacoName, principioAttivo, prescrivibile, scadenza, costo, unita);
    }

    public Farmaco(String codAic, String farmacoName, String principioAttivo, Double costo) {
        this(null, codAic, null, farmacoName, principioAttivo, false, null, costo, 0);
    }

    public Farmaco(String codAic, String lotto, String farmacoName, int unita) {
        this(null, codAic, lotto, farmacoName, null, false, null, 0D, unita);
    }

    public Farmaco(String codAic, String lotto, String farmacoName) {
        this(null, codAic, lotto, farmacoName, null, false, null, 0D, 0);
    }

    public Farmaco(String codAic, String farmacoName, String principioAttivo, boolean isPrescrivibile, Double costo) {
        this(null, codAic, null, farmacoName, principioAttivo, isPrescrivibile, null, costo, 0);
    }

    public Farmaco(String codAic, String lotto, String farmacoName, String principioAttivo, boolean isPrescrivibile, Date expireDate, int unita) {
        this(null, codAic, lotto, farmacoName, principioAttivo, isPrescrivibile, expireDate, 0, unita);
    }

}
