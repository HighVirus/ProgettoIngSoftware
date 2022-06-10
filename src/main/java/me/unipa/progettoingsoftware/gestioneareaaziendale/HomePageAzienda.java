package me.unipa.progettoingsoftware.gestioneareaaziendale;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.autenticazione.User;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageAzienda extends Homepage {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;

    public HomePageAzienda(Stage stage, FXMLLoader fxmlLoader) {
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
        stage.show();
    }
}