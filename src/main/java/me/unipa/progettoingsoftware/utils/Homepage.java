package me.unipa.progettoingsoftware.utils;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.Init;
import me.unipa.progettoingsoftware.autenticazione.User;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class Homepage extends Application {
        private Stage stage;

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
