package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

@RequiredArgsConstructor
public class ConfirmCancelOrderNoticeController extends AnchorPane {

    private final Order order;
    private final OrdersFarC ordersFarC;
    @Getter
    private final Stage stage;

    @FXML
    public void onclickConfirmCancelButton(ActionEvent event) {
        ordersFarC.deleteOrder(order.getOrderCode());
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ordersFarC.setOrderToCancel(null);
        stage.close();
    }
}
