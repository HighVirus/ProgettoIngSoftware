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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.utils.Homepage;
import me.unipa.progettoingsoftware.utils.entity.Order;

import java.util.Comparator;
import java.util.List;

public class OrderListBController extends Homepage {

    @FXML
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
        orderCodeColumn.setPrefWidth(130);
        MFXTableColumn<Order> pivaColumn = new MFXTableColumn<>("P.IVA Farmacia", true, Comparator.comparing(Order::getPivaFarmacia));
        pivaColumn.setPrefWidth(130);
        MFXTableColumn<Order> farmaciaNameColumn = new MFXTableColumn<>("Nome Farmacia", true, Comparator.comparing(Order::getFarmaciaName));
        farmaciaNameColumn.setPrefWidth(130);
        MFXTableColumn<Order> farmaciaEmailColumn = new MFXTableColumn<>("Indirizzo E-Mail", true, Comparator.comparing(Order::getEmail));
        farmaciaEmailColumn.setPrefWidth(230);
        MFXTableColumn<Order> infoOrderColumn = new MFXTableColumn<>("", false);
        infoOrderColumn.setRowCellFactory(param -> new MFXTableRowCell<>(order -> order) {
            private final MFXButton infoOrderButton = new MFXButton("");

            @Override
            public void update(Order order) {
                if (order == null) {
                    setGraphic(null);
                    return;
                }
                Image buttonImage = new Image(getClass().getResourceAsStream("/images/info.png"));
                ImageView imageView = new ImageView(buttonImage);
                imageView.setFitWidth(15);
                imageView.setFitHeight(17);
                infoOrderButton.setTextFill(Paint.valueOf("WHITE"));
                infoOrderButton.setGraphic(imageView);

                setGraphic(infoOrderButton);
                infoOrderButton.setOnAction(event -> ordersC.showInfoOrder(order));
            }
        });


        orderCodeColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getOrderCode));
        pivaColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getPivaFarmacia));
        farmaciaNameColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getFarmaciaName));
        farmaciaEmailColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getEmail));

        orderTable.getTableColumns().addAll(orderCodeColumn, pivaColumn, farmaciaNameColumn, farmaciaEmailColumn, infoOrderColumn);
        orderTable.getFilters().addAll(
                new StringFilter<>("Codice Ordine", Order::getOrderCode),
                new StringFilter<>("P.IVA Farmacia", Order::getPivaFarmacia),
                new StringFilter<>("Nome Farmacia", Order::getFarmaciaName),
                new StringFilter<>("Indirizzo E-Mail", Order::getEmail)
        );

        orderTable.setItems(FXCollections.observableArrayList(orderList));


    }

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        new HomePageAzienda(this.stage, new FXMLLoader(HomePageAzienda.class.getResource("HomePageAzienda.fxml")));
    }

    @FXML
    public void onClickCreateOrderButton(ActionEvent event){
        ordersC.showCreateOrder();
    }
}
