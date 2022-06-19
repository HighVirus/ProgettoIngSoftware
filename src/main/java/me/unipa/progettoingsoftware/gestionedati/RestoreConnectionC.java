package me.unipa.progettoingsoftware.gestionedati;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class RestoreConnectionC {
    private final Stage stage;

    public RestoreConnectionC(Stage stage) {
        this.stage = stage;
        this.showConnectionWindow();
    }

    private void showConnectionWindow() {
        ConnectionAttemptController connectionAttemptController = new ConnectionAttemptController();
        FXMLLoader fxmlLoader = new FXMLLoader(ConnectionAttempt.class.getResource("ConnectionAttempt.fxml"));
        fxmlLoader.setRoot(connectionAttemptController);
        fxmlLoader.setController(connectionAttemptController);
        new ConnectionAttempt(this.stage, fxmlLoader);
    }
}
