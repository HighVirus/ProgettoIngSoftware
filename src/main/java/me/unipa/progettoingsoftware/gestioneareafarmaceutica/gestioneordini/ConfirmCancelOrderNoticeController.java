package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale.StorageAziendaC;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

@RequiredArgsConstructor
public class ConfirmCancelOrderNoticeController extends AnchorPane {

    private final Order order;
    private final OrdersFarC ordersFarC;

    @FXML
    public void onclickConfirmCancelButton(ActionEvent event) {
        ordersFarC.clickConfirmCancelButton(order.getOrderCode());
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ordersFarC.setOrderToCancel(null);
        stage.close();
    }
}
