package me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo.ConfirmRemNotice;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.InfoOrderB;
import me.unipa.progettoingsoftware.gestioneareaaziendale.gestioneordiniaziendali.InfoOrderBController;
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
import java.util.Calendar;
import java.util.Random;

@RequiredArgsConstructor
public class OrdersFarC {
    private final Stage stage;
    private ViewCarrelloController viewCarrelloController;
    private InfoOrderBController infoOrderBController;
    private OrderListController orderListController;
    private OrderFarmaWindowBController orderFarmaWindowBController;
    private ConfirmCancelOrderNoticeController confirmCancelOrderNoticeController;
    private ModOrdFormController modOrdFormController;

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
        String piva = User.getUser().getFarmaciaPiva();
        DBMSB.getAzienda().getOrderList(piva).thenAccept(orders -> {
            orderListController = new OrderListController(stage, this, orders);
            FXMLLoader fxmlLoader = new FXMLLoader(OrderList.class.getResource("OrderList.fxml"));
            fxmlLoader.setRoot(orderListController);
            fxmlLoader.setController(orderListController);
            new OrderList(stage, fxmlLoader);
        });
    }

    public void showOrderInfoB(Order order) {
        InfoOrderBController infoOrderBController = new InfoOrderBController(order);
        FXMLLoader fxmlLoader = new FXMLLoader(InfoOrderB.class.getResource("InfoOrderB.fxml"));
        fxmlLoader.setRoot(infoOrderBController);
        fxmlLoader.setController(infoOrderBController);
        new InfoOrderB(new Stage(), fxmlLoader);
    }

    public void showModOrderForm(Order order) {
        if ((ChronoUnit.DAYS.between(order.getDeliveryDate().toLocalDate(), new Date(System.currentTimeMillis()).toLocalDate())) < 2) {
            new ErrorsNotice("Ordine non modificabile");
        } else {
            modOrdFormController = new ModOrdFormController(order, this);
            FXMLLoader fxmlLoader = new FXMLLoader(ModOrdForm.class.getResource("ModOrderForm.fxml"));
            fxmlLoader.setRoot(modOrdFormController);
            fxmlLoader.setController(modOrdFormController);
            new ModOrdForm(stage, fxmlLoader);
        }
    }


    public void showConfirmCancelOrderNotice(Order order) {
        if ((ChronoUnit.DAYS.between(this.orderToCancel.getDeliveryDate().toLocalDate(), new Date(System.currentTimeMillis()).toLocalDate())) < 2) {
            new ErrorsNotice("Ordine non annullabile");
        } else {
            confirmCancelOrderNoticeController = new ConfirmCancelOrderNoticeController(order, this);
            FXMLLoader fxmlLoader = new FXMLLoader(ConfirmCancelOrderNotice.class.getResource("ConfirmCancelOrderNotice.fxml"));
            fxmlLoader.setRoot(confirmCancelOrderNoticeController);
            fxmlLoader.setController(confirmCancelOrderNoticeController);
            new ConfirmCancelOrderNotice(stage, fxmlLoader);
        }
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
        DBMSB.getAzienda().updateOrder(orderCode);
        DBMSB.getFarmacia().updateOrder(orderCode);
        new GenericNotice("Ordine modificato con successo");
    }

    public void clickConfirmCancelButton(String orderCode) {
        DBMSB.getAzienda().deleteOrder(orderCode);
        DBMSB.getFarmacia().deleteOrder(orderCode);
        new GenericNotice("Ordine annullato con successo");
    }

    private boolean isModifiedValueValid(String codAic, String lotto, int unita) {
        Farmaco farmaco = DBMSB.getFarmacia().getFarmacoFromStorage(User.getUser().getFarmaciaPiva(), codAic, lotto).join();
        return farmaco.getUnita() >= unita;
    }

    public void clickOrdinaButton() {
        if (orderFarmaWindowBController.getOrderList().getItems().isEmpty() || CarrelloE.getInstance().getFarmaci().isEmpty()) {
            new ErrorsNotice("Il carrello è vuoto.");
        } else if (orderFarmaWindowBController.getOrderList().getItems().isEmpty() && !CarrelloE.getInstance().getFarmaci().isEmpty()) {
            String orderCode = String.valueOf(new Random(System.currentTimeMillis()).nextInt(99999));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(strings -> {
                String piva = strings.get(0);
                String farmaciaName = strings.get(1);
                DBMSB.getAzienda().getFarmaciaInfo(piva).thenAccept(strings1 -> {
                    String indirizzo = strings1.get(2);
                    String cap = strings1.get(3);
                    String email = strings1.get(1);
                    Order order = new Order(orderCode, new Date(calendar.getTimeInMillis()),
                            piva, farmaciaName, indirizzo, cap, email, 1);
                    order.getFarmacoList().addAll(CarrelloE.getInstance().getFarmaci());
                    DBMSB.getAzienda().createNewOrder(order);
                    DBMSB.getFarmacia().createNewOrder(order);
                    Platform.runLater(() -> {
                        new GenericNotice("Ordine creato conferma");
                    });
                });
            });
        } else if (!orderFarmaWindowBController.getOrderList().getItems().isEmpty() && CarrelloE.getInstance().getFarmaci().isEmpty()) {
            String orderCode = String.valueOf(new Random(System.currentTimeMillis()).nextInt(99999));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(strings -> {
                String piva = strings.get(0);
                String farmaciaName = strings.get(1);
                DBMSB.getAzienda().getFarmaciaInfo(piva).thenAccept(strings1 -> {
                    String indirizzo = strings1.get(2);
                    String cap = strings1.get(3);
                    String email = strings1.get(1);
                    Order order = new Order(orderCode, new Date(calendar.getTimeInMillis()),
                            piva, farmaciaName, indirizzo, cap, email, 1);
                    order.getFarmacoList().addAll(orderFarmaWindowBController.getOrderList().getItems());
                    DBMSB.getAzienda().createNewOrder(order);
                    DBMSB.getFarmacia().createNewOrder(order);
                    Platform.runLater(() -> {
                        new GenericNotice("Ordine creato conferma");
                    });
                });
            });
        }

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
                                this.showFarmacoExpiringNotice(farmaco);
                                while (expiringFarmacoToOrder == null && !refuseAddExpiringFarmaco) ;
                            }
                        });
                        orderFarmaWindowBController.getOrderList().getItems().add(farmaco);
                        CarrelloE.getInstance().getFarmaci().add(farmaco);

                    } else {
                        DBMSB.getAzienda().getFarmacoAvailabilityDate(farmaco.getCodAic(), farmaco.getUnita()).thenAccept(date -> {
                            Platform.runLater(() -> {
                                this.showNoAvailabilityNotice(date, farmaco);
                            });
                        });
                    }

                });
            });
        } else {
            new ErrorsNotice("Hai inserito un valore non valido, ricontrolla.");
        }
    }

    public void showFarmacoExpiringNotice(Farmaco farmaco) {
        FarmacoExpiringNoticeController farmacoExpiringNoticeController = new FarmacoExpiringNoticeController(this, farmaco);
        FXMLLoader fxmlLoader = new FXMLLoader(FarmacoExpiringNotice.class.getResource("FarmacoExpiringNotice.fxml"));
        fxmlLoader.setRoot(farmacoExpiringNoticeController);
        fxmlLoader.setController(farmacoExpiringNoticeController);
        new FarmacoExpiringNotice(new Stage(), fxmlLoader);
    }

    public void confirmAddExpiringFarmaco(Farmaco farmaco) {
        orderFarmaWindowBController.getOrderList().getItems().add(farmaco);
        CarrelloE.getInstance().getFarmaci().add(farmaco);
        new GenericNotice("Prodotto aggiunto con successo.");
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        PrenFarmFormController prenFarmFormController = new PrenFarmFormController(farmaco, this, new Date(calendar.getTimeInMillis()));
        FXMLLoader fxmlLoader = new FXMLLoader(PrenFarmForm.class.getResource("PrenFarmForm.fxml"));
        fxmlLoader.setRoot(prenFarmFormController);
        fxmlLoader.setController(prenFarmFormController);
        new NoAvailabilityNotice(new Stage(), fxmlLoader, date);

    }

    public void clickConfirmPrenFarmForm(Farmaco farmaco, Date date) {
        String orderCode = String.valueOf(new Random(System.currentTimeMillis()).nextInt(99999));
        DBMSB.getAzienda().getFarmaciaFromUserId(User.getUser().getId()).thenAccept(strings -> {
            String piva = strings.get(0);
            String farmaciaName = strings.get(1);
            DBMSB.getAzienda().getFarmaciaInfo(piva).thenAccept(strings1 -> {
                String indirizzo = strings1.get(2);
                String cap = strings1.get(3);
                String email = strings1.get(1);
                Order order = new Order(orderCode, date,
                        piva, farmaciaName, indirizzo, cap, email, 1);
                order.getFarmacoList().add(farmaco);
                DBMSB.getAzienda().createNewOrder(order);
                DBMSB.getFarmacia().createNewOrder(order);
            });
        });


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

    public void removeFromCarrello(Farmaco farmaco) {
        viewCarrelloController.getCarrello().getItems().remove(farmaco);
        CarrelloE.getInstance().getFarmaci().remove(farmaco);
    }
    public void showGestioneOrdiniB() {
        GestioneOrdiniBController gestioneOrdiniBController = new GestioneOrdiniBController(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(GestioneOrdiniB.class.getResource("GestioneOrdiniB.fxml"));
        fxmlLoader.setRoot(gestioneOrdiniBController);
        fxmlLoader.setController(gestioneOrdiniBController);
        new GestioneOrdiniB(stage, fxmlLoader);
    }
}
