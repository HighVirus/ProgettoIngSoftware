package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProdForm extends Application {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;

    public AddProdForm(Stage stage, FXMLLoader fxmlLoader) {
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
        stage.setTitle("Aggiungi Nuovo Farmaco");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        AddProdFormController addProdFormController = fxmlLoader.getController();
        addProdFormController.setupPrescrivibilitaComboBox();
        stage.show();
    }
}
