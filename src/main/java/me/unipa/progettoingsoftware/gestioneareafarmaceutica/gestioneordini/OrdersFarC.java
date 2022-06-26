package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNotice;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.CarrelloE;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.GenericNotice;

import java.sql.Date;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class OrdersFarC {
    private final Stage stage;
    private ViewCarrelloController viewCarrelloController;
    private ViewOrdiniController viewOrdiniController;
    private OrderFarmaWindowBController orderFarmaWindowBController;
    private ModOrdFormController modOrdFormController;
    private UnitOrderPerReportController unitOrderPerReportController;
    private PrenFarmFormController prenFarmFormController;
    private Farmaco expiringFarmacoToOrder = null;
    @Setter
    private boolean refuseAddExpiringFarmaco = false;
    @Setter
    private Order orderToCancel = null;

    public void showViewCarrello() {
        viewCarrelloController = new ViewCarrelloController(stage, this, CarrelloE.getInstance().getFarmaci());
        FXMLLoader fxmlLoader = new FXMLLoader(ViewCarrello.class.getResource("ViewCarrello.fxml"));
        fxmlLoader.setRoot(viewCarrelloController);
        fxmlLoader.setController(viewCarrelloController);
        new ViewCarrello(stage, fxmlLoader);
    }

    public void showOrderList() {
        DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(strings -> {
            String piva = strings.get(0);
            DBMSB.getAzienda().getOrderList(piva).thenAccept(orders -> {
                viewOrdiniController = new ViewOrdiniController(stage, this, orders);
                FXMLLoader fxmlLoader = new FXMLLoader(ViewOrdini.class.getResource("ViewOrdini.fxml"));
                fxmlLoader.setRoot(viewOrdiniController);
                fxmlLoader.setController(viewOrdiniController);
                new ViewOrdini(stage, fxmlLoader);
            });
        });
    }

    public void showViewOrdinePeriodicoB() {

    }

    public void showOrderInfoB(Order order) {

    }

    public void showModOrderForm(Order order) {
        if ((ChronoUnit.DAYS.between(order.getDeliveryDate().toLocalDate(), new Date(System.currentTimeMillis()).toLocalDate())) < 2) {
            new ErrorsNotice("Ordine non modificabile");
        } else {
            modOrdFormController = new ModOrdFormController(order, this);
            FXMLLoader fxmlLoader = new FXMLLoader(ViewOrdini.class.getResource("ViewOrdini.fxml"));
            fxmlLoader.setRoot(modOrdFormController);
            fxmlLoader.setController(modOrdFormController);
            new ModOrdForm(stage, fxmlLoader);
        }
    }

    public void showUnitOrderPerReport(Farmaco farmaco) {

    }

    public void clickConfirmModifyOrderPeriodic(Farmaco farmaco) {

    }

    public void confirmCancelOrder() {
        if ((ChronoUnit.DAYS.between(this.orderToCancel.getDeliveryDate().toLocalDate(), new Date(System.currentTimeMillis()).toLocalDate())) < 2) {
            new ErrorsNotice("Ordine non annullabile");
        } else {
            //qui si mette la funzione per cancellare l'ordine porco dio
        }
    }

    public void clickConfirmPrenFarmForm(Farmaco farmaco){

    }

    public void onClickApplyModifyToFarmaco(Farmaco farmaco, String unita) {
        if (!this.isValidUnitaField(unita)) {
            new ErrorsNotice("Valore non valido.");
            return;
        }

        int validUnita = Integer.parseInt(unita);

        if (!this.isModifiedValueValid(farmaco.getCodAic(), farmaco.getLotto(), validUnita)) {
            new ErrorsNotice("Quantità non presente in magazzino.");
            return;
        }

        this.modOrdFormController.getFarmacoTable().getItems().remove(farmaco);
        farmaco.setUnita(validUnita);
        this.modOrdFormController.getFarmacoTable().getItems().addAll(farmaco);
    }

    public void clickConfirmModifyButton(String orderCode) {
        DBMSB.getAzienda().updateOrder();
        DBMSB.getFarmacia().updateOrder();
        new GenericNotice("Modifica effettuata");
    }

    private boolean isModifiedValueValid(String codAic, String lotto, int unita) {
        Farmaco farmaco = DBMSB.getFarmacia().getFarmacoFromStorage(codAic, lotto).join();
        return farmaco.getUnita() >= unita;
    }

    public void clickOrdinaButton() {
        if (orderFarmaWindowBController.getOrderList().getItems().isEmpty() || CarrelloE.getInstance().getFarmaci().isEmpty()) {
            new ErrorsNotice("Il carrello è vuoto.");
            return;
        }

        Order order = new Order();
        DBMSB.getAzienda().createNewOrder(order);
        DBMSB.getFarmacia().createNewOrder(order);
        new GenericNotice("Ordine creato conferma");
    }

    public void showEmptyNotice() {
        EmptyCarrelloNoticeController emptyCarrelloNoticeController = new EmptyCarrelloNoticeController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(EmptyCarrelloNotice.class.getResource("EmptyCarrelloNotice.fxml"));
        fxmlLoader.setRoot(emptyCarrelloNoticeController);
        fxmlLoader.setController(emptyCarrelloNoticeController);
        new ConfirmRemNotice(new Stage(), fxmlLoader);
    }

    public void confirmEmptyCarrello() {
        CarrelloE.getInstance().removeFarmaci();
        viewCarrelloController.getCarrello().getItems().clear();
        new GenericNotice("Carrello svuotato.");
    }

    public void showOrderFarmaWindowB() {
        DBMSB.getAzienda().getFarmaciCatalogList().thenAccept(farmacoList -> {
            Platform.runLater(() -> {
                orderFarmaWindowBController = new OrderFarmaWindowBController(farmacoList, this, stage);
                FXMLLoader fxmlLoader = new FXMLLoader(OrderFarmaWindowB.class.getResource("OrderFarmaWindowB.fxml"));
                fxmlLoader.setRoot(orderFarmaWindowBController);
                fxmlLoader.setController(orderFarmaWindowBController);
                new OrderFarmaWindowB(stage, fxmlLoader);
            });

        });

    }

    public void addProductToCarrelloList(Farmaco farmaco, String textField) {
        if (isValidUnitaField(textField)) {
            int unita = Integer.parseInt(textField);
            farmaco.setUnita(unita);
            for (Farmaco f1 : orderFarmaWindowBController.getOrderList().getItems()) {
                if (f1.getCodAic().equalsIgnoreCase(farmaco.getCodAic())) {
                    new ErrorsNotice("Prodotto gia aggiunto nel carrello");
                    return;
                }
            }
            DBMSB.getAzienda().checkFarmacoAvailability(farmaco.getCodAic(), unita).thenAccept(aBoolean -> {
                Platform.runLater(() -> {
                    if (aBoolean) {
                        double costoSingolo = farmaco.getCosto() * farmaco.getUnita();
                        farmaco.setCosto(costoSingolo);

                        DBMSB.getAzienda().getFarmacoExpireDate(farmaco.getCodAic()).thenAccept(date -> {
                            if ((ChronoUnit.DAYS.between(date.toLocalDate(), new Date(System.currentTimeMillis()).toLocalDate())) < 60) {
                                this.showFarmacoExpiringNotice();
                                while (expiringFarmacoToOrder == null && !refuseAddExpiringFarmaco) ;
                            }
                        });
                        orderFarmaWindowBController.getOrderList().getItems().add(farmaco);
                        CarrelloE.getInstance().getFarmaci().add(farmaco);

                    } else {
                        DBMSB.getAzienda().getFarmacoAvailabilityDate(farmaco.getCodAic(), farmaco.getUnita()).thenAccept(date -> {
                            Platform.runLater(() -> {
                                this.showNoAvailabilityNotice(date);
                            });
                        });
                    }

                });
            });
        } else {
            new ErrorsNotice("Hai inserito un valore non valido, ricontrolla.");
        }
    }

    public void showFarmacoExpiringNotice() {
        FarmacoExpiringNoticeController farmacoExpiringNoticeController = new FarmacoExpiringNoticeController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(FarmacoExpiringNotice.class.getResource("FarmacoExpiringNotice.fxml"));
        fxmlLoader.setRoot(farmacoExpiringNoticeController);
        fxmlLoader.setController(farmacoExpiringNoticeController);
        new FarmacoExpiringNotice(new Stage(), fxmlLoader);
    }

    public void confirmAddExpiringFarmaco() {

    }

    public void showHomePageFarmacia() {
        new HomePageFarmacia(this.stage, new FXMLLoader(HomePageFarmacia.class.getResource("HomePageFarmacia.fxml")));
    }

    public void showNoAvailabilityNotice(Date date, Farmaco farmaco) {
        NoAvailabilityNoticeController noAvailabilityNoticeController = new NoAvailabilityNoticeController(this, date, farmaco);
        FXMLLoader fxmlLoader = new FXMLLoader(NoAvailabilityNotice.class.getResource("NoAvailabilityNotice.fxml"));
        fxmlLoader.setRoot(noAvailabilityNoticeController);
        fxmlLoader.setController(noAvailabilityNoticeController);
        new NoAvailabilityNotice(new Stage(), fxmlLoader, date);
    }

    public void showPrenFarmForm(Date date, Farmaco farmaco) {
        //crea l'ordine
        new GenericNotice("Ordine creato correttamente, data di consegna: " + date);
    }

    private boolean isValidUnitaField(String textField) {
        if (textField.length() == 0)
            return false;
        try {
            int value = Integer.parseInt(textField);
            return value > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
