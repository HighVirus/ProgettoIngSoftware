package me.unipa.progettoingsoftware.gestionedati.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CarrelloE {
    @Getter(lazy = true)
    private static final CarrelloE instance = new CarrelloE();
    @Getter
    private final List<Farmaco> farmaci = new ArrayList<>();

    public void removeFarmaci() {
        this.farmaci.clear();
    }
}
