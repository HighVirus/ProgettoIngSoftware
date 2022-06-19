package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
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
                Stage stage = new Stage();
                this.orderWindowBController = new OrderWindowBController(farmacos, this, stage);
                FXMLLoader fxmlLoader = new FXMLLoader(OrderWindowB.class.getResource("OrderWindowB.fxml"));
                fxmlLoader.setRoot(orderWindowBController);
                fxmlLoader.setController(orderWindowBController);
                new OrderWindowB(stage, fxmlLoader);
            });
        });
    }

    public void addProductToOrderList(Farmaco farmaco, String textField) {
        if (isValidUnitaField(textField)) {
            int unita = Integer.parseInt(textField);
            farmaco.setUnita(unita);
            for (Farmaco f1 : orderWindowBController.getFarmaciTable().getItems()) {
                if (f1.getCodAic().equalsIgnoreCase(farmaco.getCodAic())) {
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

    public void confirmOrder() {
        if (orderWindowBController.getFarmaciTable().getItems().isEmpty()) {
            new ErrorsNotice("Non ci sono farmaci nella lista dei farmaci da ordinare.");
            return;
        }
        if (orderWindowBController.getPiva().getText().isEmpty()) {
            new ErrorsNotice("Devi inserire la P.IVA della farmacia alla quale inviare l'ordine.");
            return;
        }
        String piva = orderWindowBController.getPiva().getText();
        DBMSB.getAzienda().getFarmaciaInfo(piva).thenAccept(strings -> {
            Platform.runLater(() -> {
                if (strings.isEmpty()){
                    new ErrorsNotice("Farmacia non trovata, ricontrolla la P.IVA");
                    return;
                }
                for (String s : strings)
                    System.out.println(s);
                orderWindowBController.getStage().close();
            });
        });
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
