package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.utils.Homepage;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;

import java.util.Comparator;
import java.util.List;

public class GestioneCatalogoController extends Homepage {

    @FXML
    @Getter
    private MFXTableView<Farmaco> catalogo;
    private final Stage stage;
    private final CatalogoAzControl catalogoAzControl;
    private final List<Farmaco> catalogList;

    public GestioneCatalogoController(Stage stage, CatalogoAzControl catalogoAzControl, List<Farmaco> catalogList) {
        super(stage);
        this.stage = stage;
        this.catalogoAzControl = catalogoAzControl;
        this.catalogList = catalogList;
    }

    public void setupTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Farmaco::getFarmacoName));
        farmacoNameColumn.setPrefWidth(350);
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        principioAttivoColumn.setPrefWidth(200);
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
                    catalogoAzControl.setFarmacoToRemove(farmaco);
                    catalogoAzControl.showConfirmRemNotice();
                });
            }
        });


        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));

        catalogo.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, costoColumn, removeColumn);
        catalogo.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new DoubleFilter<>("Costo", Farmaco::getCosto)
        );

        catalogo.setItems(FXCollections.observableArrayList(catalogList));


    }

    @FXML
    public void onClickAggiungiButton(ActionEvent event) {
        catalogoAzControl.addProductRequest();
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        new HomePageAzienda(this.stage, new FXMLLoader(HomePageAzienda.class.getResource("HomePageAzienda.fxml")));
    }
}
