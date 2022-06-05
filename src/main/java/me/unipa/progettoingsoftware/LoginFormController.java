package me.unipa.progettoingsoftware;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javafx.event.ActionEvent;

@RequiredArgsConstructor
public class LoginFormController extends AnchorPane {
    private final LoginC loginC;


    @FXML
    @Getter
    private MFXTextField email;

    @FXML
    @Getter
    private MFXPasswordField password;

    @FXML
    public void onClickButton(ActionEvent event){
        loginC.clickButton();
    }


}
