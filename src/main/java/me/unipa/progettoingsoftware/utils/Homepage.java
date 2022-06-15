package me.unipa.progettoingsoftware.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.unipa.progettoingsoftware.Init;
import me.unipa.progettoingsoftware.utils.entity.User;

import java.io.IOException;

public abstract class Homepage extends AnchorPane {
    private final Stage stage;

    public Homepage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    @Setter
    @Getter
    private Label welcomeText;

    @FXML
    public void onClickLogoutButton(ActionEvent event) {
        if (User.isAuthenticated())
            User.getUser().logout();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Init.class.getResource("InitPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Nome Farmacia");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
