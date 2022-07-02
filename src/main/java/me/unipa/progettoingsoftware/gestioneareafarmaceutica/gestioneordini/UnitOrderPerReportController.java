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

@RequiredArgsConstructor
public class UnitOrderPerReportController extends AnchorPane {

    private final PeriodicOrder periodicOrder;
    private final OrderPeriodicoC orderPeriodicoC;
    @Getter
    private final Stage stage;

    @FXML
    @Getter
    private MFXTextField unita;
    @FXML
    @Getter
    private Label textToShow;

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        orderPeriodicoC.clickConfirmModifyOrderPeriodic(periodicOrder);
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
