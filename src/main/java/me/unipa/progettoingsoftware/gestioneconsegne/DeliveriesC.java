package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.utils.GenericNotice;

@RequiredArgsConstructor
public class DeliveriesC {
    private final Stage stage;
    private DeliveryListBController deliveryListBController;

    public void showDeliveryList() {
        DBMSB.getAzienda().getDeliveryList();
        FXMLLoader fxmlLoader = new FXMLLoader(DeliveryListB.class.getResource("ViewDeliveries.fxml"));
        fxmlLoader.setRoot(deliveryListBController);
        fxmlLoader.setController(deliveryListBController);
        new DeliveryListB(stage, fxmlLoader);
    }

    public void clickConfirmDeliveryButton() {

    }

    public void showSignNotice() {

    }

    public void submitSign() {
        new GenericNotice("Consegna confermta");
    }


    public void showInfoDelivery() {

    }

    public void showConfirmDelivery() {

    }
}
