package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.util.Comparator;
import java.util.List;

public class ViewOrdiniController extends Homepage {

    @FXML
    @Getter
    private final Stage stage;
    private final OrdersFarC ordersFarC;
    @FXML
    @Getter
    private MFXTableView<Order> orderTable;
    private final List<Order> orderList;

    public ViewOrdiniController(Stage stage, OrdersFarC ordersFarC, List<Order> orderList) {
        super(stage);
        this.stage = stage;
        this.ordersFarC = ordersFarC;
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
        MFXTableColumn<Order> statoColumn = new MFXTableColumn<>("Stato Consegna", true, Comparator.comparing(Order::getStatus));
        farmaciaEmailColumn.setPrefWidth(230);
        MFXTableColumn<Order> infoOrderColumn = new MFXTableColumn<>("", false);
        infoOrderColumn.setRowCellFactory(param -> new MFXTableRowCell<>(order -> order) {
            private final MFXButton infoOrderButton = new MFXButton("!");

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
                infoOrderButton.setOnAction(event -> {

                });
            }
        });

        MFXTableColumn<Order> modOrderColumn = new MFXTableColumn<>("", false);
        infoOrderColumn.setRowCellFactory(param -> new MFXTableRowCell<>(order -> order) {
            private final MFXButton modOrderButton = new MFXButton("?");

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
                modOrderButton.setTextFill(Paint.valueOf("WHITE"));
                modOrderButton.setGraphic(imageView);

                setGraphic(modOrderButton);
                modOrderButton.setOnAction(event -> {
                    ordersFarC.showModOrderForm(order);
                });
            }
        });

        MFXTableColumn<Order> cancelOrderColumn = new MFXTableColumn<>("", false);
        infoOrderColumn.setRowCellFactory(param -> new MFXTableRowCell<>(order -> order) {
            private final MFXButton cancelOrderButton = new MFXButton("X");


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
                cancelOrderButton.setTextFill(Paint.valueOf("WHITE"));
                cancelOrderButton.setGraphic(imageView);

                setGraphic(cancelOrderButton);
                cancelOrderButton.setOnAction(event -> {
                    ordersFarC.setOrderToCancel(order);
                });
            }
        });


        orderCodeColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getOrderCode));
        pivaColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getPivaFarmacia));
        farmaciaNameColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getFarmaciaName));
        farmaciaEmailColumn.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getEmail));
        statoColumn.setRowCellFactory(order -> new MFXTableRowCell<>(order1 -> order1.getStatus().getValue()));

        orderTable.getTableColumns().addAll(orderCodeColumn, pivaColumn, farmaciaNameColumn, farmaciaEmailColumn, statoColumn, infoOrderColumn);
        orderTable.getFilters().addAll(
                new StringFilter<>("Codice Ordine", Order::getOrderCode),
                new StringFilter<>("P.IVA Farmacia", Order::getPivaFarmacia),
                new StringFilter<>("Nome Farmacia", Order::getFarmaciaName),
                new StringFilter<>("Indirizzo E-Mail", Order::getEmail),
                new StringFilter<>("Stato Consegna", order -> order.getStatus().getValue())
        );

        orderTable.setItems(FXCollections.observableArrayList(orderList));
    }

    @FXML
    public void onClickTornaButton() {
        ordersFarC.showHomePageFarmacia();
    }

}
