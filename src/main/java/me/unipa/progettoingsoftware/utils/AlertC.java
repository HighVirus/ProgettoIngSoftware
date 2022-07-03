package me.unipa.progettoingsoftware.utils;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.AlertE;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

@RequiredArgsConstructor
public class AlertC {
    private final Stage stage;
    private AlertInfoCaricoBController alertInfoCaricoBController;
    private AlertInfoQuantitaBController alertInfoQuantitaBController;

    private AlertReportBController alertReportBController;

    private AlertListBController alertListBController;

    public void showAlertReport() {
        new AlertReportB(this.stage, new FXMLLoader(AlertReportB.class.getResource("AlertReportB.fxml")));
        DBMSB.getAzienda().getAlertsAzienda().thenAccept(alertEList -> {
            if (alertEList.isEmpty()) {
                new GenericNotice("Non ci sono alert da visualizzare");
            } else {
                alertReportBController = new AlertReportBController(this.stage, this, alertEList);
                FXMLLoader fxmlLoader = new FXMLLoader(AlertReportB.class.getResource("AlertReportB.fxml"));
                fxmlLoader.setRoot(alertReportBController);
                fxmlLoader.setController(alertReportBController);
                new AlertReportB(new Stage(), fxmlLoader);
            }
        });
    }


    public void showAlertList() {
        DBMSB.getFarmacia().getAlertList(User.getUser().getFarmaciaPiva()).thenAccept(alertEList -> {
            if (alertEList.isEmpty()) {
                new GenericNotice("Non ci sono alert da visualizzare");
            } else {
                alertListBController = new AlertListBController(this.stage, this, alertEList);
                FXMLLoader fxmlLoader = new FXMLLoader(AlertListB.class.getResource("AlertListB.fxml"));
                fxmlLoader.setRoot(alertListBController);
                fxmlLoader.setController(alertListBController);
                new AlertListB(new Stage(), fxmlLoader);
            }
        });
    }

    public void showViewAlertFarmacia(AlertE alertE) {
        if (alertE.getAlertType() == AlertType.FARMACIA_QUANTITY) {

            alertInfoQuantitaBController = new AlertInfoQuantitaBController(this.stage, this, alertE.getFarmacoQuantityList());
            FXMLLoader fxmlLoader = new FXMLLoader(AlertInfoQuantitaB.class.getResource("AlertInfoQuantitaB.fxml"));
            fxmlLoader.setRoot(alertInfoQuantitaBController);
            fxmlLoader.setController(alertInfoQuantitaBController);
            new AlertInfoQuantitaB(new Stage(), fxmlLoader);
        } else if (alertE.getAlertType() == AlertType.FARMACIA_CARICO) {
            alertInfoCaricoBController = new AlertInfoCaricoBController(this.stage, this, alertE.getCaricoOrderList());
            FXMLLoader fxmlLoader = new FXMLLoader(AlertInfoCaricoB.class.getResource("AlertInfoCaricoB.fxml"));
            fxmlLoader.setRoot(alertInfoCaricoBController);
            fxmlLoader.setController(alertInfoCaricoBController);
            new AlertInfoCaricoB(new Stage(), fxmlLoader);
        }
    }


    public void clickConfirmNotAll() {
        new StorageFarmaciaC(this.stage).showCaricaMerci();
    }

    public void clickConfirmAll() {
        //DBMSB.getFarmacia().getFarmaciFromOrder(order.getOrderCode());
        //addFarmacoToStorage
        //makeDeliveryCompleted azienda e farmacia
    }

    public void clickAlreadyOrdered() {
        //DBMSB.getFarmacia().addFarmaciToOrdered(this, alertInfoQuantitaBController.getFarmacoTable().getItems());
    }
}
