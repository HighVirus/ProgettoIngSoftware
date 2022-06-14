package me.unipa.progettoingsoftware.utils;


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
}
