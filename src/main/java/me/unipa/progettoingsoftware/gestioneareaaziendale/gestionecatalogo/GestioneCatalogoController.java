package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.externalcomponents.DBMSB;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.util.Comparator;

public class GestioneCatalogoController extends Homepage {

    @FXML
    public MFXTableView<Farmaco> catalogo;
    private final Stage stage;
    private final CatalogoAzControl catalogoAzControl;

    public GestioneCatalogoController(Stage stage, CatalogoAzControl catalogoAzControl) {
        super(stage);
        this.stage = stage;
        this.catalogoAzControl = catalogoAzControl;
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
                super.update(farmaco);

                if (farmaco == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> catalogo.getItems().remove(farmaco)
                );
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
        DBMSB.getAzienda().getFarmaciCatalogList().whenComplete((farmacoList, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(farmacoList -> {
            Platform.runLater(() -> {
                catalogo.setItems(FXCollections.observableArrayList(farmacoList));
            });
        });

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
