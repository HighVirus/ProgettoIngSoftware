package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignNoticeController extends AnchorPane {

    private final DeliveriesC deliveriesC;

    @FXML
    public void onClickSubmitSign(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        deliveriesC.submitSign();
        stage.close();
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
