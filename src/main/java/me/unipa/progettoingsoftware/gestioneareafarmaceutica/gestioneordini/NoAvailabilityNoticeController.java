package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;

import java.sql.Date;

@RequiredArgsConstructor
public class NoAvailabilityNoticeController extends AnchorPane {
    private final OrdersFarC ordersFarC;
    private final Date date;
    private final Farmaco farmaco;
    @FXML
    @Getter
    private Label textToShow;


    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ordersFarC.showPrenFarmForm(date, farmaco);
        stage.close();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ordersFarC.setRefuseAddExpiringFarmaco(true);
        stage.close();
    }
}
