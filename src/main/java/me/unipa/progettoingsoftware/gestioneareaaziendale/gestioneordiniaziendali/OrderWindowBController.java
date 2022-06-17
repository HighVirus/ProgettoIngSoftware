package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class OrderWindowBController extends AnchorPane {

    @FXML
    private MFXTableView<Farmaco> catalogoTable;
    @FXML
    private MFXListView<Farmaco> farmaciList;
    private final List<Farmaco> catalogList;
    private final OrdersC ordersC;

    @FXML
    private TextField piva;


    public void setup() {
        this.setupCatalogoTable();
    }

    private void setupCatalogoTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(350);
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(200);
        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));

        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", false);
        unitaColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final TextField unitaField = new TextField();

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(unitaField);
            }
        });

        MFXTableColumn<Farmaco> addUnitProductColumn = new MFXTableColumn<>("Aggiungi", false);
        addUnitProductColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton addUnitProduct = new MFXButton("X");

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                addUnitProduct.setStyle("-fx-background-color: #FF595E;" + "-fx-font-weight: bold;");
                addUnitProduct.setTextFill(Paint.valueOf("WHITE"));

                setGraphic(addUnitProduct);
                addUnitProduct.setOnAction(event -> {
                    farmaciList.getItems().add(farmaco);
                });
            }
        });

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));

        catalogoTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, costoColumn, addUnitProductColumn, unitaColumn);
        catalogoTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new DoubleFilter<>("Costo", Farmaco::getCosto)
        );

        catalogoTable.setItems(FXCollections.observableArrayList(catalogList));
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickConfirmButton(ActionEvent event) {

    }
}
