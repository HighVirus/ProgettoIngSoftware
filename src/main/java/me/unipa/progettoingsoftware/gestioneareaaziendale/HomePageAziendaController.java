package me.unipa.progettoingsoftware.gestioneareaaziendale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale.StorageAziendaC;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.OrdersC;
import me.unipa.progettoingsoftware.utils.Homepage;

public class HomePageAziendaController extends Homepage {
    private final Stage stage;

    public HomePageAziendaController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @FXML
    public void onClickCatalogoButton(ActionEvent event) {
        new CatalogoAzControl(stage).showCatalogoAziendale();
    }

    public void onClickStorAzButton(ActionEvent event) {
        new StorageAziendaC(stage).showAziendaStorage();
    }

    public void onClickOrdiniButton(ActionEvent event) {
        new OrdersC(stage).showOrderList();
    }
}
