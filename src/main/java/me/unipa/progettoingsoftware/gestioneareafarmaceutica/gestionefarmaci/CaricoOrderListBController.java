package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CaricoOrderListBController extends AnchorPane {

    @FXML
    private MFXTableView<Order> orderTable;

    private final List<Order> orderList;
    private final StorageFarmaciaC storageFarmaciaC;
    @Getter
    private final Stage stage;

    public void setup() {
        MFXTableColumn<Order> orderCodeColumn = new MFXTableColumn<>("Codice Ordine", true, Comparator.comparing(Order::getOrderCode));
        MFXTableColumn<Order> deliveryDateColumn = new MFXTableColumn<>("Data Consegna", true, Comparator.comparing(Order::getDeliveryDate));
        MFXTableColumn<Order> statusColumn = new MFXTableColumn<>("Stato", true, Comparator.comparing(Order::getStatus));

        MFXTableColumn<Order> addOrderButtonColumn = new MFXTableColumn<>("", false);
        addOrderButtonColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton addUnitProduct = new MFXButton("+");

            @Override
            public void update(Order order) {
                if (order == null) {
                    setGraphic(null);
                    return;
                }

                addUnitProduct.setStyle("-fx-background-color: #FF595E;" + "-fx-font-weight: bold;");
                addUnitProduct.setTextFill(Paint.valueOf("WHITE"));

                setGraphic(addUnitProduct);
                addUnitProduct.setOnAction(event -> {
                    storageFarmaciaC.addFarmaciFromOrder(order);
                });
            }
        });

        orderCodeColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getOrderCode));
        deliveryDateColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getDeliveryDate));
        statusColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getStatus));

        orderTable.getTableColumns().addAll(orderCodeColumn, deliveryDateColumn, statusColumn, addOrderButtonColumn);
        orderTable.setItems(FXCollections.observableArrayList(orderList));
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}