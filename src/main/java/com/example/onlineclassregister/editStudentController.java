package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class editStudentController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    private Student student;

    @FXML
    private Text title;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private DatePicker dob;

    @FXML
    private Button deleteButton;

    @FXML
    private Button deleteConfirmButton;


    @FXML
    public void deleteButtonClick(){

        deleteConfirmButton.setDisable(false);
        deleteConfirmButton.setOpacity(100);
        deleteButton.setDisable(true);
        deleteButton.setOpacity(0);

    }

    @FXML
    public void deleteConfirmButton() {

        String SQL= "Delete from users where id= "+ student.userId+";";
        String SQL2= "DROP table register"+student.userId+";";

        dbConnection dbConn =new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            stmt.executeUpdate(SQL2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }



    @FXML
    public void initialize(Student s){
        title.setText("You'll now be editing "+s.fName+ " "+ s.lName);
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);
    }

    public void setStudent(Student s) {
        this.student = s;
    }
    public void addButtonClick(ActionEvent actionEvent) {

        String firstName = fName.getText();
        String lastName = lName.getText();
        String dob1 = null;
        if (dob.getValue() != null) {
            dob1 = dob.getValue().toString();
        }
        String email1 = email.getText();
        String password1 = password.getText();

        String updateStmt ="UPDATE users SET ";

        if(!fName.getText().isEmpty()) {
            updateStmt += "fName = '" + firstName + "', ";
        }
        if(!lName.getText().isEmpty()) {
            updateStmt += "lName = '" + lastName + "', ";
        }
        if(dob1 != null) {
            updateStmt += "birthDate = '" + dob1 + "', ";
        }
        if(!email1.isEmpty() && emailOk(email1)) {
            updateStmt += "mail = '" + email1 +"', ";
        }
        if(!password1.isEmpty()) {
            md5 md5Helper = new md5();
            password1=md5Helper.getMd5(password1);
            updateStmt += "password = '" + password1 + "', ";
        }
        // Remove trailing comma
        updateStmt = updateStmt.substring(0, updateStmt.length() - 2);
        // Add WHERE clause to update specific record
        updateStmt += " WHERE id = "+ student.userId + ";";

        dbConnection dbConn = new dbConnection();
        Connection conn= dbConn.getConnection();

        try{
            Statement stmt=conn.createStatement();
            stmt.executeUpdate(updateStmt);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    private boolean emailOk(String text) {

        boolean checkAt = false, checkDot = false;
        char auxChar;

        for (int i = 0; i < text.length(); i++)
            if (text.charAt(i) == '@')
                checkAt = true;
            else if (text.charAt(i) == '.' && checkAt == true)
                checkDot = true;

        if (checkAt && checkDot)
            return true;

        return false;

    }

    public void cancelButtonClick( ) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
