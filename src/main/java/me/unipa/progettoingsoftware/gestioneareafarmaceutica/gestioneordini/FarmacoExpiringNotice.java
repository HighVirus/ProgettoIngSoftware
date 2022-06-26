package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmaciaController;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class FarmacoExpiringNotice extends Application {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;

    public FarmacoExpiringNotice(Stage stage, FXMLLoader fxmlLoader) {
        this.stage = stage;
        this.fxmlLoader = fxmlLoader;
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
        stage.show();
    }
}
