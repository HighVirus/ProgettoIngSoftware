package me.unipa.progettoingsoftware.gestioneareafarmaceutica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale.StorageAziendaC;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.OrdersC;
import me.unipa.progettoingsoftware.utils.Homepage;

public class HomePageFarmaciaController extends Homepage {
    private final Stage stage;

    public HomePageFarmaciaController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @FXML
    public void onClickGesFarmaciButton(ActionEvent event) {
        System.out.println("porcodio");
    }

    @FXML
    public void onClickGesOrdiniButton(ActionEvent event) {
        System.out.println("allahkìakbar");
    }

}
