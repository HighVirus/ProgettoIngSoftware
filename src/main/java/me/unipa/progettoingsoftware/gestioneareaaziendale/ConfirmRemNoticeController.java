package me.unipa.progettoingsoftware.gestioneareaaziendale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale.StorageAziendaC;

public class ConfirmRemNoticeController extends AnchorPane {
    private CatalogoAzControl catalogoAzControl;
    private StorageAziendaC storageAziendaC;

    public ConfirmRemNoticeController(CatalogoAzControl catalogoAzControl) {
        this.catalogoAzControl = catalogoAzControl;
    }

    public ConfirmRemNoticeController(StorageAziendaC storageAziendaC) {
        this.storageAziendaC = storageAziendaC;
    }

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (catalogoAzControl != null)
            catalogoAzControl.confirmRemProduct();
        if (storageAziendaC != null)
            storageAziendaC.confirmRemProduct();
        stage.close();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (catalogoAzControl != null)
            catalogoAzControl.setFarmacoToRemove(null);
        if (storageAziendaC != null)
            storageAziendaC.setFarmacoToRemove(null);
        stage.close();
    }
}
