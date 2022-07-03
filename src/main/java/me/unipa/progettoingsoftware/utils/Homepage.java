package me.unipa.progettoingsoftware.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.unipa.progettoingsoftware.Init;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

import java.io.IOException;

public abstract class Homepage extends AnchorPane {
    private final Stage stage;
    private String piva;

    public Homepage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    @Getter
    private Label welcomeText;

    @FXML
    @Getter
    private MFXButton alertButton;

    @FXML
    public void onClickLogoutButton(ActionEvent event) {
        if (User.isAuthenticated())
            User.getUser().logout();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Init.class.getResource("InitPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Ipazia");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onClickAlertButton(ActionEvent event) {
        if (User.isAuthenticated()) {
            if (User.getUser().getType() == 1) {
                DBMSB.getAzienda().getAlertsAzienda();
                new AlertC(stage).showAlertReport();
            } else if (User.getUser().getType() == 2) {
                DBMSB.getFarmacia().getAlertList(piva);
                new AlertC(stage).showAlertList();
            }
        }
        TempoC.getInstance().setAlertsToRead(false);
        Image buttonImage = new Image(getClass().getResourceAsStream("/images/bell-alert.png"));
        ImageView imageView = new ImageView(buttonImage);
        imageView.setFitWidth(27);
        imageView.setFitHeight(27);
        this.alertButton.setGraphic(imageView);
    }

}
