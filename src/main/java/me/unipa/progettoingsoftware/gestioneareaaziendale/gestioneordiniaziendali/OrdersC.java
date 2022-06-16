package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.externalcomponents.DBMSB;
import me.unipa.progettoingsoftware.gestioneareaaziendale.ConfirmRemNotice;
import me.unipa.progettoingsoftware.gestioneareaaziendale.ConfirmRemNoticeController;
import me.unipa.progettoingsoftware.utils.GenericNotice;
import me.unipa.progettoingsoftware.utils.entity.Order;

@RequiredArgsConstructor
public class OrdersC {
    private final Stage stage;
    private OrderListBController orderListBController;
    @Getter
    @Setter
    private Order order;

    public void showOrderList() {
        DBMSB.getAzienda().getOrderList().whenComplete((orders, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(orders -> {
            Platform.runLater(() -> {
                this.orderListBController = new OrderListBController(stage, this, orders);
                FXMLLoader fxmlLoader = new FXMLLoader(OrderListB.class.getResource("OrderListB.fxml"));
                fxmlLoader.setRoot(this.orderListBController);
                fxmlLoader.setController(this.orderListBController);

                new OrderListB(this.stage, fxmlLoader);
            });
        });
    }

    /*public void showOrderDetail() {
        ConfirmRemNoticeController confirmRemNoticeController = new ConfirmRemNoticeController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(ConfirmRemNotice.class.getResource("ConfirmRemNotice.fxml"));
        fxmlLoader.setRoot(confirmRemNoticeController);
        fxmlLoader.setController(confirmRemNoticeController);
        new ConfirmRemNotice(new Stage(), fxmlLoader);
    }*/

}
