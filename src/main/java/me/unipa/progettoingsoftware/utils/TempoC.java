package me.unipa.progettoingsoftware.utils;

import lombok.Getter;
import lombok.Setter;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.entity.User;

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
                        DBMSB.getAzienda().addFarmaciToStorage(farmaco);
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

    private void checkAlerts() {  //da aggiungere quando maria fa il database
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
                        DBMSB.getFarmacia().thereIsAlertsNotRead().thenAccept(aBoolean -> {
                            alertsToRead = aBoolean;
                        });
                    }
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
