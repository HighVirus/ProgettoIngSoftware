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
        MFXTableColumn<Order> codAicColumn = new MFXTableColumn<>("Codice AIC", false, Comparator.comparing(Farmaco::getCodAic));
        codAicColumn.setMinWidth(90);
        codAicColumn.resize(90, codAicColumn.getHeight());
        MFXTableColumn<Order> farmacoNameColumn = new MFXTableColumn<>("Nome", false, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(230);
        MFXTableColumn<Order> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(170);
        MFXTableColumn<Order> expireDateColumn = new MFXTableColumn<>("Scadenza", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        MFXTableColumn<Order> disponibilitaColumn = new MFXTableColumn<>("Disponibilità", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        disponibilitaColumn.setMinWidth(80);
        disponibilitaColumn.resize(80, disponibilitaColumn.getHeight());
        MFXTableColumn<Order> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        costoColumn.setMinWidth(80);
        costoColumn.resize(80, costoColumn.getHeight());
        MFXTableColumn<Order> unitaColumn = new MFXTableColumn<>("Unità", false);

        MFXTableColumn<Order> addUnitProductColumn = new MFXTableColumn<>("", false);
        addUnitProductColumn.setMinWidth(50);
        addUnitProductColumn.resize(50, addUnitProductColumn.getHeight());
        addUnitProductColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
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

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getCosto));
        expireDateColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getScadenza));
        disponibilitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Order::getUnita));

        orderTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, disponibilitaColumn, expireDateColumn, costoColumn, unitaColumn, addUnitProductColumn);
        orderTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Order::getCodAic),
                new StringFilter<>("Nome", Order::getFarmacoName),
                new StringFilter<>("Principio Attivo", Order::getPrincipioAttivo),
                new IntegerFilter<>("Disponibilità", Order::getUnita),
                new DoubleFilter<>("Costo", Order::getCosto)
        );

        orderTable.setItems(FXCollections.observableArrayList(orderList));
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}