package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfirmRemNoticeController extends AnchorPane {
    private final CatalogoAzControl catalogoAzControl;

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        catalogoAzControl.confirmRemProduct();
        stage.close();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        catalogoAzControl.setFarmacoToRemove(null);
        stage.close();
    }
}
