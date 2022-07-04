package me.unipa.progettoingsoftware.gestioneconsegne;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.unipa.progettoingsoftware.Init;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestionefarmaci.StorageFarmaciaC;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.Homepage;

import java.io.IOException;

@RequiredArgsConstructor
public class HomePageCorriereController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private Label welcomeText;

    @FXML
    public void onClickLogoutButton(ActionEvent event){
        if (User.isAuthenticated())
            User.getUser().logout();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Init.class.getResource("InitPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Ipazia");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onClickViewDeliveriesButton(ActionEvent event) {
        new DeliveriesC(stage).showDeliveryList();
    }

}
