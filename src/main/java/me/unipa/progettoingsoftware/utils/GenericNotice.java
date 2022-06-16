package me.unipa.progettoingsoftware.utils;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class GenericNotice extends Application {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;
    private final String text;
    private final GenericNoticeController genericNoticeController;

    public GenericNotice(String text) {
        this.stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(GenericNotice.class.getResource("GenericNotice.fxml"));
        genericNoticeController = new GenericNoticeController();
        fxmlLoader.setRoot(genericNoticeController);
        fxmlLoader.setController(genericNoticeController);
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
        fxmlLoader.setController(genericNoticeController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        genericNoticeController.getTextToShow().setText(genericNoticeController.getTextToShow().getText()
                .replaceAll("%text%", this.text));
        stage.show();
    }
}
