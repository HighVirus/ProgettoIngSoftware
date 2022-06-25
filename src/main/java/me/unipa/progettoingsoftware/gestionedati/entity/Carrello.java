package me.unipa.progettoingsoftware.gestionedati.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Carrello {
    @Getter(lazy = true)
    private static final Carrello instance = new Carrello;
    private final List<Farmaco> farmacoList= new ArrayList<>();

    public static Carrello createInstance(int id, int type, String email, String name, String surname) {
        user = new User(id, type, email, name, surname);
        return user;
    }
}
