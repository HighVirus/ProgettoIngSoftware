package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.util.Comparator;
import java.util.List;

public class ViewOrdinePeriodicoBController extends Homepage {

    private final OrderPeriodicoC orderPeriodicoC;
    @FXML
    @Getter
    private final Stage stage;


    @FXML
    @Getter
    private MFXTableView<PeriodicOrder> periodicOrderTable;
    private final List<PeriodicOrder> periodicOrders;

    public ViewOrdinePeriodicoBController(Stage stage, OrderPeriodicoC orderPeriodicoC, List<PeriodicOrder> periodicOrders) {
        super(stage);
        this.stage = stage;
        this.orderPeriodicoC = orderPeriodicoC;
        this.periodicOrders = periodicOrders;
    }

    public void setupTable() {
        MFXTableColumn<PeriodicOrder> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(PeriodicOrder::getCodAic));
        MFXTableColumn<PeriodicOrder> farmacoNameColumn = new MFXTableColumn<>("Nome Farmaco", true, Comparator.comparing(PeriodicOrder::getFarmacoName));
        farmacoNameColumn.setPrefWidth(130);
        MFXTableColumn<PeriodicOrder> principioColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(PeriodicOrder::getPrincipioAttivo));
        principioColumn.setPrefWidth(130);
        MFXTableColumn<PeriodicOrder> unitaColumn = new MFXTableColumn<>("Unità", true, Comparator.comparing(PeriodicOrder::getUnita));
        unitaColumn.setPrefWidth(230);
        MFXTableColumn<PeriodicOrder> intervalloColumn = new MFXTableColumn<>("Intervallo", true, Comparator.comparing(PeriodicOrder::getPeriodic));

        MFXTableColumn<PeriodicOrder> modOrdPerButton = new MFXTableColumn<>("", false);
        modOrdPerButton.setRowCellFactory(param -> new MFXTableRowCell<>(periodicOrder -> periodicOrder) {
            private final MFXButton modOrdPerButton = new MFXButton("");

            @Override
            public void update(PeriodicOrder periodicOrder) {
                if (periodicOrder == null) {
                    setGraphic(null);
                    return;
                }
                Image buttonImage = new Image(getClass().getResourceAsStream("/images/modificaButtonImage.png"));
                ImageView imageView = new ImageView(buttonImage);
                imageView.setFitWidth(15);
                imageView.setFitHeight(17);
                modOrdPerButton.setTextFill(Paint.valueOf("WHITE"));
                modOrdPerButton.setGraphic(imageView);

                setGraphic(modOrdPerButton);
                modOrdPerButton.setOnAction(event -> {
                    orderPeriodicoC.showUnitOrderPerReport(periodicOrder);
                });
            }
        });


        codAicColumn.setRowCellFactory(periodicOrder -> new MFXTableRowCell<>(PeriodicOrder::getCodAic));
        farmacoNameColumn.setRowCellFactory(periodicOrder -> new MFXTableRowCell<>(PeriodicOrder::getFarmacoName));
        principioColumn.setRowCellFactory(periodicOrder -> new MFXTableRowCell<>(PeriodicOrder::getPrincipioAttivo));
        unitaColumn.setRowCellFactory(periodicOrder -> new MFXTableRowCell<>(PeriodicOrder::getUnita));
        intervalloColumn.setRowCellFactory(periodicOrder -> new MFXTableRowCell<>(PeriodicOrder::getPeriodic));

        periodicOrderTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioColumn, unitaColumn, intervalloColumn, modOrdPerButton);
        periodicOrderTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", PeriodicOrder::getCodAic),
                new StringFilter<>("Nome Farmaco", PeriodicOrder::getFarmacoName),
                new StringFilter<>("Principio Attivo", PeriodicOrder::getPrincipioAttivo),
                new IntegerFilter<>("Unità", PeriodicOrder::getUnita)
        );

        periodicOrderTable.setItems(FXCollections.observableArrayList(periodicOrders));
    }

    @FXML
    public void onClickTornaButton() {
        orderPeriodicoC.showGestioneOrdiniB();
    }

}
