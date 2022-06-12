package me.unipa.progettoingsoftware.gestioneareaaziendale;

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
import me.unipa.progettoingsoftware.autenticazione.User;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.io.IOException;

public class HomePageAziendaController extends Homepage {
    private final Stage stage;

    @FXML
    @Setter
    @Getter
    private Label welcomeText;

    public HomePageAziendaController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    /*@FXML
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

    }*/

    public void onClickCatalogoButton(ActionEvent event) {
    }

    public void onClickMagazzinoButton(ActionEvent event) {
    }

    public void onClickOrdiniButton(ActionEvent event) {
    }
}
