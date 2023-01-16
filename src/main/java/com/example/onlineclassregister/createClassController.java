package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createClassController {

    @FXML
    private TextField name;

    @FXML
    private TextField room;

    @FXML
    private TextField studentsCount;


    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox year;

    @FXML
    private ChoiceBox classTeacher;

    @FXML
    private Button cancelButton;

    private Map<String, Integer> subjectIds = new HashMap<>();
    private Map<String, Integer> teacherIds = new HashMap<>();
    public void initialize(){



        List<Teacher> teachersList = Teacher.getTeachers();
        for(Teacher t: teachersList)
        {
            classTeacher.getItems().add(t.fName+" "+t.lName);
            teacherIds.put(t.fName + " " + t.lName, t.userId);
        }

        for(int i=0; i<=12; i++)
            year.getItems().add(i);

    }

    public void addButtonClick(){

        String SQL="INSERT INTO class(name, year, classTeacherId, studentsCount, room) VALUES (";

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try{

            Statement stmt = conn.createStatement();
            boolean ok=false;
            if(name.getText().isEmpty()){
                ok=false;
                name.setStyle("-fx-border-color: red");
                name.setPromptText("Input a class name!");
            }else{
                SQL+="'"+name.getText()+"', ";
                ok=true;
            }

            if(year.getItems().isEmpty()){
                ok=false;
                year.setStyle("-fx-border-color: red");
            }else{
                SQL+=year.getValue()+", ";
                ok=true;
            }

            if(classTeacher.getItems().isEmpty()){
                ok=false;
                classTeacher.setStyle("-fx-border-color: red");

            }else{
                SQL+=teacherIds.get(classTeacher.getValue())+", ";
                ok=true;
            }

            if(studentsCount.getText().isEmpty()){
                ok=false;
                studentsCount.setStyle("-fx-border-color: red");
                studentsCount.setPromptText("Input the students count!");
            }else{
                SQL+=studentsCount.getText()+", ";
                ok=true;
            }

            if(room.getText().isEmpty()){
                ok=false;
                room.setStyle("-fx-border-color: red");
                room.setPromptText("Input a room!");
            }else{
                SQL+="'"+room.getText()+"'); ";
                ok=true;
            }

            if(ok==true)
                stmt.executeUpdate(SQL);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    public void cancelButtonClick(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
