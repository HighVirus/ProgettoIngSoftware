package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmSellB extends Application {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;
    private double total;

    public ConfirmSellB(FXMLLoader fxmlLoader, double costo) {
        this.stage = new Stage();
        this.fxmlLoader = fxmlLoader;
        this.total = costo;
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
        ConfirmSellBController confirmSellBController = fxmlLoader.getController();
        confirmSellBController.getTextToShow().setText(confirmSellBController.getTextToShow().getText().replaceAll("%total%", String.valueOf(total)));
        stage.show();
    }
}
