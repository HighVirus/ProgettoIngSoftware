package me.unipa.progettoingsoftware;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class LoginC {
    private final Stage stage;
    private LoginForm loginForm;
    private LoginFormController loginFormController;

    public LoginC(Stage stage) {
        this.stage = stage;
        this.inizializzaLoginForm(stage);
    }

    private void inizializzaLoginForm(Stage stage) {
        loginFormController = new LoginFormController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("LoginForm.fxml"));
        fxmlLoader.setRoot(loginFormController);
        fxmlLoader.setController(loginFormController);

        loginForm = new LoginForm(stage, fxmlLoader);
    }

    public void clickButton() {
        System.out.println("porco dio1: " + loginFormController.getEmail().getText());
        System.out.println("porco dio2: " + loginFormController.getPassword().getText());
    }
}
