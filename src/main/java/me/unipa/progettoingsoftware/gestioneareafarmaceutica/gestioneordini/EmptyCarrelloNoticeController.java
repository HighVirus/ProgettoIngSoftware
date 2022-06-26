package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionemagazzinoaziendale.StorageAziendaC;

@RequiredArgsConstructor
public class EmptyCarrelloNoticeController extends AnchorPane {
    private final OrdersFarC ordersFarC;


    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ordersFarC.confirmEmptyCarrello();
        stage.close();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
