package me.unipa.progettoingsoftware.utils;

import lombok.Getter;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.PeriodicOrder;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.User;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TempoC {

    @Getter(lazy = true)
    private static final TempoC instance = new TempoC();

    @Getter
    @Setter
    private boolean alertsToRead = false;

    private TempoC() {
        this.rifonisciMagazzinoAziendale();
        this.removeFarmaciScaduti();
        this.checkAlerts();
        this.addPeriodicOrder();
        this.checkOrderReadyToLoadList();
        this.checkUnitFarmaco();
    }

    private void checkUnitFarmaco() {// metodo controllo magazzino farmaco < 50
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                DBMSB.getFarmacia().getFarmacoListCheckStorage().thenAccept(farmacos -> {
                    if (farmacos.isEmpty()) return;
                    for (Farmaco farmaco : farmacos) {
                        DBMSB.getFarmacia().isFarmacoOrdered(farmaco.getCodAic());
                        if (false){
                            TempoC.addToListFarmaciNotOrdered();
                        }
                    }
                    if (){

                    }
                });
            }

        },
                calendar.getTime(), TimeUnit.HOURS.toMillis(24));
    }

    private static void addToListFarmaciNotOrdered() {
    }


    private void rifonisciMagazzinoAziendale() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                Calendar.DAY_OF_WEEK,
                Calendar.MONDAY
        );
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                DBMSB.getAzienda().getFarmaciCatalogList().thenAccept(farmacos -> {
                    if (farmacos.isEmpty()) return;
                    for (Farmaco farmaco : farmacos) {
                        farmaco.setLotto(getRandomLottoCode());
                        farmaco.setUnita(1000);
                        DBMSB.getAzienda().addFarmacoToStorage(farmaco);
                    }
                });
            }
        }, calendar.getTime(), TimeUnit.DAYS.toMillis(7));
    }

    private void removeFarmaciScaduti() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                DBMSB.getAzienda().removeFarmaciExpired();
                DBMSB.getFarmacia().removeFarmaciExpired();
            }
        }, calendar.getTime(), TimeUnit.HOURS.toMillis(24));
    }

    private void checkAlerts() {
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (User.isAuthenticated()) {
                    if (User.getUser().getType() == 1) {
                        DBMSB.getAzienda().thereIsAlertsNotRead().thenAccept(aBoolean -> {
                            alertsToRead = aBoolean;
                        });
                    } else if (User.getUser().getType() == 2) {
                        DBMSB.getFarmacia().thereIsAlertsNotRead(User.getUser().getFarmaciaPiva()).thenAccept(aBoolean -> {
                            alertsToRead = aBoolean;
                        });
                    }
                }
            }
        }, 0, TimeUnit.MINUTES.toMillis(10));
    }

    private void addPeriodicOrder() {  //ogni lunedì
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                Calendar.DAY_OF_WEEK,
                Calendar.MONDAY
        );
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                DBMSB.getAzienda().getFarmaciePivaList().thenAccept(pivaList -> {
                    if (pivaList.isEmpty()) return;
                    for (String piva : pivaList) {
                        DBMSB.getFarmacia().getFarmacoUnitaPeriodic(piva).thenAccept(periodicOrders -> {
                            if (periodicOrders.isEmpty()) return;
                            for (PeriodicOrder periodicOrder : periodicOrders) {
                                DBMSB.getFarmacia().getFarmacoListFromStorage(periodicOrder.getCodAic()).thenAccept(farmacoList -> {
                                    Farmaco farmaco = farmacoList.get(0);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(new Date(System.currentTimeMillis()));
                                    calendar.add(Calendar.YEAR, 2);
                                    DBMSB.getFarmacia().addFarmaciBancoToStorage(periodicOrder.getCodAic(),
                                            getRandomLottoCode(),
                                            periodicOrder.getFarmacoName(),
                                            periodicOrder.getPrincipioAttivo(),
                                            farmaco.isPrescrivibile(),
                                            new Date(calendar.getTimeInMillis()),
                                            farmaco.getCosto(),
                                            periodicOrder.getUnita());
                                });

                            }
                        });
                    }
                });

            }
        }, calendar.getTime(), TimeUnit.DAYS.toMillis(7));
    }


    private void checkOrderReadyToLoadList() {  //ogni giorno alle 20:00
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (User.isAuthenticated() && User.getUser().getType() == 2){
                    DBMSB.getFarmacia().getOrderReadyToLoadList(User.getUser().getFarmaciaPiva());
                }
            }
        }, 0, TimeUnit.MINUTES.toMillis(10));

    }

    private String getRandomLottoCode() {
        String alphaStr = "abcdefghijklmnopqurestuvwxyz";
        String numStr = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        for (int j = 0; j < 3; j++) {
            double randNo = new Random(System.currentTimeMillis()).nextDouble();
            int idx = (int) (alphaStr.length() * randNo);
            stringBuilder.append(alphaStr.charAt(idx));
        }

        for (int j = 0; j < 4; j++) {
            double randNo = new Random(System.currentTimeMillis()).nextDouble();
            int idx = (int) (numStr.length() * randNo);
            stringBuilder.append(numStr.charAt(idx));
        }
        return stringBuilder.toString();
    }
}
