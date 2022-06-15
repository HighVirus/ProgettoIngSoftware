package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.externalcomponents.DBMSB;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;

@RequiredArgsConstructor
public class CatalogoAzControl {

    private final Stage stage;
    private GestioneCatalogoController gestioneCatalogoController;
    private AggiungiFarmacoController aggiungiFarmacoController;
    @Getter
    @Setter
    private Farmaco farmacoToRemove;

    public void showCatalogoAziendale() {
        this.gestioneCatalogoController = new GestioneCatalogoController(stage, this);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneCatalogo.class.getResource("GestioneCatalogo.fxml"));
        fxmlLoader.setRoot(this.gestioneCatalogoController);
        fxmlLoader.setController(this.gestioneCatalogoController);

        new GestioneCatalogo(this.stage, fxmlLoader);
    }

    public void addProductRequest() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(AggiungiFarmaco.class.getResource("AggiungiFarmaco.fxml"));
        aggiungiFarmacoController = new AggiungiFarmacoController(this, stage);
        fxmlLoader.setRoot(aggiungiFarmacoController);
        fxmlLoader.setController(aggiungiFarmacoController);
        new AggiungiFarmaco(stage, fxmlLoader);
    }

    public void confirmAddProduct() {
        if (!this.allFieldAreFilled()) {
            new ErrorsNotice("Tutti i campi devono essere compilati.");
            return;
        }

        boolean isPrescrivibile;
        int aic;
        double costo;
        isPrescrivibile = aggiungiFarmacoController.getPrescrivibilita().getValue().equalsIgnoreCase("sì");

        try {
            aic = Integer.parseInt(aggiungiFarmacoController.getCodiceAic().getText());
            costo = Double.parseDouble(aggiungiFarmacoController.getCosto().getText());
            Farmaco farmaco = new Farmaco(String.valueOf(aic), aggiungiFarmacoController.getNomeProdotto().getText(), aggiungiFarmacoController.getPrincipioAttivo().getText(), isPrescrivibile, costo);
            DBMSB.getAzienda().getFarmacoFromCatalog(farmaco.getCodAic()).whenComplete((farm, throwable) -> {
                if (throwable != null)
                    throwable.printStackTrace();
            }).thenAccept(farm -> {
                Platform.runLater(() -> {
                    if (farm != null)
                        new ErrorsNotice("Il prodotto è gia presente all'interno del catalogo.");
                    else {
                        gestioneCatalogoController.getCatalogo().getItems().add(farmaco);
                        DBMSB.getAzienda().addFarmacoToCatalog(farmaco.getCodAic(), farmaco.getFarmacoName(),
                                farmaco.getPrincipioAttivo(), farmaco.isPrescrivibile(), farmaco.getCosto());
                        gestioneCatalogoController.getCatalogo().update();
                        aggiungiFarmacoController.getStage().close();
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
        gestioneCatalogoController.getCatalogo().getItems().remove(this.farmacoToRemove);
        DBMSB.getAzienda().removeFarmacoToCatalog(this.farmacoToRemove.getCodAic());
    }

    private boolean allFieldAreFilled() {
        if (aggiungiFarmacoController.getPrincipioAttivo().getText().length() == 0)
            return false;
        if (aggiungiFarmacoController.getNomeProdotto().getText().length() == 0)
            return false;
        if (aggiungiFarmacoController.getPrescrivibilita().getText().length() == 0)
            return false;
        if (aggiungiFarmacoController.getCosto().getText().length() == 0)
            return false;
        if (aggiungiFarmacoController.getPrescrivibilita().getValue().length() == 0)
            return false;
        return true;
    }
}
