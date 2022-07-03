package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class PrenFarmForm extends Application {

    private final FXMLLoader fxmlLoader;
    private final String farmacoName;

    public PrenFarmForm(Stage stage, FXMLLoader fxmlLoader, String farmacoName) {
        this.fxmlLoader = fxmlLoader;
        this.farmacoName = farmacoName;

        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        PrenFarmFormController prenFarmFormController = fxmlLoader.getController();
        prenFarmFormController.getTextToShow().setText(prenFarmFormController.getTextToShow().getText()
                .replaceAll("%NomeFarmaco%", farmacoName));
        stage.show();
    }
}
