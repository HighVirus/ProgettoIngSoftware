package me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
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
import me.unipa.progettoingsoftware.utils.entity.Order;

import java.util.Comparator;
import java.util.List;

public class OrderListBController extends Homepage {

    @FXML
    @Getter
    private MFXTableView<Order> orderTable;
    private final Stage stage;
    private final OrdersC ordersC;
    private final List<Order> orderList;

    public OrderListBController(Stage stage, OrdersC ordersC, List<Order> orderList) {
        super(stage);
        this.stage = stage;
        this.ordersC = ordersC;
        this.orderList = orderList;
    }

    public void setupTable() {
        MFXTableColumn<Order> orderCodeColumn = new MFXTableColumn<>("Codice Ordine", true, Comparator.comparing(Order::getOrderCode));
        MFXTableColumn<Order> pivaColumn = new MFXTableColumn<>("P.IVA Farmacia", true, Comparator.comparing(Order::getPivaFarmacia));
        MFXTableColumn<Order> farmaciaNameColumn = new MFXTableColumn<>("Nome Farmacia", true);
        MFXTableColumn<Order> farmaciaEmailColumn = new MFXTableColumn<>("Indirizzo E-Mail", true);
        MFXTableColumn<Order> infoOrderColumn = new MFXTableColumn<>("", false);
        infoOrderColumn.setMinWidth(50);
        infoOrderColumn.resize(50, infoOrderColumn.getHeight());
        infoOrderColumn.setColumnResizable(false);
        infoOrderColumn.setRowCellFactory(param -> new MFXTableRowCell<>(farmaco -> farmaco) {
            private final MFXButton infoOrderButton = new MFXButton("?");

            @Override
            public void update(Order order) {
                if (order == null) {
                    setGraphic(null);
                    return;
                }

                infoOrderButton.setStyle("-fx-background-color: #FF595E;" + "-fx-font-weight: bold;");
                infoOrderButton.setTextFill(Paint.valueOf("WHITE"));

                setGraphic(infoOrderButton);
                infoOrderButton.setOnAction(event -> {
                    /*storageAziendaC.setFarmacoToRemove(farmaco);
                    storageAziendaC.showConfirmRemNotice();*/
                });
            }
        });


        orderCodeColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getOrderCode));
        pivaColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getPivaFarmacia));

        orderTable.getTableColumns().addAll(orderCodeColumn, pivaColumn, farmaciaNameColumn, farmaciaEmailColumn, infoOrderColumn);
        orderTable.getFilters().addAll(
                new StringFilter<>("Codice Ordine", Order::getOrderCode),
                new StringFilter<>("P.IVA Farmacia", Order::getPivaFarmacia)
        );

        orderTable.setItems(FXCollections.observableArrayList(orderList));


    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        new HomePageAzienda(this.stage, new FXMLLoader(HomePageAzienda.class.getResource("HomePageAzienda.fxml")));
    }
}
