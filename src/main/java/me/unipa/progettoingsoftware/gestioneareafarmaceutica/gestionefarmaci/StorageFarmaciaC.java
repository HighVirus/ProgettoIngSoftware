package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.GenericNotice;
import me.unipa.progettoingsoftware.utils.OrderStatus;

import java.sql.Date;
import java.util.*;

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
        FXMLLoader fxmlLoader = new FXMLLoader(CaricaProductsForm.class.getResource("CaricaProductsForm.fxml"));
        fxmlLoader.setRoot(caricaProductsFormController);
        fxmlLoader.setController(caricaProductsFormController);
        new CaricaProductsForm(new Stage(), fxmlLoader);
    }

    public void showCaricoOrderListB() {
        String piva = User.getUser().getFarmaciaPiva();
        DBMSB.getFarmacia().getOrderList(piva, OrderStatus.CONSEGNATO_DA_CARICARE).whenComplete((orderList, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(orders -> {
            Platform.runLater(() -> {
                this.caricoOrderListBController = new CaricoOrderListBController(orders, this, new Stage());
                FXMLLoader fxmlLoader = new FXMLLoader(CaricoOrderListB.class.getResource("CaricoOrderListB.fxml"));
                fxmlLoader.setRoot(caricoOrderListBController);
                fxmlLoader.setController(caricoOrderListBController);
                new CaricoOrderListB(new Stage(), fxmlLoader);
            });
        });
    }

    public void showStorageFarmaciaB() {
        DBMSB.getFarmacia().getFarmacoListFromStorage(User.getUser().getFarmaciaPiva()).whenComplete((farmacos, throwable) -> {
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

    public void modificaCostoFarmaco(Farmaco farmaco, String textField) {
        if (isValidCostoField(textField)) {
            double costo = Double.parseDouble(textField);
            farmaco.setCosto(costo);
            for (Farmaco f1 : caricaProductsFormController.getCaricoList().getItems()) {
                if (f1.getCodAic().equalsIgnoreCase(farmaco.getCodAic()) && f1.getLotto().equalsIgnoreCase(farmaco.getLotto())) {
                    f1.setCosto(costo);
                    break;
                }
            }
            caricaProductsFormController.getCaricoList().update();
        } else {
            new ErrorsNotice("Valore non valido");
        }
    }

    private boolean isValidCostoField(String textField) {
        if (textField.length() == 0)
            return false;
        try {
            double value = Double.parseDouble(textField);
            return value > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
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
        if (this.caricaProductsFormController.checkList() == 0) {
            new ErrorsNotice("La lista dei farmaci da caricare è vuota.");
            return;
        }

        List<String> orderCodeSet = new ArrayList<>();
        for (Farmaco farmaco : this.caricaProductsFormController.getCaricoList().getItems()) {
            if (!orderCodeSet.contains(farmaco.getOrderCode()))
                orderCodeSet.add(farmaco.getOrderCode());
        }

        DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(farmaciaInfo -> {
            for (String orderCode : orderCodeSet){
                List<Farmaco> farmacoList = DBMSB.getAzienda().getFarmaciFromOrder(orderCode).join();
                if (farmacoList.isEmpty()) {
                    new ErrorsNotice("Il codice d'ordine non esiste.");
                    return;
                }

                for (Farmaco farmaco : farmacoList) {
                    boolean found = false;
                    for (Farmaco f : this.caricaProductsFormController.getCaricoList().getItems()) {
                        if (f.getCodAic().equals(farmaco.getCodAic())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        DBMSB.getAzienda().sendAlert(farmaciaInfo.get(0));
                        break;
                    }
                }
            }
        });

        DBMSB.getFarmacia().addFarmacoListToStorage(User.getUser().getFarmaciaPiva(), this.caricaProductsFormController.getCaricoList().getItems());

        for (String orderCode : orderCodeSet){
            DBMSB.getFarmacia().makeDeliveryCompleted(orderCode);
            DBMSB.getAzienda().makeDeliveryCompleted(orderCode);
        }

        DBMSB.getFarmacia().getFarmaciAlreadyOrdered(User.getUser().getFarmaciaPiva()).thenAccept(farmacoList -> {
            for (String codAic : farmacoList) {
                DBMSB.getFarmacia().removeFarmacoFromAlreadyOrdered(User.getUser().getFarmaciaPiva(), codAic);
            }
        });

        new GenericNotice("Prodotti caricati con successo.");

    }

    public void addFarmaciFromOrder(Order order) {
        this.caricaProductsFormController.getCaricoList().getItems().addAll(order.getFarmacoList());
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
