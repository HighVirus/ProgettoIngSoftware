package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddProdFormController extends AnchorPane {
    private final CatalogoAzControl catalogoAzControl;
    @Getter
    private final Stage stage;

    @FXML
    @Getter
    private MFXTextField codiceAic;
    @FXML
    @Getter
    private MFXTextField nomeProdotto;
    @FXML
    @Getter
    private MFXTextField principioAttivo;
    @FXML
    @Getter
    private MFXComboBox<String> prescrivibilita;
    @FXML
    @Getter
    private MFXTextField costo;

    public void setupPrescrivibilitaComboBox() {
        prescrivibilita.setItems(FXCollections.observableArrayList("Sì", "No"));
    }

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        catalogoAzControl.confirmAddProduct();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        stage.close();
    }
}
