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

@RequiredArgsConstructor
public class PrenFarmFormController extends AnchorPane {

    private final Farmaco farmaco;
    private final OrdersFarC ordersFarC;

    @FXML
    @Getter
    private MFXTextField unita;
    @FXML
    @Getter
    private Label textToShow;

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        ordersFarC.clickConfirmPrenFarmForm(farmaco);
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
