package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.utils.entity.User;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderListB extends Application {

    private final FXMLLoader fxmlLoader;

    public OrderListB(Stage stage, FXMLLoader fxmlLoader) {
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
        OrderListBController storageAziendaBController = fxmlLoader.getController();
        storageAziendaBController.setupTable();
        storageAziendaBController.getWelcomeText().setText(storageAziendaBController.getWelcomeText().getText()
                .replaceAll("%utente%", User.getUser().getName() + " " + User.getUser().getSurname()));
        stage.show();
    }
}