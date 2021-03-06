package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.utils.GenericNotice;

@RequiredArgsConstructor
public class DeliveriesC {
    private final Stage stage;
    private DeliveryListBController deliveryListBController;

    public void showDeliveryList() {
        DBMSB.getAzienda().getDeliveryList().whenComplete((orders, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(orders -> {
            Platform.runLater(() -> {
                this.deliveryListBController = new DeliveryListBController(stage, this, orders);
                FXMLLoader fxmlLoader = new FXMLLoader(DeliveryListB.class.getResource("DeliveryListB.fxml"));
                fxmlLoader.setRoot(deliveryListBController);
                fxmlLoader.setController(deliveryListBController);
                new DeliveryListB(stage, fxmlLoader);
            });
        });
    }

    public void showHomePageCorriere() {
        new HomePageCorriere(this.stage, new FXMLLoader(HomePageCorriere.class.getResource("HomePageCorriere.fxml")));
    }

    public void showSignNotice(Order order) {
        SignNoticeController signNoticeController = new SignNoticeController(this, order);
        FXMLLoader fxmlLoader = new FXMLLoader(SignNotice.class.getResource("SignNotice.fxml"));
        fxmlLoader.setRoot(signNoticeController);
        fxmlLoader.setController(signNoticeController);
        new SignNotice(new Stage(), fxmlLoader, order.getFarmaciaName());
    }

    public void submitSign(Order order) {
        DBMSB.getAzienda().makeOrderDeliveredReadyToLoad(order.getOrderCode());
        DBMSB.getFarmacia().makeOrderDeliveredReadyToLoad(order.getOrderCode());
        new GenericNotice("La consegna è stata effettuata con successo.");
    }

    public void showInfoDelivery(Order order) {
        InfoDeliveryBController infoDeliveryBController = new InfoDeliveryBController(order, this);
        FXMLLoader fxmlLoader = new FXMLLoader(InfoDeliveryB.class.getResource("InfoDeliveryB.fxml"));
        fxmlLoader.setRoot(infoDeliveryBController);
        fxmlLoader.setController(infoDeliveryBController);
        new InfoDeliveryB(new Stage(), fxmlLoader);
    }

}
