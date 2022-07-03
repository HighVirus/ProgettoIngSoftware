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
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class AlertReportBController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private MFXTableView<AlertE> alertTable;
    private final AlertC alertC;
    @Getter
    private final List<AlertE> alertList;


    public void setupTable() {
        MFXTableColumn<AlertE> codiceAlertColumn = new MFXTableColumn<>("Codice Alert", true, Comparator.comparing(AlertE::getCodeAlert));
        codiceAlertColumn.setMinWidth(80);
        MFXTableColumn<AlertE> messaggioColumn = new MFXTableColumn<>("Messaggio", true, Comparator.comparing(AlertE::getMessage));
        messaggioColumn.setMinWidth(80);

        codiceAlertColumn.setRowCellFactory(order -> new MFXTableRowCell<>(AlertE::getCodeAlert));
        messaggioColumn.setRowCellFactory(order -> new MFXTableRowCell<>(AlertE::getMessage));

        alertTable.getTableColumns().addAll(codiceAlertColumn, messaggioColumn);
        alertTable.getFilters().addAll(
                new StringFilter<>("Codice Alert", (AlertE::getCodeAlert)),
                new StringFilter<>("Messaggio", (AlertE::getMessage))
        );

    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
