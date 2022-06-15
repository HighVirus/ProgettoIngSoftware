package me.unipa.progettoingsoftware.utils;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorsNotice extends Application {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;
    private final String text;
    private final ErrorsNoticeController errorsNoticeController;

    public ErrorsNotice(String text) {
        this.stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ErrorsNotice.class.getResource("ErrorsNotice.fxml"));
        errorsNoticeController = new ErrorsNoticeController(stage);
        fxmlLoader.setRoot(errorsNoticeController);
        fxmlLoader.setController(errorsNoticeController);
        this.fxmlLoader = fxmlLoader;
        this.text = text;

        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader.setController(errorsNoticeController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        errorsNoticeController.getTextToShow().setText(errorsNoticeController.getTextToShow().getText()
                .replaceAll("%text%", this.text));
        stage.show();
    }
}
