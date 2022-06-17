package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.entity.Order;

import java.util.Comparator;

@RequiredArgsConstructor
public class InfoOrderBController extends AnchorPane {

    @FXML
    private MFXTableView<Farmaco> farmacoTable;
    private final Order order;

    @FXML
    private Label codiceOrdine;
    @FXML
    private Label pivaFarmacia;
    @FXML
    private Label indirizzoEmail;
    @FXML
    private Label cap;
    @FXML
    private Label indirizzo;
    @FXML
    private Label deliveryDate;


    public void setupFields() {
        codiceOrdine.setText(codiceOrdine.getText().replaceAll("%CodiceOrdine%", order.getOrderCode()));
        pivaFarmacia.setText(pivaFarmacia.getText().replaceAll("%pivaFarmacia%", order.getPivaFarmacia()));
        indirizzoEmail.setText(indirizzoEmail.getText().replaceAll("%IndirizzoEmail%", order.getEmail()));
        cap.setText(cap.getText().replaceAll("%CAP%", order.getCap()));
        indirizzo.setText(indirizzo.getText().replaceAll("%Indirizzo%", order.getIndirizzo()));
        deliveryDate.setText(deliveryDate.getText().replaceAll("%DataConsegna%", order.getDeliveryDate().toString()));


        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome Farmaco", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(250);
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", true, Comparator.comparing(Farmaco::getUnita));
        unitaColumn.setPrefWidth(130);

        farmacoNameColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        unitaColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getUnita));

        farmacoTable.getTableColumns().addAll(farmacoNameColumn, unitaColumn);

        farmacoTable.setItems(FXCollections.observableArrayList(order.getFarmacoList()));
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
