package me.unipa.progettoingsoftware.gestionedati.entity;

import lombok.Getter;
import me.unipa.progettoingsoftware.utils.OrderStatus;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Getter
public class Order {
    private final String orderCode;
    private final Date deliveryDate;
    private final String pivaFarmacia;
    private final String farmaciaName;
    private final String indirizzo;
    private final String cap;
    private final String email;
    private final OrderStatus status;
    private final List<Farmaco> farmacoList = new ArrayList<>();

    public Order(String orderCode, Date deliveryDate, String pivaFarmacia, String farmaciaName, String indirizzo, String cap, String email, int stato) {
        this.orderCode = orderCode;
        this.deliveryDate = deliveryDate;
        this.pivaFarmacia = pivaFarmacia;
        this.farmaciaName = farmaciaName;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.email = email;
        this.status = OrderStatus.getFromType(stato);
    }

}
