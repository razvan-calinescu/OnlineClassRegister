package com.example.onlineclassregister;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class HelloController {
    @FXML
    private Label welcomeText;
    private ImageView logo;

    @FXML
    protected void onHelloButtonClick() {
       // welcomeText.setText("Welcome to JavaFX Application!");
        File file = new File("src/Box13.jpg");
        Image image = new Image(file.toURI().toString());
        logo = new ImageView(image);
    }

    @FXML
    protected void init1(){
        ;
    }
}