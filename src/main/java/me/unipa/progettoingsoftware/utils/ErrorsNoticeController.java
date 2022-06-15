package me.unipa.progettoingsoftware.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorsNoticeController extends AnchorPane {
    private final Stage stage;
    @FXML
    @Getter
    private Label textToShow;

    @FXML
    public void onClickTornaButton(ActionEvent event) {
        stage.close();
    }
}
