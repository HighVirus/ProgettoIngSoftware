package me.unipa.progettoingsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class InitController {

    @FXML
    public void onClickLoginButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        new LoginC(stage);
    }
}
