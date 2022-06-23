package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;

@RequiredArgsConstructor
public class StorageFarmaciaC {
    private final Stage stage;

    public void showGestioneFarmaciB() {
        GestioneFarmaciBController gestioneFarmaciBController = new GestioneFarmaciBController(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneFarmaciB.class.getResource("GestioneFarmaciB.fxml"));
        fxmlLoader.setRoot(gestioneFarmaciBController);
        fxmlLoader.setController(gestioneFarmaciBController);
        new GestioneFarmaciB(stage, fxmlLoader);
    }

    public void showStorageFarmaciaB() {
        DBMSB.getFarmacia().getFarmaciListFromStorage().whenComplete((farmacos, throwable) -> {
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

}
