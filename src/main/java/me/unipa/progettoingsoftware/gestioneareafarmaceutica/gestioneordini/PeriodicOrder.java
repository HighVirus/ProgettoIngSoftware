package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class PeriodicOrder {
    private final String piva;
    private final String codAic;
    private final String farmacoName;
    private final String principioAttivo;
    @Setter
    private final int unita;
    private final String periodic;
}
