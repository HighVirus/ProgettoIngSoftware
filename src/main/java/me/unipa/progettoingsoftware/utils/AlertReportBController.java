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
    private MFXTableView<Order> orderTable;
    private final AlertC alertC;
    @Getter
    private final List<Order> orderList;


    public void setupTable() {
        MFXTableColumn<Order> codiceOrdine = new MFXTableColumn<>("Codice Ordine", true, Comparator.comparing(Order::getOrderCode));
        codiceOrdine.setMinWidth(80);
        MFXTableColumn<Order> piva = new MFXTableColumn<>("Partita IVA", true, Comparator.comparing(Order::getPivaFarmacia));
        piva.setMinWidth(80);



        codiceOrdine.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getOrderCode));
        piva.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getPivaFarmacia));


        orderTable.getTableColumns().addAll(codiceOrdine, piva);
        orderTable.getFilters().addAll(
                new StringFilter<>("Codice Ordine", (Order::getOrderCode)),
                new StringFilter<>("Partita IVA", (Order::getPivaFarmacia))


        );



    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
