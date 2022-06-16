package me.unipa.progettoingsoftware.utils.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Order {
    private final String orderCode;
    private final Date deliveryDate;
    private final String pivaFarmacia;
    private final String indirizzo;
    private final String cap;
    private final String email;
    private final List<Farmaco> farmacoList = new ArrayList<>();
}
