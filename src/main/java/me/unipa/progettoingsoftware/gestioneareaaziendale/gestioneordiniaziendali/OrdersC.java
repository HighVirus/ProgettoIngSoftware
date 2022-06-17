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
import me.unipa.progettoingsoftware.utils.entity.Order;

@RequiredArgsConstructor
public class OrdersC {
    private final Stage stage;
    private OrderListBController orderListBController;

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

    public void showInfoOrder(Order order) {
        InfoOrderBController infoOrderBController = new InfoOrderBController(order);
        FXMLLoader fxmlLoader = new FXMLLoader(InfoOrderB.class.getResource("InfoOrderB.fxml"));
        fxmlLoader.setRoot(infoOrderBController);
        fxmlLoader.setController(infoOrderBController);
        new InfoOrderB(new Stage(), fxmlLoader);
    }

    public void showCreateOrder() {
        DBMSB.getAzienda().getFarmaciCatalogList().whenComplete((farmacos, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(farmacos -> {
            Platform.runLater(() -> {
                OrderWindowBController orderWindowBController = new OrderWindowBController(farmacos, this);
                FXMLLoader fxmlLoader = new FXMLLoader(OrderWindowB.class.getResource("OrderWindowB.fxml"));
                fxmlLoader.setRoot(orderWindowBController);
                fxmlLoader.setController(orderWindowBController);
                new OrderWindowB(new Stage(), fxmlLoader);
            });
        });

    }

}
