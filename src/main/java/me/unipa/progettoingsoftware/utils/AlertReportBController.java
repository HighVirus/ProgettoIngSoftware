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
import me.unipa.progettoingsoftware.gestionedati.entity.AlertE;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class AlertReportBController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private MFXTableView<AlertE> alertTable;
    private final AlertC alertC;
    private final List<AlertE> alertList;


    public void setupTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        codAicColumn.setMinWidth(80);
        codAicColumn.resize(80, codAicColumn.getHeight());
        MFXTableColumn<Farmaco> lottoColumn = new MFXTableColumn<>("Lotto", false, Comparator.comparing(Farmaco::getLotto));
        lottoColumn.setMinWidth(80);
        lottoColumn.resize(80, lottoColumn.getHeight());
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", false, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(250);
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(100);
        MFXTableColumn<Farmaco> prescrivibileColumn = new MFXTableColumn<>("Prescrivibile", false, Comparator.comparing(Farmaco::isPrescrivibile));
        prescrivibileColumn.setMinWidth(80);
        prescrivibileColumn.resize(80, prescrivibileColumn.getHeight());

        MFXTableColumn<Farmaco> expireDateColumn = new MFXTableColumn<>("Data di scadenza", false, Comparator.comparing(Farmaco::getScadenza));
        expireDateColumn.setMinWidth(90);
        expireDateColumn.resize(90, expireDateColumn.getHeight());

        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        costoColumn.setMinWidth(65);
        costoColumn.resize(65, costoColumn.getHeight());

        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", false, Comparator.comparing(Farmaco::getUnita));
        unitaColumn.setMinWidth(70);
        unitaColumn.resize(70, unitaColumn.getHeight());

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        lottoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getLotto));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        prescrivibileColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::isPrescrivibile));
        expireDateColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getScadenza));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));
        unitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getUnita));

        alertTable.getTableColumns().addAll(codAicColumn, lottoColumn, farmacoNameColumn, principioAttivoColumn, prescrivibileColumn, expireDateColumn, costoColumn, unitaColumn);
        alertTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Lotto", Farmaco::getLotto),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new BooleanFilter<>("Prescrivibilità", Farmaco::isPrescrivibile),
                new DoubleFilter<>("Costo", Farmaco::getCosto),
                new IntegerFilter<>("Unita", Farmaco::getUnita)
        );

        alertTable.setItems(FXCollections.observableArrayList(storageList));

    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
