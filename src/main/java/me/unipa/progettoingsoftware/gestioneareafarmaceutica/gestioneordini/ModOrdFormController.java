package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
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
import java.util.Map;

@RequiredArgsConstructor
public class ModOrdFormController extends AnchorPane {

    @FXML
    @Getter
    private MFXTableView<Farmaco> farmacoTable;
    private final Order order;
    private final OrdersFarC ordersFarC;

    @FXML
    private Label codiceOrdine;
    @FXML
    private Label pivaFarmacia;
    @FXML
    private Label indirizzoEmail;
    @FXML
    private Label cap;
    @FXML
    private Label indirizzo;
    @FXML
    private Label deliveryDate;
    @FXML
    private Label deliveryStatus;


    public void setupFields() {
        codiceOrdine.setText(codiceOrdine.getText().replaceAll("%CodiceOrdine%", order.getOrderCode()));
        pivaFarmacia.setText(pivaFarmacia.getText().replaceAll("%pivaFarmacia%", order.getPivaFarmacia()));
        indirizzoEmail.setText(indirizzoEmail.getText().replaceAll("%IndirizzoEmail%", order.getEmail()));
        cap.setText(cap.getText().replaceAll("%CAP%", order.getCap()));
        indirizzo.setText(indirizzo.getText().replaceAll("%Indirizzo%", order.getIndirizzo()));
        deliveryDate.setText(deliveryDate.getText().replaceAll("%DataConsegna%", order.getDeliveryDate().toString()));
        deliveryStatus.setText(deliveryStatus.getText().replaceAll("%StatoConsegna%", order.getStatus().getValue()));


        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome Farmaco", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(250);
        MFXTableColumn<Farmaco> lottoColumn = new MFXTableColumn<>("Lotto", true, Comparator.comparing(Farmaco::getLotto));
        farmacoNameColumn.setPrefWidth(100);
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", true, Comparator.comparing(Farmaco::getUnita));
        unitaColumn.setPrefWidth(130);

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

        farmacoNameColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        lottoColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getLotto));
        unitaColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getUnita));

        MFXTableColumn<Farmaco> addUnitProductColumn = new MFXTableColumn<>("", false);
        addUnitProductColumn.setMinWidth(50);
        addUnitProductColumn.resize(50, addUnitProductColumn.getHeight());
        addUnitProductColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton applyModifyUnita = new MFXButton("Edit");

            @Override
            public void update(Farmaco farmaco) {
                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                applyModifyUnita.setStyle("-fx-background-color: #FF595E;" + "-fx-font-weight: bold;");
                applyModifyUnita.setTextFill(Paint.valueOf("WHITE"));

                setGraphic(applyModifyUnita);
                applyModifyUnita.setOnAction(event -> {
                    ordersFarC.onClickApplyModifyToFarmaco(farmaco, fields.get(farmaco).getText());
                });
            }
        });

        farmacoTable.getTableColumns().addAll(farmacoNameColumn, lottoColumn, unitaColumn);

        farmacoTable.setItems(FXCollections.observableArrayList(order.getFarmacoList()));
    }

    @FXML
    public void onClickConfirmModifyButton(ActionEvent event){
        ordersFarC.clickConfirmModifyButton(order.getOrderCode());
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
