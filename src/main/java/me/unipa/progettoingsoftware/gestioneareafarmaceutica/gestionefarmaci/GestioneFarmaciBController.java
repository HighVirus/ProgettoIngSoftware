package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.utils.Homepage;

public class GestioneFarmaciBController extends Homepage {
    private final Stage stage;

    public GestioneFarmaciBController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @FXML
    public void onClickSellButton(ActionEvent event) {
        new SellFarmacoC(stage).showSellWindowB();
    }

    @FXML
    public void onClickCaricaProductsButton(ActionEvent event) {
        new StorageFarmaciaC(stage).showCaricaMerci();
    }

    @FXML
    public void onClickViewMagFarButton(ActionEvent event) {
        new StorageFarmaciaC(stage).showStorageFarmaciaB();
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        new HomePageFarmacia(this.stage, new FXMLLoader(HomePageFarmacia.class.getResource("HomePageFarmacia.fxml")));
    }


}
