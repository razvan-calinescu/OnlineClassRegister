package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void initialize(Student s) throws SQLException {
        Map<Integer, String> classes = Class.getAllClassesMap();
        title.setText("You'll now be editing "+s.fName+ " "+ s.lName);
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);



        for(String classNames: classes.values())
            className.getItems().add(classNames);
    }

    public void setStudent(Student s) {
        this.student = s;
    }
    public void addButtonClick(ActionEvent actionEvent) throws SQLException {

        boolean stmt1=false;
        String firstName = fName.getText();
        String lastName = lName.getText();
        String dob1 = null;
        String email1 = email.getText();
        String password1 = password.getText();

        String updateStmt ="UPDATE users SET ";

        if(!fName.getText().isEmpty()) {
            stmt1=true;
            updateStmt += "fName = '" + firstName + "', ";
        }
        if(!lName.getText().isEmpty()) {
            stmt1=true;
            updateStmt += "lName = '" + lastName + "', ";
        }
        if(!email1.isEmpty() && emailOk(email1)) {
            stmt1=true;
            updateStmt += "mail = '" + email1 +"', ";
        }
        if(!password1.isEmpty()) {
            stmt1=true;
            md5 md5Helper = new md5();
            password1=md5Helper.getMd5(password1);
            updateStmt += "password = '" + password1 + "', ";
        }
        // Remove trailing comma
        if(stmt1) {
            updateStmt = updateStmt.substring(0, updateStmt.length() - 2);
            // Add WHERE clause to update specific record
            updateStmt += " WHERE id = " + student.userId + ";";
        }

        Map<Integer, String> classNames= Class.getAllClassesMap();

        String updateStmt2="";
        boolean stmt2=false;
        if(className.getValue() != null)
        {
            stmt2=true;
            int classId=-1;

            for(Integer key: classNames.keySet())
                if(classNames.get(key).equals(className.getValue()))
                {
                    classId = key;
                    break;
                }

            updateStmt2="UPDATE student SET classId="+classId+" where userId="+ student.userId+";";
        }


        String updateStmt3="UPDATE users SET ";
        boolean stmt3=false;

        String parentFnameS = parentFName.getText();
        String parentLnameS= parentLName.getText();
        String parentEmailS = parentEmail.getText();
        String parentPhoneS = parentPhone.getText();

        if(!parentFName.getText().isEmpty()){
            updateStmt3+="fName= '"+parentFnameS+"', ";
            stmt3=true;
        }
        if(!parentLName.getText().isEmpty()){
            updateStmt3+="lName= '"+parentLnameS+"', ";
            stmt3=true;
        }
        if(!parentEmail.getText().isEmpty()){
            updateStmt3+="mail= '"+parentEmailS+"', ";
            stmt3=true;
        }
        if(!parentPhone.getText().isEmpty()){
            updateStmt3+="phone= '"+parentPhoneS+"', ";
            stmt3=true;
        }

        if(stmt3) {
            // Remove trailing comma
            updateStmt3 = updateStmt3.substring(0, updateStmt3.length() - 2);
            // Add WHERE clause to update specific record
            updateStmt3 += " WHERE id = " + student.parent1Id + ";";
        }


        System.out.println(updateStmt3);

        dbConnection dbConn = new dbConnection();
        Connection conn= dbConn.getConnection();

        try{
            Statement stmt=conn.createStatement();
            if(stmt1)
            stmt.executeUpdate(updateStmt);

            if(stmt2)
                stmt.executeUpdate(updateStmt2);

            if(stmt3)
                stmt.executeUpdate(updateStmt3);

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
