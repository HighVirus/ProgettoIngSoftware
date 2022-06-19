package me.unipa.progettoingsoftware.gestioneareaaziendale;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.utils.TempoC;
import me.unipa.progettoingsoftware.utils.entity.User;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageAzienda extends Application {
    private final Stage stage;
    private final FXMLLoader fxmlLoader;

    public HomePageAzienda(Stage stage, FXMLLoader fxmlLoader) {
        this.stage = stage;
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
        fxmlLoader.setController(new HomePageAziendaController(stage));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        HomePageAziendaController homePageAziendaController = fxmlLoader.getController();
        Image buttonImage = TempoC.getInstance().isAlertsToRead() ? new Image(getClass().getResourceAsStream("/images/bell-new-alert.png")) : new Image(getClass().getResourceAsStream("/images/bell-alert.png"));
        ImageView imageView = new ImageView(buttonImage);
        imageView.setFitWidth(27);
        imageView.setFitHeight(27);
        homePageAziendaController.getAlertButton().setGraphic(imageView);
        homePageAziendaController.getAlertButton().setBackground(Background.EMPTY);
        homePageAziendaController.getWelcomeText().setText(homePageAziendaController.getWelcomeText().getText()
                .replaceAll("%utente%", User.getUser().getName() + " " + User.getUser().getSurname()));
        stage.show();
    }
}
