package me.unipa.progettoingsoftware.autenticazione;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.HomePageFarmacia;
import me.unipa.progettoingsoftware.gestioneconsegne.HomePageCorriere;
import me.unipa.progettoingsoftware.gestionedati.DBMSB;
import me.unipa.progettoingsoftware.gestioneareaaziendale.HomePageAzienda;
import me.unipa.progettoingsoftware.utils.ErrorsNotice;
import me.unipa.progettoingsoftware.utils.GenericNotice;
import me.unipa.progettoingsoftware.utils.TempoB;

public class AccessControl {
    private final Stage stage;
    private LoginForm loginForm;
    private LoginFormController loginFormController;

    public AccessControl(Stage stage) {
        this.stage = stage;
    }

    public void showLoginForm() {
        this.loginFormController = new LoginFormController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("LoginForm.fxml"));
        fxmlLoader.setRoot(this.loginFormController);
        fxmlLoader.setController(this.loginFormController);

        loginForm = new LoginForm(this.stage, fxmlLoader);
    }

    public void clickLoginButton() {
        String email = loginFormController.getEmail().getText();
        String password = loginFormController.getPassword().getText();
        DBMSB.getAzienda().getUser(email, password).whenComplete((user, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(user -> {
            Platform.runLater(() -> {
                if (user != null) {
                    FXMLLoader fxmlLoader;

                    int userType = user.getType();
                    new TempoB();
                    switch (userType) {
                        case 1 -> {
                            fxmlLoader = new FXMLLoader(HomePageAzienda.class.getResource("HomePageAzienda.fxml"));
                            new HomePageAzienda(this.stage, fxmlLoader);
                        }
                        case 2 -> {
                            fxmlLoader = new FXMLLoader(HomePageFarmacia.class.getResource("HomePageFarmacia.fxml"));
                            new HomePageFarmacia(this.stage, fxmlLoader);
                        }
                        case 3 -> {
                            fxmlLoader = new FXMLLoader(HomePageCorriere.class.getResource("HomePageCorriere.fxml"));
                            new HomePageCorriere(this.stage, fxmlLoader);
                        }
                        default -> {
                            new ErrorsNotice("Qualcosa ?? andato storto, contatta l'amministratore.");
                        }
                    }
                    new GenericNotice("Login effettuato con successo.");
                } else
                    new ErrorsNotice("Login fallito, controlla i dati e riprova.");
            });
        });
    }
}
