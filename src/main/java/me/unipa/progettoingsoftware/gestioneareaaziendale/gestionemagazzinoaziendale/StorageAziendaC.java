package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNotice;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNoticeController;
import me.unipa.progettoingsoftware.utils.GenericNotice;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;

@RequiredArgsConstructor
public class StorageAziendaC {
    private final Stage stage;
    private StorageAziendaBController storageAziendaBController;
    @Getter
    @Setter
    private Farmaco farmacoToRemove;

    public void showAziendaStorage() {
        DBMSB.getAzienda().getFarmaciListFromStorage().whenComplete((farmacoList, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(farmacoList -> {
            Platform.runLater(() -> {
                this.storageAziendaBController = new StorageAziendaBController(stage, this, farmacoList);
                FXMLLoader fxmlLoader = new FXMLLoader(StorageAziendaB.class.getResource("StorageAziendaB.fxml"));
                fxmlLoader.setRoot(this.storageAziendaBController);
                fxmlLoader.setController(this.storageAziendaBController);

                new StorageAziendaB(this.stage, fxmlLoader);
            });
        });
    }

    public void confirmRemProduct(){
        storageAziendaBController.getStorage().getItems().remove(this.farmacoToRemove);
        DBMSB.getAzienda().removeFarmacoFromStorage(this.farmacoToRemove.getCodAic(), this.farmacoToRemove.getLotto());
        new GenericNotice("Prodotto rimosso con successo dal magazzino.");
    }
}
