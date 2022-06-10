package me.unipa.progettoingsoftware.externalcomponents;

import javafx.fxml.FXMLLoader;

import java.sql.SQLException;

public class RestoreConnectionC {
    private ConnectionAttemptController connectionAttemptController;
    private ConnectionAttempt connectionAttempt;

    public RestoreConnectionC() {
        this.showConnectionWindow();
    }

    public void showConnectionWindow() {
        connectionAttemptController = new ConnectionAttemptController();
        FXMLLoader fxmlLoader = new FXMLLoader(ConnectionAttempt.class.getResource("ConnectionAttempt.fxml"));
        fxmlLoader.setRoot(connectionAttemptController);
        fxmlLoader.setController(connectionAttemptController);

        connectionAttempt = new ConnectionAttempt(fxmlLoader);
    }

    public void restoreConnection() {

        try {
            boolean connectionAzienda = DBMSB.getAzienda().checkConnection();
            boolean connectionFarmacia = DBMSB.getFarmacia().checkConnection();

            while (!connectionAzienda && !connectionFarmacia) {
                connectionAzienda = DBMSB.getAzienda().checkConnection();
                connectionFarmacia = DBMSB.getFarmacia().checkConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
