package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.utils.Farmaco;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.util.Comparator;

public class GestioneCatalogoController extends Homepage {

    @FXML
    public MFXTableView<Farmaco> catalogo;

    public GestioneCatalogoController(Stage stage, CatalogoAzControl catalogoAzControl) {
        super(stage);
    }

    public void setupTable() {
        MFXTableColumn<Farmaco> codAicColumn = new MFXTableColumn<>("Codice AIC", true, Comparator.comparing(Farmaco::getCodAic));
        MFXTableColumn<Farmaco> farmacoNameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Farmaco::getFarmacoName));
        MFXTableColumn<Farmaco> principioAttivoColumn = new MFXTableColumn<>("Principio Attivo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));
        MFXTableColumn<Farmaco> costoColumn = new MFXTableColumn<>("Costo", true, Comparator.comparing(Farmaco::getPrincipioAttivo));

        codAicColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCodAic));
        farmacoNameColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getFarmacoName));
        principioAttivoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getPrincipioAttivo));
        costoColumn.setRowCellFactory(farmaco -> new MFXTableRowCell<>(Farmaco::getCosto));

        catalogo.getTableColumns().addAll(codAicColumn, farmacoNameColumn, principioAttivoColumn, costoColumn);
        catalogo.getFilters().addAll(
                new StringFilter<>("Codice AIC", Farmaco::getCodAic),
                new StringFilter<>("Nome", Farmaco::getFarmacoName),
                new StringFilter<>("Principio Attivo", Farmaco::getPrincipioAttivo),
                new DoubleFilter<>("Costo", Farmaco::getCosto)
        );
    }

    @FXML
    public void onClickAggiungiButton(ActionEvent event) {
    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
    }
}
