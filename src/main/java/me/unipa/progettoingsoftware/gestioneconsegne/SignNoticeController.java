package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

@RequiredArgsConstructor
public class SignNoticeController extends AnchorPane {
    private final DeliveriesC deliveriesC;
    private final Order order;
    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        deliveriesC.submitSign(order);

        stage.close();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
