package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;

import java.sql.Date;

@RequiredArgsConstructor
public class StorageFarmaciaC {
    private final Stage stage;
    private CaricaProductsFormController caricaProductsFormController;

    public void showGestioneFarmaciB() {
        GestioneFarmaciBController gestioneFarmaciBController = new GestioneFarmaciBController(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneFarmaciB.class.getResource("GestioneFarmaciB.fxml"));
        fxmlLoader.setRoot(gestioneFarmaciBController);
        fxmlLoader.setController(gestioneFarmaciBController);
        new GestioneFarmaciB(stage, fxmlLoader);
    }

    public void showCaricaMerci() {
        this.caricaProductsFormController = new CaricaProductsFormController(stage, this);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneFarmaciB.class.getResource("CaricaProductsForm.fxml"));
        fxmlLoader.setRoot(caricaProductsFormController);
        fxmlLoader.setController(caricaProductsFormController);
        new CaricaProductsForm(new Stage(), fxmlLoader);
    }

    public void showStorageFarmaciaB() {
        DBMSB.getFarmacia().getFarmacoListFromStorage().whenComplete((farmacos, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(farmacos -> {
            Platform.runLater(() -> {
                StorageFarmaciaBController storageFarmaciaBController = new StorageFarmaciaBController(stage, this, farmacos);
                FXMLLoader fxmlLoader = new FXMLLoader(StorageFarmaciaB.class.getResource("StorageFarmaciB.fxml"));
                fxmlLoader.setRoot(storageFarmaciaBController);
                fxmlLoader.setController(storageFarmaciaBController);
                new StorageFarmaciaB(stage, fxmlLoader);
            });
        });
    }

    public void addProductToCaricoList() {
        if (!allFieldAreFilled()) {
            new ErrorsNotice("Tutti i campi devono essere compilati.");
            return;
        }
        try {
            String orderCode = this.caricaProductsFormController.getOrderCode().getText();
            String principio = this.caricaProductsFormController.getPrincipioAttivo().getText();
            String aicCode = this.caricaProductsFormController.getAicCode().getText();
            boolean isPrescrivibile = this.caricaProductsFormController.getPrescrivibilita().getValue().equalsIgnoreCase("sì");
            double costo = Double.parseDouble(this.caricaProductsFormController.getCosto().getText());
            String lotto = this.caricaProductsFormController.getLottoCode().getText();
            String farmacoName = this.caricaProductsFormController.getFarmacoName().getText();
            int unita = Integer.parseInt(this.caricaProductsFormController.getUnita().getText());
            Date expireDate = Date.valueOf(this.caricaProductsFormController.getExpireDate().getCurrentDate());

            this.caricaProductsFormController.getCaricoList().getItems().add(new Farmaco(orderCode, aicCode, lotto, farmacoName, principio, isPrescrivibile, expireDate, costo, unita));
        } catch (NumberFormatException ex) {
            new ErrorsNotice("Hai inserito un valore non valido, ricontrolla.");
        }
    }

    public void clickConfirmButton() {
        if (this.caricaProductsFormController.checkList() == 0) {
            new ErrorsNotice("La lista dei farmaci da caricare è vuota.");
            return;
        }
    }

    private boolean allFieldAreFilled() {
        if (this.caricaProductsFormController.getOrderCode().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getPrincipioAttivo().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getAicCode().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getCosto().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getLottoCode().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getFarmacoName().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getUnita().getText().length() == 0)
            return false;
        if (this.caricaProductsFormController.getExpireDate().getCurrentDate() == null)
            return false;
        if (this.caricaProductsFormController.getPrescrivibilita().getText().length() == 0)
            return false;
        System.out.println("data: " + this.caricaProductsFormController.getExpireDate().getCurrentDate());
        return true;
    }

}
