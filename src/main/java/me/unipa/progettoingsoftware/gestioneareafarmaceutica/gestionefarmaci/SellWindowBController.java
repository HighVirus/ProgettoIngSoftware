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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SellWindowBController extends AnchorPane {

    @FXML
    private MFXTableView<Farmaco> storageTable;
    @FXML
    @Getter
    private MFXTableView<Farmaco> carrelloTable;
    private final List<Farmaco> storageList;
    private final SellFarmacoC sellFarmacoC;
    @Getter
    private final Stage stage;


    public void setup() {
        this.setupStorageTable();
        this.setupCarrelloTable();
    }

    private void setupStorageTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", false, Comparator.comparing(Farmaco::getCodAic));
        codAicColumn.setMinWidth(90);
        codAicColumn.resize(90, codAicColumn.getHeight());
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", false, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(230);
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(170);
        MFXTableColumn<Farmaco> expireDateColumn = new MFXTableColumn<>("Scadenza", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        MFXTableColumn<Farmaco> disponibilitaColumn = new MFXTableColumn<>("Disponibilità", false, Comparator.comparing(Farmaco::getPrincipioAttivo));
        disponibilitaColumn.setMinWidth(80);
        disponibilitaColumn.resize(80, disponibilitaColumn.getHeight());
        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        costoColumn.setMinWidth(80);
        costoColumn.resize(80, costoColumn.getHeight());
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", false);

        Map<Farmaco, TextField> fields = new HashMap<>();

        unitaColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                TextField field = fields.get(farmaco);
                if (field == null) {
                    field = new TextField();
                    fields.put(farmaco, field);
                }
                setGraphic(field);
            }
        });

        MFXTableColumn<Farmaco> addUnitProductColumn = new MFXTableColumn<>("", false);
        addUnitProductColumn.setMinWidth(50);
        addUnitProductColumn.resize(50, addUnitProductColumn.getHeight());
        addUnitProductColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton addUnitProduct = new MFXButton("+");

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
                    sellFarmacoC.addProductToSellList(farmaco, fields.get(farmaco).getText());
                });
            }
        });

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));
        expireDateColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getScadenza));
        disponibilitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getUnita));

        storageTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, disponibilitaColumn, expireDateColumn, costoColumn, unitaColumn, addUnitProductColumn);
        storageTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new IntegerFilter<>("Disponibilità", Farmaco::getUnita),
                new DoubleFilter<>("Costo", Farmaco::getCosto)
        );

        storageTable.setItems(FXCollections.observableArrayList(storageList));
    }

    private void setupCarrelloTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(350);
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(200);
        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));

        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", false);
        unitaColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final Label label = new Label();

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }
                label.setText(String.valueOf(farmaco.getUnita()));

                setGraphic(label);
            }
        });

        MFXTableColumn<Farmaco> addUnitProductColumn = new MFXTableColumn<>("", false);

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
                    carrelloTable.getItems().remove(farmaco);
                });
            }
        });

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));

        carrelloTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, costoColumn, unitaColumn, addUnitProductColumn);
        carrelloTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new DoubleFilter<>("Costo", Farmaco::getCosto)
        );

    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        sellFarmacoC.clickConfirmButton();
    }
}