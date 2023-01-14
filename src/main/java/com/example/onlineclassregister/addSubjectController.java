package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class addSubjectController {

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField name;

    public void addButtonClick() {
        if (!name.getText().isEmpty()) {

            String nameSQL=name.getText();

            dbConnection dbConn = new dbConnection();
            Connection conn = dbConn.getConnection();

            try{
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("INSERT INTO subject(name) VALUES ('"+nameSQL+"')");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            name.setStyle("-fx-border-color: red");
            name.setPromptText("No name written!");
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    public void cancelButtonClick () {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
