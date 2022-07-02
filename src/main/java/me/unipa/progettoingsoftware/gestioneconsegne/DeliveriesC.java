package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.InfoOrderB;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.InfoOrderBController;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.OrderListBController;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.utils.GenericNotice;

@RequiredArgsConstructor
public class DeliveriesC {
    private final Stage stage;
    private DeliveryListBController deliveryListBController;
    private final DeliveriesC deliveriesC;
    private Order order;
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

    public void showSignNotice() {
        SignNoticeController signNoticeController = new SignNoticeController(deliveriesC);
        FXMLLoader fxmlLoader = new FXMLLoader(SignNotice.class.getResource("SignNotice.fxml"));
        fxmlLoader.setRoot(signNoticeController);
        fxmlLoader.setController(signNoticeController);
        new SignNotice(new Stage(), fxmlLoader);
    }

    public void submitSign() {

        DBMSB.getAzienda().makeOrderDeliveredReadyToLoad(this.order.getOrderCode());
        new GenericNotice("La consegna è stata effettuata con successo.");

    }

    public void showInfoDelivery() {
        InfoDeliveryBController infoDeliveryBController = new InfoDeliveryBController();
        FXMLLoader fxmlLoader = new FXMLLoader(InfoDeliveryB.class.getResource("InfoDeliveryB.fxml"));
        fxmlLoader.setRoot(infoDeliveryBController);
        fxmlLoader.setController(infoDeliveryBController);
        new InfoDeliveryB(new Stage(), fxmlLoader);
    }

}
