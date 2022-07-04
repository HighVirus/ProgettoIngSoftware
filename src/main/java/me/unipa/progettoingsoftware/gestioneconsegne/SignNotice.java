package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class SignNotice extends Application {

    private final FXMLLoader fxmlLoader;
    private final String farmaciaName;

    public SignNotice(Stage stage, FXMLLoader fxmlLoader, String farmaciaName) {
        this.fxmlLoader = fxmlLoader;
        this.farmaciaName = farmaciaName;
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
        SignNoticeController signNoticeController = fxmlLoader.getController();
        signNoticeController.getTextToShow().setText(signNoticeController.getTextToShow().getText()
                .replaceAll("%NomeFarmacia%", farmaciaName));
        stage.show();
    }
}
