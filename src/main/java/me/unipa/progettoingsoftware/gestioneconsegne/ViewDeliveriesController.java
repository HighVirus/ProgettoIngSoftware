package me.unipa.progettoingsoftware.gestioneconsegne;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.CatalogoAzControl;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.util.Comparator;
import java.util.List;

public class ViewDeliveriesController {

    @FXML
    @Getter
    private MFXTableView<Order> order;
    private final Stage stage;
    private final DeliveriesC deliveriesC;

    public ViewDeliveriesController(Stage stage, DeliveriesC deliveriesC) {
        super(stage);
        this.stage = stage;
        this.deliveriesC = deliveriesC;
    }
}
