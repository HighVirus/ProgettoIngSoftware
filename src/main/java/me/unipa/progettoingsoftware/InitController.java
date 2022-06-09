package me.unipa.progettoingsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.autenticazione.AccessControl;

public class InitController {

    @FXML
    public void onClickLoginButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        new AccessControl(stage).showLoginForm();
    }
}
