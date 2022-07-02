package me.unipa.progettoingsoftware.gestionedati.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {
    private final int id;
    private final int type;
    private final String email;
    private final String name;
    private final String surname;
    private final String farmaciaPiva;
    @Getter
    private static User user = null;

    public static User createInstance(int id, int type, String email, String name, String surname) {
        user = new User(id, type, email, name, surname, null);
        return user;
    }

    public static User createInstance(int id, int type, String email, String name, String surname, String farmaciaPiva) {
        user = new User(id, type, email, name, surname, farmaciaPiva);
        return user;
    }

    public static boolean isAuthenticated() {
        return user != null;
    }

    public void logout() {
        user = null;
    }
}
