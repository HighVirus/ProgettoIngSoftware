package me.unipa.progettoingsoftware.gestioneareafarmaceutica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.OrdersC;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.OrdersFarC;
import me.unipa.progettoingsoftware.utils.Homepage;

public class HomePageFarmaciaController extends Homepage {
    private final Stage stage;

    public HomePageFarmaciaController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @FXML
    public void onClickGesFarmaciButton(ActionEvent event) {
        new StorageFarmaciaC(stage).showGestioneFarmaciB();
    }

    @FXML
    public void onClickGesOrdiniButton(ActionEvent event) {
        new OrdersFarC(stage).showGestioneOrdiniB();
    }

}
