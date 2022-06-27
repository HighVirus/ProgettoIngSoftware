package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.utils.Homepage;

@RequiredArgsConstructor
public class HomePageCorriereController extends AnchorPane {
    private final Stage stage;

    @FXML
    public void onClickViewDeliveriesButton(ActionEvent event) {
        new DeliveriesC(stage).showDeliveryList();
    }

}
