package me.unipa.progettoingsoftware.gestioneareaaziendale.gestionecatalogo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.utils.TempoC;
import me.unipa.progettoingsoftware.utils.entity.User;

import java.net.URL;
import java.util.ResourceBundle;

public class GestioneCatalogo extends Application {

    private final FXMLLoader fxmlLoader;

    public GestioneCatalogo(Stage stage, FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;

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
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        GestioneCatalogoController gestioneCatalogoController = fxmlLoader.getController();
        gestioneCatalogoController.setupTable();
        Image buttonImage = TempoC.getInstance().isAlertsToRead() ? new Image(getClass().getResourceAsStream("/images/bell-new-alert.png")) : new Image(getClass().getResourceAsStream("/images/bell-alert.png"));
        ImageView imageView = new ImageView(buttonImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        gestioneCatalogoController.getAlertButton().setGraphic(imageView);
        gestioneCatalogoController.getWelcomeText().setText(gestioneCatalogoController.getWelcomeText().getText()
                .replaceAll("%utente%", User.getUser().getName() + " " + User.getUser().getSurname()));
        stage.show();
    }
}
