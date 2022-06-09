package me.unipa.progettoingsoftware.autenticazione;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {
    private final int type;
    private final String email;
    private final String name;
    @Getter
    private static User user = null;

    public static User createInstance(int type, String email, String name) {
        user = new User(type, email, name);
        return user;
    }

    public static boolean isAuthenticated() {
        return user != null;
    }

    public void logout() {
        user = null;
    }
}
