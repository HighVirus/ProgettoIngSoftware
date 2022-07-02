package me.unipa.progettoingsoftware.utils;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.NoAvailabilityNotice;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.PrenFarmForm;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.PrenFarmFormController;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.AlertE;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

import java.sql.Date;

@RequiredArgsConstructor
public class AlertC {
    private final Stage stage;
    private AlertInfoCaricoBController alertInfoCaricoBController;
    private AlertInfoQuantitaBController alertInfoQuantitaBController;

    private AlertReportBController alertReportController;

    private AlertListBController alertListBController;

    public void showAlertReport() { //lato azienda, sistemare la control
        new AlertReportB(this.stage, new FXMLLoader(AlertReportB.class.getResource( "AlertReportB.fxml")));
    }

    public void showAlertList() {  //lato farmacia, sistemare la control
        User.getUser().getFarmaciaPiva().getAlertList().thenAccept(AlertE -> {
            if (alertListBController.getAlertList().isEmpty()) {
                new ErrorsNotice("Non ci sono alert da visualizzare");
            }
            else {
                AlertListBController prenFarmFormController = new AlertListBController(farmaco, this, new Date(calendar.getTimeInMillis()));
                FXMLLoader fxmlLoader = new FXMLLoader(AlertListB.class.getResource("AlertListB.fxml"));
                fxmlLoader.setRoot(prenFarmFormController);
                fxmlLoader.setController(prenFarmFormController);
                new AlertListB(new Stage(), fxmlLoader);
            }
        });
    }

    public void showViewAlertFarmacia(AlertE alertE) { //quantita DA SISTEMARE LA CONTROL MANUALE
        if (alertE.getAlertType() == AlertType.FARMACIA_QUANTITY) {
            new AlertInfoQuantitaB(new Stage(), new FXMLLoader(AlertInfoQuantitaB.class.getResource("AlertInfoQuantitaB.fxml")));
        } else if (alertE.getAlertType() == AlertType.FARMACIA_CARICO) {
            new AlertInfoCaricoB(new Stage(), new FXMLLoader(AlertInfoCaricoB.class.getResource("AlertInfoCaricoB.fxml")));
        }
    }


    public void showCaricaMerci() {
        new StorageFarmaciaC(this.stage).showCaricaMerci();
    }

    public void clickConfirmAll() { //da aggiungere l'aggiunta in db dei farmaci
        this.alertInfoCaricoBController.getOrderTable().getItems();
    }

    public void clickAlreadyOrdered() {
        DBMSB.getFarmacia().addFarmaciToOrdered(this.alertInfoQuantitaBController.getFarmacoTable().getItems());
    }
}
