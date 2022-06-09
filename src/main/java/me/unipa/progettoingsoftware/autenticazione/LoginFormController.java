package me.unipa.progettoingsoftware.autenticazione;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javafx.event.ActionEvent;

@RequiredArgsConstructor
public class LoginFormController extends AnchorPane {
    private final AccessControl accessControl;


    @FXML
    @Getter
    private MFXTextField email;

    @FXML
    @Getter
    private MFXPasswordField password;

    @FXML
    public void onClickLoginButton(ActionEvent event){
        accessControl.clickLoginButton();
    }


}