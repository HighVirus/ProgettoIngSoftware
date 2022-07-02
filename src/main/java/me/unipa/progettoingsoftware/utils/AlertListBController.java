package me.unipa.progettoingsoftware.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestionedati.entity.AlertE;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class AlertListBController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private MFXTableView<AlertE> alertTable;
    private final AlertC alertC;

    @Getter
    private final List<AlertE> alertList;

    public void setupTable() {
        MFXTableColumn<AlertE> codiceAlertColumn = new MFXTableColumn<>("Codice Alert", true, Comparator.comparing(AlertE::getCodeAlert));
        codiceAlertColumn.setPrefWidth(130);
        MFXTableColumn<AlertE> tipoColumn = new MFXTableColumn<>("Tipo Alert", true, Comparator.comparing(AlertE::getAlertType));
        tipoColumn.setPrefWidth(130);

        MFXTableColumn<AlertE> infoAlertColumn = new MFXTableColumn<>("", false);
        infoAlertColumn.setRowCellFactory(param -> new MFXTableRowCell<>(alertE -> alertE) {
            private final MFXButton infoAlertButton = new MFXButton("");

            @Override
            public void update(AlertE alertE) {
                if (alertE == null) {
                    setGraphic(null);
                    return;
                }
                Image buttonImage = new Image(getClass().getResourceAsStream("/images/info.png"));
                ImageView imageView = new ImageView(buttonImage);
                imageView.setFitWidth(15);
                imageView.setFitHeight(17);
                infoAlertButton.setTextFill(Paint.valueOf("WHITE"));
                infoAlertButton.setGraphic(imageView);

                setGraphic(infoAlertButton);
                infoAlertButton.setOnAction(event -> alertC.showViewAlertFarmacia(alertE));


            }
        });


        codiceAlertColumn.setRowCellFactory(order -> new MFXTableRowCell<>(AlertE::getCodeAlert));
        tipoColumn.setRowCellFactory(order -> new MFXTableRowCell<>(AlertE::getAlertType));


        alertTable.getTableColumns().addAll(codiceAlertColumn, tipoColumn, infoAlertColumn);
        alertTable.getFilters().addAll(
                new StringFilter<>("Codice Alert", AlertE::getCodeAlert),
                new IntegerFilter<>("Tipo Alert", AlertE::getAlertType)

        );

        alertTable.setItems(FXCollections.observableArrayList(alertList));
    }
}
