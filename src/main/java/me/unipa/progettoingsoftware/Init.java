package me.unipa.progettoingsoftware;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;

import java.io.IOException;

public class Init extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Init.class.getResource("InitPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ipazia");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/farmacia-logo.png")));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        DBMSB.getAzienda().closePool();
        DBMSB.getFarmacia().closePool();
    }
}