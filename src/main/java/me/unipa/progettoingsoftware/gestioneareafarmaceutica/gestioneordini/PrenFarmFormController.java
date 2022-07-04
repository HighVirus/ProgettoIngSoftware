package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import io.github.palexdev.materialfx.controls.MFXTextField;
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
public class PrenFarmFormController extends AnchorPane {

    private final Farmaco farmaco;
    private final OrdersFarC ordersFarC;
    private final Date date;

    @FXML
    @Getter
    private MFXTextField unita;
    @FXML
    @Getter
    private Label textToShow;

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        ordersFarC.clickConfirmPrenFarmForm(farmaco, date);
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
