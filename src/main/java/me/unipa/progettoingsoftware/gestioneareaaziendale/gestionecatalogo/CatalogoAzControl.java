package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.GenericNotice;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;

@RequiredArgsConstructor
public class CatalogoAzControl {

    private final Stage stage;
    private CatalogoAziendaController catalogoAziendaController;
    private AddProdFormController addProdFormController;
    @Getter
    @Setter
    private Farmaco farmacoToRemove;

    public void showCatalogoAziendale() {
        DBMSB.getAzienda().getFarmaciCatalogList().whenComplete((farmacoList, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(farmacoList -> {
            Platform.runLater(() -> {
                this.catalogoAziendaController = new CatalogoAziendaController(stage, this, farmacoList);
                FXMLLoader fxmlLoader = new FXMLLoader(CatalogoAzienda.class.getResource("GestioneCatalogo.fxml"));
                fxmlLoader.setRoot(this.catalogoAziendaController);
                fxmlLoader.setController(this.catalogoAziendaController);

                new CatalogoAzienda(this.stage, fxmlLoader);
            });
        });

    }

    public void addProductRequest() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(AddProdForm.class.getResource("AggiungiFarmaco.fxml"));
        addProdFormController = new AddProdFormController(this, stage);
        fxmlLoader.setRoot(addProdFormController);
        fxmlLoader.setController(addProdFormController);
        new AddProdForm(stage, fxmlLoader);
    }

    public void confirmAddProduct() {
        if (!this.allFieldAreFilled()) {
            new ErrorsNotice("Tutti i campi devono essere compilati.");
            return;
        }

        boolean isPrescrivibile;
        int aic;
        double costo;
        isPrescrivibile = addProdFormController.getPrescrivibilita().getValue().equalsIgnoreCase("sì");

        try {
            aic = Integer.parseInt(addProdFormController.getCodiceAic().getText());
            costo = Double.parseDouble(addProdFormController.getCosto().getText());
            Farmaco farmaco = new Farmaco(String.valueOf(aic), addProdFormController.getNomeProdotto().getText(), addProdFormController.getPrincipioAttivo().getText(), isPrescrivibile, costo);
            DBMSB.getAzienda().getFarmacoFromCatalog(farmaco.getCodAic()).whenComplete((farm, throwable) -> {
                if (throwable != null)
                    throwable.printStackTrace();
            }).thenAccept(farm -> {
                Platform.runLater(() -> {
                    if (farm != null)
                        new ErrorsNotice("Il prodotto è gia presente all'interno del catalogo.");
                    else {
                        catalogoAziendaController.getCatalogo().getItems().add(farmaco);
                        catalogoAziendaController.getCatalogo().update();
                        DBMSB.getAzienda().addFarmacoToCatalog(farmaco.getCodAic(), farmaco.getFarmacoName(),
                                farmaco.getPrincipioAttivo(), farmaco.isPrescrivibile(), farmaco.getCosto());
                        addProdFormController.getStage().close();
                    }
                });
            });
        } catch (NumberFormatException ex) {
            new ErrorsNotice("Hai inserito un valore non valido, ricontrolla.");
        }
    }

    public void showConfirmRemNotice(){
        ConfirmRemNoticeController confirmRemNoticeController = new ConfirmRemNoticeController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(ConfirmRemNotice.class.getResource("ConfirmRemNotice.fxml"));
        fxmlLoader.setRoot(confirmRemNoticeController);
        fxmlLoader.setController(confirmRemNoticeController);
        new ConfirmRemNotice(new Stage(), fxmlLoader);
    }

    public void confirmRemProduct(){
        catalogoAziendaController.getCatalogo().getItems().remove(this.farmacoToRemove);
        DBMSB.getAzienda().removeFarmacoToCatalog(this.farmacoToRemove.getCodAic());
        new GenericNotice("Prodotto rimosso con successo dal catalogo.");
    }

    private boolean allFieldAreFilled() {
        if (addProdFormController.getPrincipioAttivo().getText().length() == 0)
            return false;
        if (addProdFormController.getNomeProdotto().getText().length() == 0)
            return false;
        if (addProdFormController.getPrescrivibilita().getText().length() == 0)
            return false;
        if (addProdFormController.getCosto().getText().length() == 0)
            return false;
        if (addProdFormController.getPrescrivibilita().getValue().length() == 0)
            return false;
        return true;
    }
}
