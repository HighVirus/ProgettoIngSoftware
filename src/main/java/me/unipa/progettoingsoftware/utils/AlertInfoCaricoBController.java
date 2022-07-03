package me.unipa.progettoingsoftware.utils;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class AlertInfoCaricoBController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private MFXTableView<Farmaco> farmacoTable;

    @FXML
    private Label codiceOrdine;
    @FXML
    private Label pivaFarmacia;
    @FXML
    private Label indirizzoEmail;
    private final AlertC alertC;
    private final List<Order> orderList;

    public void setupTable() {

        codiceOrdine.setText(codiceOrdine.getText().replaceAll("%CodiceOrdine%", order.getOrderCode()));
        pivaFarmacia.setText(pivaFarmacia.getText().replaceAll("%pivaFarmacia%", order.getPivaFarmacia()));
        indirizzoEmail.setText(indirizzoEmail.getText().replaceAll("%IndirizzoEmail%", order.getEmail()));

        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome Farmaco", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(250);
        MFXTableColumn<Farmaco> lottoColumn = new MFXTableColumn<>("Lotto", true, Comparator.comparing(Farmaco::getLotto));
        farmacoNameColumn.setPrefWidth(100);
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", true, Comparator.comparing(Farmaco::getUnita));


        lottoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getLotto));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        unitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getUnita));

        orderList.getTableColumns().addAll(farmacoNameColumn, lottoColumn, unitaColumn);
        orderList.getFilters().addAll(
                new StringFilter<>("Nome Farmaco", Farmaco::getFarmacoName),
                new StringFilter<>("Lotto", Farmaco::getLotto),
                new IntegerFilter<>("Unita", Farmaco::getUnita)
        );

        farmacoTable.setItems(FXCollections.observableArrayList(order.getFarmacoList()));

    }

    @FXML
    public void onClickConfirmAll(ActionEvent event) {
        alertC.clickConfirmAll();
    }

    @FXML
    public void onClickConfirmNotAll(ActionEvent event) {
        alertC.clickConfirmNotAll();

    }
    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }



}
