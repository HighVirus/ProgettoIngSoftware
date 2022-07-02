package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.util.Comparator;
import java.util.List;

public class ViewCarrelloController extends Homepage {

    @FXML
    @Getter
    private MFXTableView<Farmaco> carrello;
    private final Stage stage;
    private final OrdersFarC ordersFarC;
    private final List<Farmaco> carrelloList;

    public ViewCarrelloController(Stage stage, OrdersFarC ordersFarC, List<Farmaco> carrelloList) {
        super(stage);
        this.stage = stage;
        this.ordersFarC = ordersFarC;
        this.carrelloList = carrelloList;
    }

    public void setupTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(350);
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(200);
        MFXTableColumn<Farmaco> unitaColumn = new MFXTableColumn<>("Unità", true, Comparator.comparing(Farmaco::getUnita));
        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));


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
                    ordersFarC.removeFromCarrello(farmaco);
                });
            }
        });


        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));
        unitaColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getUnita));

        carrello.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, unitaColumn, costoColumn, removeColumn);
        carrello.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new DoubleFilter<>("Costo", Farmaco::getCosto),
                new IntegerFilter<>("Unità", Farmaco::getUnita)
        );

        carrello.setItems(FXCollections.observableArrayList(carrelloList));


    }

    @FXML
    public void onClickOrdinaButton(ActionEvent event) {
        ordersFarC.clickOrdinaButton();
    }

    public void onClickEmptyCarrelloButton(ActionEvent event) {
        ordersFarC.showEmptyNotice();
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        ordersFarC.showHomePageFarmacia();
    }
}
