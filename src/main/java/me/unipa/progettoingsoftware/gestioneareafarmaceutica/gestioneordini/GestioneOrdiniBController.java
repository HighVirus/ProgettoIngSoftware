package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.SellFarmacoC;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.utils.Homepage;

public class GestioneOrdiniBController extends Homepage {
    private final Stage stage;

    public GestioneOrdiniBController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @FXML
    public void onClickCarrelloButton(ActionEvent event) {
        new OrdersFarC(stage).showViewCarrello();
    }

    @FXML
    public void onClickOrdFarmaciButton(ActionEvent event) {
        new OrdersFarC(stage).showOrderFarmaWindowB();
    }

    @FXML
    public void onClickViewOrderButtonB(ActionEvent event) {
        new OrdersFarC(stage).showOrderList();
    }

     @FXML
    public void onClickViewOrderPerButton(ActionEvent event){
        new OrderPeriodicoC(stage).showViewOrdinePeriodicoB();
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        new HomePageFarmacia(this.stage, new FXMLLoader(HomePageFarmacia.class.getResource("HomePageFarmacia.fxml")));
    }


}
