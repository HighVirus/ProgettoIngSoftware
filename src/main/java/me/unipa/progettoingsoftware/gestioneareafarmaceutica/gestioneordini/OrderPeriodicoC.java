package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.GenericNotice;

@RequiredArgsConstructor
public class OrderPeriodicoC {

    private final Stage stage;
    private ViewOrdinePeriodicoBController viewOrdinePeriodicoBController;
    private UnitOrderPerReportController unitOrderPerReportController;

    public void showViewOrdinePeriodicoB(){
        DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(strings -> {
            String piva = strings.get(0);
            DBMSB.getAzienda().getFarmaciBanco(piva).thenAccept(farmacoList-> {
                Platform.runLater(() -> {
                    viewOrdinePeriodicoBController = new ViewOrdinePeriodicoBController(stage, this, farmacoList);
                    FXMLLoader fxmlLoader = new FXMLLoader(ViewOrdini.class.getResource("ViewOrdinePeriodicoB.fxml"));
                    fxmlLoader.setRoot(viewOrdinePeriodicoBController);
                    fxmlLoader.setController(viewOrdinePeriodicoBController);
                    new ViewOrdinePeriodicoB(stage, fxmlLoader);
                });
            });
        });

    }

    public void showUnitOrderPerReport(Farmaco farmaco){
        unitOrderPerReportController = new UnitOrderPerReportController(farmaco, this);
        FXMLLoader fxmlLoader = new FXMLLoader(ViewOrdini.class.getResource("UnitOrderPerReport.fxml"));
        fxmlLoader.setRoot(unitOrderPerReportController);
        fxmlLoader.setController(unitOrderPerReportController);
        new UnitOrderPerReport(stage, fxmlLoader);
    }

    public void clickConfirmModifyOrderPeriodic(Farmaco farmaco) {
        DBMSB.getAzienda().updateUnitaPeriodicOrder(farmaco);
        DBMSB.getFarmacia().updateUnitaPeriodicOrder(farmaco);
        new GenericNotice("Ordine periodico modificato con successo");

    }
    public void showHomePageFarmacia() {
        new HomePageFarmacia(this.stage, new FXMLLoader(HomePageFarmacia.class.getResource("HomePageFarmacia.fxml")));
    }
}


