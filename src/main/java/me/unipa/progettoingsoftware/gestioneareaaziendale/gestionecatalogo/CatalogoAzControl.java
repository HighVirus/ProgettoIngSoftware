package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

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
}
