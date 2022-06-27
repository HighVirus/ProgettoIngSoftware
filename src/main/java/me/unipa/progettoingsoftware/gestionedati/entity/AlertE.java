package me.unipa.progettoingsoftware.gestionedati.entity;

import lombok.Getter;
import me.unipa.progettoingsoftware.utils.AlertType;

import java.util.List;


public class AlertE {
    @Getter
    private final AlertType alertType;
    @Getter
    private final String message;

    @Getter
    private final List<Order> caricoOrderList;

    @Getter
    private final List<Farmaco> farmacoQuantityList;

    public AlertE (AlertType alertType, String piva, String farmaciaName){
        this.alertType = alertType;
        this.message = "La farmacia " + piva + "|" + farmaciaName + " ha un problema con uno o più ordini, contattala.";
        this.caricoOrderList = null;
        this.farmacoQuantityList = null;
    }

    public AlertE (AlertType alertType, List<Order> caricoOrderList, String uselessIdentifier){
        this.alertType = alertType;
        this.message = "Ci sono degli ordini che non hai caricato, verifica.";
        this.caricoOrderList = caricoOrderList;
        this.farmacoQuantityList = null;
    }

    public AlertE (AlertType alertType, List<Farmaco> farmacoQuantityList){
        this.alertType = alertType;
        this.message = "Ci sono dei farmaci in esaurimento! Verifica.";
        this.farmacoQuantityList = farmacoQuantityList;
        this.caricoOrderList = null;
    }
}
