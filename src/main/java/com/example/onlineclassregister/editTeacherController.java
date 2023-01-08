package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class editTeacherController {


    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    private Teacher teacher;

    @FXML
    private Text title;

    @FXML
    private TextField fName;

    @FXML
    private TextField email;
    @FXML
    private TextField lName;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<Class> classTeacher;

    @FXML
    private ComboBox<Subject> subject;

    @FXML
    private ListView<Class> classes;

    @FXML
    private ListView<CheckBox> checkBoxList;

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

        String SQL= "Delete from users where id= "+ teacher.userId+";";
      //  String SQL2= "DROP table teacher"+teacher.userId+";";

        dbConnection dbConn =new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
         //   stmt.executeUpdate(SQL2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }



    @FXML
    public void initialize(Teacher t){
        title.setText("You'll now be editing "+t.fName+ " "+ t.lName);
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);
    }

    public void setTeacher(Teacher s) {
        this.teacher = s;
    }
    public void addButtonClick(ActionEvent actionEvent) {

        String updateStmt = "UPDATE users SET ";
        boolean ok=false;

        if(!fName.getText().isEmpty()) {
            ok=true;
            updateStmt += "fName = '" + fName.getText() + "', ";
        }
        if(!lName.getText().isEmpty()) {
            ok=true;
            updateStmt += "lName = '" + lName.getText() + "', ";
        }
        if(!email.getText().isEmpty() && emailOk(email.getText())) {
            ok=true;
            updateStmt += "email = '" + email.getText() + "', ";
        }
        else if(!email.getText().isEmpty())
            email.setPromptText("Please provide a correct email!");

        if(!password.getText().isEmpty()) {
            ok=true;
            updateStmt += "password = '" + password.getText() + "', ";
        }
        // Remove trailing comma
        updateStmt = updateStmt.substring(0, updateStmt.length() - 2);
        // Add WHERE clause to update specific record
        updateStmt += " WHERE id = " + teacher.userId + ";";

        Boolean ok2=false;
        String updateStmt2="UPDATE teacher SET";

        if(classTeacher.getValue() != null) {
            ok2=true;
            updateStmt2 += "classTeacherId = '" + classTeacher.getValue().id + "', ";
        }
        if(subject.getValue() != null) {
            ok2=true;
            updateStmt2 += "subjectId = '" + subject.getValue().id + "', ";
        }

        // Remove trailing comma
        updateStmt2 = updateStmt.substring(0, updateStmt.length() - 2);
        // Add WHERE clause to update specific record
        updateStmt2 += " WHERE userId = " + teacher.userId + ";";


        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try {
            Statement stmt = conn.createStatement();
            if(ok)
            stmt.executeUpdate(updateStmt);
            if(ok2)
            stmt.executeUpdate(updateStmt2);

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
