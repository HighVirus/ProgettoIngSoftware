package me.unipa.progettoingsoftware.externalcomponents;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConnectionAttempt extends Application {
    private final FXMLLoader fxmlLoader;

    public ConnectionAttempt(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;

        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
