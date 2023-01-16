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
import java.util.List;

public class editClassController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    private Class currentClass;

    @FXML
    private Text title;

    @FXML
    private TextField name;

    @FXML
    private TextField studentsCount;

    @FXML
    private TextField room;

    @FXML
    private ComboBox<String> classTeacher;

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

        String SQL= "Delete from class where id= "+ currentClass.id+";";

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
    public void initialize(Class c){
        title.setText("You'll now be editing "+c.name);
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);

        List<Teacher> teachers = Teacher.getTeachers();
        for(Teacher t: teachers)
            classTeacher.getItems().add(t.fName+" "+t.lName);
    }

    public void setStudent(Class c) {
        this.currentClass = c;
    }
    public void addButtonClick() {

      String nameSQL = name.getText();
      Integer studentsCountSQL = null;
      if(!studentsCount.getText().isEmpty())
       studentsCountSQL = Integer.valueOf(studentsCount.getText());

      String roomSQL = room.getText();

      Integer classTeacherIdSQL = null;
      if(classTeacher.getValue() != null)
          for(Teacher t: Teacher.getTeachers())
              if(classTeacher.getValue().equals(t.fName+" "+t.lName))
       classTeacherIdSQL = t.userId;


        String updateStmt ="UPDATE class SET ";

        if(!name.getText().isEmpty()) {
            updateStmt += "name = '" + nameSQL + "', ";
        }
        if(!studentsCount.getText().isEmpty()) {
            updateStmt += "studentsCount = '" + studentsCountSQL + "', ";
        }

        if(!room.getText().isEmpty()) {
            updateStmt += "room = '" + roomSQL + "', ";
        }

        if(classTeacher.getValue()!=null) {
            updateStmt += "classTeacherId = '" + classTeacherIdSQL + "', ";
        }


        // Remove trailing comma
        updateStmt = updateStmt.substring(0, updateStmt.length() - 2);
        // Add WHERE clause to update specific record
        updateStmt += " WHERE id = "+ currentClass.id + ";";

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

    public void cancelButtonClick( ) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
