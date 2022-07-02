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
        MFXTableColumn<AlertE> codiceAlert = new MFXTableColumn<>("Codice Alert", true, Comparator.comparing(AlertE::getCodeAlert));
        codiceAlert.setMinWidth(80);
        /*MFXTableColumn<AlertE> codiceOrdine = new MFXTableColumn<>("Codice Ordine", true, Comparator.comparing(AlertE:://getCodeOrder));
        codiceOrdine.setMinWidth(80);
        MFXTableColumn<AlertE> piva = new MFXTableColumn<>("PIVA", true, Comparator.comparing(AlertE:://getPiva));
        piva.setMinWidth(80);*/



        codiceAlert.setRowCellFactory(farmaco -> new MFXTableRowCell<>(AlertE::getCodeAlert));


        alertTable.getTableColumns().addAll();
        alertTable.getFilters().addAll(
                new StringFilter<>(),
                new StringFilter<>(),
                new StringFilter<>()


        );



    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
