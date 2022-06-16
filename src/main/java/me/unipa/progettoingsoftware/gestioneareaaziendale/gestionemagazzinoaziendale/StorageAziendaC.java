package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.externalcomponents.DBMSB;

@RequiredArgsConstructor
public class StorageAziendaC {
    private final Stage stage;
    private StorageAziendaBController storageAziendaBController;

    public void showAziendaStorage() {
        DBMSB.getAzienda().getFarmaciListFromAziendaStorage().whenComplete((farmacoList, throwable) -> {
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
}
