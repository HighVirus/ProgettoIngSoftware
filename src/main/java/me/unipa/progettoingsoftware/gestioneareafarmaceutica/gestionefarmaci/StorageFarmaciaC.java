package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.AlertType;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.GenericNotice;

import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@RequiredArgsConstructor
public class StorageFarmaciaC {
    private final Stage stage;
    private CaricaProductsFormController caricaProductsFormController;
    private CaricoOrderListBController caricoOrderListBController;

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

    public void showCaricoOrderListB() {
        DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(strings -> {
            String piva = strings.get(0);
            DBMSB.getAzienda().getOrderList(piva).thenAccept(orders -> {
                Platform.runLater(() -> {
                    this.caricoOrderListBController = new CaricoOrderListBController(orders, this, new Stage());
                    FXMLLoader fxmlLoader = new FXMLLoader(CaricoOrderListB.class.getResource("CaricoOrderListB.fxml"));
                    fxmlLoader.setRoot(caricaProductsFormController);
                    fxmlLoader.setController(caricaProductsFormController);
                    new CaricoOrderListB(new Stage(), fxmlLoader);
                });
            });
        });

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

    public void removeProductFromCaricoList(Farmaco farmaco) {
        this.caricaProductsFormController.getCaricoList().getItems().remove(farmaco);
    }

    public void clickConfirmButton() {
        Set<String> orderCodeSet = new HashSet<>();
        for (Farmaco farmaco : this.caricaProductsFormController.getCaricoList().getItems())
            orderCodeSet.add(farmaco.getFarmacoName());

        Iterator<String> orderCodeIterator = orderCodeSet.iterator();
        while (orderCodeIterator.hasNext()) {
            DBMSB.getAzienda().getFarmaciFromOrder(orderCodeIterator.next());   //continuare
            if (this.caricaProductsFormController.checkList() == 0) {
                new ErrorsNotice("La lista dei farmaci da caricare è vuota.");
                return;
            }
            //continuare con la lista ottenuta
            String farmaciaName = DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).join().get(1);
            DBMSB.getAzienda().sendAlert(AlertType.AZIENDA, farmaciaName);
        }

        DBMSB.getFarmacia().addFarmacoListToStorage(this.caricaProductsFormController.getCaricoList().getItems());

        while (orderCodeIterator.hasNext()){
            DBMSB.getFarmacia().makeDeliveryCompleted(orderCodeIterator.next());
            DBMSB.getAzienda().makeDeliveryCompleted(orderCodeIterator.next());
        }

        DBMSB.getFarmacia().getFarmaciAlreadyOrdered().thenAccept(farmacoList -> {
            for (Farmaco farmaco : farmacoList){
                DBMSB.getFarmacia().removeFarmacoFromAlreadyOrdered(farmaco.getCodAic());
            }
        });

        new GenericNotice("messaggio di conferma");

    }

    public void addFarmaciFromOrder(Order order){

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
