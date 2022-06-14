package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.Init;

import java.io.IOException;

@RequiredArgsConstructor
public class CatalogoAzControl {

    private final Stage stage;
    private GestioneCatalogoController gestioneCatalogoController;


    public void showCatalogoAziendale() {
        this.gestioneCatalogoController = new GestioneCatalogoController(stage, this);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneCatalogo.class.getResource("GestioneCatalogo.fxml"));
        fxmlLoader.setRoot(this.gestioneCatalogoController);
        fxmlLoader.setController(this.gestioneCatalogoController);

        new GestioneCatalogo(this.stage, fxmlLoader);
    }

    public void addProductRequest() {
        FXMLLoader fxmlLoader = new FXMLLoader(AggiungiFarmaco.class.getResource("AggiungiFarmaco.fxml"));
        AggiungiFarmacoController aggiungiFarmacoController = new AggiungiFarmacoController(this);
        fxmlLoader.setRoot(aggiungiFarmacoController);
        fxmlLoader.setController(aggiungiFarmacoController);
        new AggiungiFarmaco(new Stage(), fxmlLoader);

    }
}
