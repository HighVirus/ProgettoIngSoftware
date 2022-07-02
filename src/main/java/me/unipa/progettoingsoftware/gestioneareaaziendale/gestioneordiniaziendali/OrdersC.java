package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

import java.sql.Date;
import java.util.Random;

@RequiredArgsConstructor
public class OrdersC {
    private final Stage stage;
    private OrderListBController orderListBController;
    private OrderWindowBController orderWindowBController;
    private String nomeCorriere;

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

    public void showHomePageAzienda() {
        new HomePageAzienda(this.stage, new FXMLLoader(HomePageAzienda.class.getResource("HomePageAzienda.fxml")));
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

    public void submitOrder() {
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
                String orderCode = String.valueOf(new Random(System.currentTimeMillis()).nextInt(99999));
                Order order = new Order(nomeCorriere, orderCode, new Date(System.currentTimeMillis() + 3000), piva, strings.get(0), strings.get(2), strings.get(3), strings.get(1), 1);
                for (Farmaco farmaco : orderWindowBController.getFarmaciTable().getItems())
                    order.getFarmacoList().add(farmaco);
                DBMSB.getAzienda().createNewOrder(order);
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

    private String getRandomLottoCode() {
        String alphaStr = "abcdefghijklmnopqurestuvwxyz";
        String numStr = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        for (int j = 0; j < 3; j++) {
            double randNo = new Random(System.currentTimeMillis()).nextDouble();
            int idx = (int) (alphaStr.length() * randNo);
            stringBuilder.append(alphaStr.charAt(idx));
        }

        for (int j = 0; j < 4; j++) {
            double randNo = new Random(System.currentTimeMillis()).nextDouble();
            int idx = (int) (numStr.length() * randNo);
            stringBuilder.append(numStr.charAt(idx));
        }
        return stringBuilder.toString();
    }

}
