package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.Init;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaricaProductsFormController extends MFXScrollPane {

    @FXML
    @Getter
    private MFXTableView<Farmaco> caricoList;
    private final Stage stage;
    private final StorageFarmaciaC storageFarmaciaC;

    @FXML
    @Getter
    private MFXTextField orderCode;
    @FXML
    @Getter
    private MFXTextField aicCode;
    @FXML
    @Getter
    private MFXTextField lottoCode;
    @FXML
    @Getter
    private MFXTextField farmacoName;
    @FXML
    @Getter
    private MFXTextField principioAttivo;
    @FXML
    @Getter
    private MFXComboBox<String> prescrivibilita;
    @FXML
    @Getter
    private MFXDatePicker expireDate;
    @FXML
    @Getter
    private MFXTextField costo;
    @FXML
    @Getter
    private MFXTextField unita;

    public CaricaProductsFormController(Stage stage, StorageFarmaciaC storageFarmaciaC) {
        this.stage = stage;
        this.storageFarmaciaC = storageFarmaciaC;
    }

    public void setupPrescrivibilitaComboBox() {
        prescrivibilita.setItems(FXCollections.observableArrayList("S??", "No"));
    }

    public void setupTable() {
        MFXTableColumn<Farmaco> orderCodeColumn = new MFXTableColumn<>("Ordine", true, Comparator.comparing(Farmaco::getOrderCode));
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> lottoColumn = new MFXTableColumn<>("Lotto", true, Comparator.comparing(Farmaco::getLotto));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(350);
        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getCosto));
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unit??", true, Comparator.comparing(Farmaco::getCosto));

        MFXTableColumn<Farmaco> modificaCostoColumn = new MFXTableColumn<>("Modifica Costo", false);
        Map<Farmaco, TextField> fields = new HashMap<>();
        modificaCostoColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {

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

        MFXTableColumn<Farmaco> modificaColumn = new MFXTableColumn<>("", false);
        modificaColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton modificaButton = new MFXButton(" ");

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                Image buttonImage = new Image(getClass().getResourceAsStream("/images/modificaButtonImage.png"));
                ImageView imageView = new ImageView(buttonImage);
                imageView.setFitWidth(15);
                imageView.setFitHeight(17);
                modificaButton.setTextFill(Paint.valueOf("WHITE"));
                modificaButton.setGraphic(imageView);
                setGraphic(modificaButton);
                modificaButton.setOnAction(event -> {
                    storageFarmaciaC.modificaCostoFarmaco(farmaco, fields.get(farmaco).getText());
                });
            }
        });


        MFXTableColumn<Farmaco> removeColumn = new MFXTableColumn<>("", false);
        removeColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton deleteButton = new MFXButton("X");

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                deleteButton.setStyle("-fx-background-color: #FF595E;" + "-fx-font-weight: bold;");
                deleteButton.setTextFill(Paint.valueOf("WHITE"));

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    storageFarmaciaC.removeProductFromCaricoList(farmaco);
                });
            }
        });


        orderCodeColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getOrderCode));
        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        lottoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getLotto));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        unitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getUnita));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));

        caricoList.getTableColumns().addAll(orderCodeColumn, codAicColumn, lottoColumn, farmacoNameColumn, costoColumn, unitaColumn, modificaCostoColumn, modificaColumn, removeColumn);
        caricoList.getFilters().addAll(
                new StringFilter<>("Ordine", Farmaco::getOrderCode),
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Lotto", Farmaco::getLotto),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new DoubleFilter<>("Costo", Farmaco::getCosto),
                new IntegerFilter<>("Unit??", Farmaco::getUnita)
        );


    }

    @FXML
    public void onClickAddButton(ActionEvent event) {
        storageFarmaciaC.addProductToCaricoList();
    }

    @FXML
    public void onClickFarmFromOrder(ActionEvent event) {
        storageFarmaciaC.showCaricoOrderListB();
    }

    @FXML
    public void onClickConfirmButton(ActionEvent event) {
        storageFarmaciaC.clickConfirmButton();
    }

    @FXML
    public void onClickAnnullaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public int checkList() {
        return caricoList.getItems().size();
    }

}
