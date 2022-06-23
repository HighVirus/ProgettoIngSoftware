package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNotice;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNoticeController;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;

@RequiredArgsConstructor
public class SellFarmacoC {
    private final Stage stage;
    private Stage sellStage;
    private SellWindowBController sellWindowBController;

    public void showGestioneFarmaciB() {
        GestioneFarmaciBController gestioneFarmaciBController = new GestioneFarmaciBController(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneFarmaciB.class.getResource("GestioneFarmaciB.fxml"));
        fxmlLoader.setRoot(gestioneFarmaciBController);
        fxmlLoader.setController(gestioneFarmaciBController);
        new GestioneFarmaciB(stage, fxmlLoader);
    }

    public void showSellWindowB() {
        DBMSB.getFarmacia().getFarmaciListFromStorage().whenComplete((farmacos, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(farmacos -> {
            Platform.runLater(() -> {
                this.sellStage = new Stage();
                this.sellWindowBController = new SellWindowBController(farmacos, this, stage);
                FXMLLoader fxmlLoader = new FXMLLoader(SellWindowB.class.getResource("SellWindowB.fxml"));
                fxmlLoader.setRoot(sellWindowBController);
                fxmlLoader.setController(sellWindowBController);
                new SellWindowB(sellStage, fxmlLoader);
            });
        });
    }

    private void showConfirmSellB(double total){
        ConfirmSellBController confirmSellBController = new ConfirmSellBController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(ConfirmSellB.class.getResource("ConfirmSellB.fxml"));
        fxmlLoader.setRoot(confirmSellBController);
        fxmlLoader.setController(confirmSellBController);
        new ConfirmSellB(fxmlLoader, total);
    }

    public void addProductToSellList(Farmaco farmaco, String textField) {
        if (isValidUnitaField(textField)) {
            int unita = Integer.parseInt(textField);
            farmaco.setUnita(unita);
            for (Farmaco f1 : sellWindowBController.getCarrelloTable().getItems()) {
                if (f1.getCodAic().equalsIgnoreCase(farmaco.getCodAic())) {
                    new ErrorsNotice("Prodotto gia aggiunto nel carrello");
                    return;
                }
            }
            DBMSB.getAzienda().checkFarmacoAvailability(farmaco.getCodAic(), unita).thenAccept(aBoolean -> {
                Platform.runLater(() -> {
                    if (aBoolean){
                        double costoSingolo = farmaco.getCosto() * farmaco.getUnita();
                        farmaco.setCosto(costoSingolo);
                        sellWindowBController.getCarrelloTable().getItems().add(farmaco);

                    }
                    else
                        new ErrorsNotice("Quantità non disponibile.");
                });
            });
        } else {
            new ErrorsNotice("Hai inserito un valore non valido, ricontrolla.");
        }
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

    public void clickConfirmButton() {
        if (sellWindowBController.getCarrelloTable().getItems().isEmpty()) {
            new ErrorsNotice("Non ci sono farmaci nel carrello.");
            return;
        }

        double total = this.calcolaTotale();
        this.showConfirmSellB(total);

    }

    public void emettiScontrino() {
        System.out.println("emesso");
        this.sellStage.close();
    }

    private double calcolaTotale() {
        double total = 0D;
        for (Farmaco f : sellWindowBController.getCarrelloTable().getItems()){
            total += f.getCosto();
        }
        return total;
    }
}
