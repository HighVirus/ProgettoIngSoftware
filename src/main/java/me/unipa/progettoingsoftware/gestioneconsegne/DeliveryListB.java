package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.ViewOrdiniController;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

import java.net.URL;
import java.util.ResourceBundle;

public class DeliveryListB extends Application {


    private final FXMLLoader fxmlLoader;

    public DeliveryListB(Stage stage, FXMLLoader fxmlLoader) {
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
        DeliveryListBController deliveryListBController = fxmlLoader.getController();
        deliveryListBController.setupTable();
        deliveryListBController.getWelcomeText().setText(deliveryListBController.getWelcomeText().getText()
                .replaceAll("%utente%", User.getUser().getName() + " " + User.getUser().getSurname()));
        stage.show();
    }

}