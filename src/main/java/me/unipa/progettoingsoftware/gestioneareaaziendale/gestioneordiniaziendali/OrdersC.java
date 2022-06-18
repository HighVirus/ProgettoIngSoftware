package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.externalcomponents.DBMSB;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.entity.Order;

@RequiredArgsConstructor
public class OrdersC {
    private final Stage stage;
    private OrderListBController orderListBController;
    private OrderWindowBController orderWindowBController;

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
                this.orderWindowBController = new OrderWindowBController(farmacos, this);
                FXMLLoader fxmlLoader = new FXMLLoader(OrderWindowB.class.getResource("OrderWindowB.fxml"));
                fxmlLoader.setRoot(orderWindowBController);
                fxmlLoader.setController(orderWindowBController);
                new OrderWindowB(new Stage(), fxmlLoader);
            });
        });
    }

    public void addProductToOrderList(Farmaco farmaco, String textField) {
        if (isValidUnitaField(textField)) {
            int unita = Integer.parseInt(textField);
            farmaco.setUnita(unita);
            for (Farmaco f1 : orderWindowBController.getFarmaciTable().getItems()){
                if (f1.getCodAic().equalsIgnoreCase(farmaco.getCodAic())){
                    new ErrorsNotice("Prodotto gia aggiunto nella lista dei farmaci da ordinare");
                    return;
                }
            }
            DBMSB.getAzienda().checkFarmacoAvailability(farmaco.getCodAic(), unita).thenAccept(aBoolean -> {
                Platform.runLater(() -> {
                    if (aBoolean)
                        orderWindowBController.getFarmaciTable().getItems().add(farmaco);
                    else
                        new ErrorsNotice("Quantità non disponibile.");
                });
            });
        } else {
            new ErrorsNotice("Hai inserito un valore non valido, ricontrolla.");
        }
    }

    private boolean isValidUnitaField(String textField) {
        if (textField.length() == 0)
            return false;
        try {
            int value = Integer.parseInt(textField);
            return value > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
