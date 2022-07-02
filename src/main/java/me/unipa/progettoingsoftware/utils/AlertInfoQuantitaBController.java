package me.unipa.progettoingsoftware.utils;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class AlertInfoQuantitaBController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private MFXTableView<Farmaco> farmacoTable;
    private final AlertC alertC;
    private final List<Farmaco> farmacoList;


    public void setupTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        codAicColumn.setMinWidth(80);
        codAicColumn.resize(80, codAicColumn.getHeight());
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome Farmaco", false, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(250);
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", false, Comparator.comparing(Farmaco::getUnita));
        unitaColumn.setMinWidth(70);
        unitaColumn.resize(70, unitaColumn.getHeight());

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        unitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getUnita));

        farmacoTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, unitaColumn);
        farmacoTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome Farmaco", Farmaco::getFarmacoName),
                new IntegerFilter<>("Unita", Farmaco::getUnita)
        );

        farmacoTable.setItems(FXCollections.observableArrayList(farmacoList));

    }

    @FXML
    public void onClickAlreadyOrdered(ActionEvent event) {
        alertC.clickAlreadyOrdered();
    }

    @FXML
    public void onClickNotOrdered(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
