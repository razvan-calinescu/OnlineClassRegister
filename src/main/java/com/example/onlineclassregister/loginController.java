package com.example.onlineclassregister;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private Button loginExitButton;

    public void exitButton(ActionEvent e)
    {

        Stage stage = (Stage) loginExitButton.getScene().getWindow();
        stage.close();
    }
}
