package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNotice;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.CarrelloE;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

import java.sql.Date;

@RequiredArgsConstructor
public class DeliveriesC {
    private final Stage stage;
    private ViewDeliveriesController viewDeliveriesController;

    public void showDeliveryList() {
        DBMSB.getAzienda().getDeliveryList();
        FXMLLoader fxmlLoader = new FXMLLoader(ViewDeliveries.class.getResource("ViewDeliveries.fxml"));
        fxmlLoader.setRoot(viewDeliveriesController);
        fxmlLoader.setController(viewDeliveriesController);
        new ViewDeliveries(stage, fxmlLoader);
    }


    public void showInfoDelivery() {

    }

    public void showConfirmDelivery() {

    }
 }
