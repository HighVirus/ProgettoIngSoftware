package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
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
    private MFXTableView<Farmaco> farmacoTable;
    private final List<Farmaco> farmacoList;

    public ViewOrdinePeriodicoBController(Stage stage, OrderPeriodicoC orderPeriodicoC, List<Farmaco> farmacoList) {
        super(stage);
        this.stage = stage;
        this.orderPeriodicoC = orderPeriodicoC;
        this.farmacoList = farmacoList;
    }

    public void setupTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome Farmaco", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(130);
        MFXTableColumn<Farmaco> principioColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioColumn.setPrefWidth(130);
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", true, Comparator.comparing(Farmaco::getUnita));
        unitaColumn.setPrefWidth(230);
        MFXTableColumn<Farmaco> intervalloColumn = new MFXTableColumn<>("Intervallo", true);

        intervalloColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final Label label = new Label("Ogni lunedì");

            @Override
            public void update(Farmaco farmaco){
                if (farmaco == null){
                    setGraphic(null);
                    return;
                }
            }
        });

        MFXTableColumn<Farmaco> modOrdPerButton = new MFXTableColumn<>("", false);
        modOrdPerButton.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton modOrdPerButton = new MFXButton("");

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
                modOrdPerButton.setTextFill(Paint.valueOf("WHITE"));
                modOrdPerButton.setGraphic(imageView);

                setGraphic(modOrdPerButton);
                modOrdPerButton.setOnAction(event -> {
                    orderPeriodicoC.showUnitOrderPerReport(farmaco);
                });
            }
        });


        codAicColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        unitaColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Farmaco::getUnita));

        farmacoTable.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioColumn, unitaColumn, intervalloColumn, modOrdPerButton);
        farmacoTable.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome Farmaco", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new IntegerFilter<>("Unità", Farmaco::getUnita)
        );

        farmacoTable.setItems(FXCollections.observableArrayList(farmacoList));
    }

    @FXML
    public void onClickTornaButton() {
        orderPeriodicoC.showHomePageFarmacia();
    }

}
