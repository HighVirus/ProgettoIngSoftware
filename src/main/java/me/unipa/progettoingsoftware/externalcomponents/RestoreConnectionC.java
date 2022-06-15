package me.unipa.progettoingsoftware.externalcomponents;

import javafx.fxml.FXMLLoader;

import java.sql.SQLException;

public class RestoreConnectionC {

    public RestoreConnectionC() {
        this.showConnectionWindow();
    }

    private void showConnectionWindow() {
        ConnectionAttemptController connectionAttemptController = new ConnectionAttemptController();
        FXMLLoader fxmlLoader = new FXMLLoader(ConnectionAttempt.class.getResource("ConnectionAttempt.fxml"));
        fxmlLoader.setRoot(connectionAttemptController);
        fxmlLoader.setController(connectionAttemptController);
        new ConnectionAttempt(fxmlLoader);
    }

    public boolean restoreConnection() {

        try {
            boolean connectionAzienda = DBMSB.getAzienda().checkConnection();
            boolean connectionFarmacia = DBMSB.getFarmacia().checkConnection();

            while (!connectionAzienda || !connectionFarmacia) {
                connectionAzienda = DBMSB.getAzienda().checkConnection();
                connectionFarmacia = DBMSB.getFarmacia().checkConnection();
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
