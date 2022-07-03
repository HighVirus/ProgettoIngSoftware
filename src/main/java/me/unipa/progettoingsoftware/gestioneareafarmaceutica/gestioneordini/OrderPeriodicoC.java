package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.GenericNotice;

@RequiredArgsConstructor
public class OrderPeriodicoC {

    private final Stage stage;
    private ViewOrdinePeriodicoBController viewOrdinePeriodicoBController;
    private UnitOrderPerReportController unitOrderPerReportController;

    public void showViewOrdinePeriodicoB() {
        String piva = User.getUser().getFarmaciaPiva();
        DBMSB.getAzienda().getFarmacoUnitaPeriodic(piva).thenAccept(periodicOrders -> {
            Platform.runLater(() -> {
                viewOrdinePeriodicoBController = new ViewOrdinePeriodicoBController(stage, this, periodicOrders);
                FXMLLoader fxmlLoader = new FXMLLoader(OrderList.class.getResource("ViewOrdinePeriodicoB.fxml"));
                fxmlLoader.setRoot(viewOrdinePeriodicoBController);
                fxmlLoader.setController(viewOrdinePeriodicoBController);
                new ViewOrdinePeriodicoB(stage, fxmlLoader);
            });
        });

    }

    public void showUnitOrderPerReport(PeriodicOrder periodicOrder) {
        Stage stage = new Stage();
        unitOrderPerReportController = new UnitOrderPerReportController(periodicOrder, this, stage);
        FXMLLoader fxmlLoader = new FXMLLoader(OrderList.class.getResource("UnitOrderPerReport.fxml"));
        fxmlLoader.setRoot(unitOrderPerReportController);
        fxmlLoader.setController(unitOrderPerReportController);
        new UnitOrderPerReport(stage, fxmlLoader);
    }

    public void clickConfirmModifyOrderPeriodic(PeriodicOrder periodicOrder) {
        if (!isValidUnitaField(unitOrderPerReportController.getUnita().getText())) {
            new ErrorsNotice("Valore non valido");
            return;
        }
        DBMSB.getAzienda().updateUnitaPeriodicOrder(periodicOrder);
        new GenericNotice("Ordine periodico modificato con successo");
        unitOrderPerReportController.getStage().close();

    }

    public void showHomePageFarmacia() {
        new HomePageFarmacia(this.stage, new FXMLLoader(HomePageFarmacia.class.getResource("HomePageFarmacia.fxml")));
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


