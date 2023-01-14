package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class editSubjectController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    private Subject subject;

    @FXML
    private Text title;

    @FXML
    private TextField name;

    @FXML
    private Button deleteButton;

    @FXML
    private Button deleteConfirmButton;

    @FXML
    private ComboBox<String> className;

    @FXML
    private TextField parentFName;

    @FXML
    private TextField parentLName;

    @FXML
    private TextField parentEmail;

    @FXML
    private TextField parentPhone;


    @FXML
    public void deleteButtonClick(){

        deleteConfirmButton.setDisable(false);
        deleteConfirmButton.setOpacity(100);
        deleteButton.setDisable(true);
        deleteButton.setOpacity(0);

    }

    @FXML
    public void deleteConfirmButton() {

        String SQL= "Delete from subject where id= "+ subject.id+";";

        dbConnection dbConn =new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }



    @FXML
    public void initialize(Subject s) throws SQLException {
        Map<Integer, String> classes = Class.getAllClassesMap();
        title.setText("You'll now be editing "+s.name+" course");
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);

    }

    public void setSubject(Subject s) {
        this.subject = s;
    }
    public void addButtonClick(ActionEvent actionEvent) throws SQLException {
        String updatedName = name.getText();

        if(name.getText().isEmpty())
        {
            name.setStyle("-fx-border-color: red");
            name.setPromptText("No name typed");
        }
        else
        {
            dbConnection dbConn = new dbConnection();
            Connection conn = dbConn.getConnection();

            try{
                Statement stmt = conn.createStatement();
                String SQL="UPDATE subject SET name='"+updatedName+"' where id="+ subject.id+";";
                System.out.println("SQL "+SQL);
                stmt. executeUpdate(SQL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();

        }

    }


    public void cancelButtonClick( ) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
