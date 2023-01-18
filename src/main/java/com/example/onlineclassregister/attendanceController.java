package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class attendanceController {

    public int classId;

    @FXML
    private Button exitButton;
    @FXML
    private TableView<regEntry> attendanceTable;

    @FXML
    private Button chooseStudentBtn;

    public   Map<Integer, String> studentsNames = new HashMap<>();
    ObservableList<regEntry> entries = FXCollections.observableArrayList();
    Student activeStudent;


    @FXML
    private ComboBox<String> chooseStudent;

    @FXML
    private void chooseStudentBtnClick(){

        int studId=-1;
        if(!chooseStudent.getValue().isEmpty()){

            for(Integer i: studentsNames.keySet())
                if(studentsNames.get(i).equals(chooseStudent.getValue()))
                {
                    studId=i;
                    break;
                }

            activeStudent = null;


            List<Student> studentList = Student.getStudents();

            for(Student s: studentList)
                if(s.userId == studId){
                    activeStudent =s;
                    for(regEntry ren: s.regEntries)
                        if(ren.value==0)
                        entries.add(ren);

                    break;
                }


            Map<Integer, String> subjNames=Subject.initSubject();

            for(regEntry r: entries) {
                r.subject = subjNames.get(r.subjectId);
                if(r.getMotivated()==false)
                    r.motivateStatus="Not Motivated";
                else
                    r.motivateStatus="Motivated";

                r.motivate = new Button();
                r.motivate.setUserData(r);
                r.motivate.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
                r.motivate.setPrefWidth(84);
                r.motivate.setPrefHeight(23);

                if(r.getMotivated()==false)
                r.motivate.setText("Motivate!");
                else
                    r.motivate.setText("De-Motivate!");
                r.motivate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        motivateButtonClick(r.motivate);
                    }
                });
            }

            attendanceTable.setItems(entries);

        }
        else{
            chooseStudent.setPromptText("Choose a student!");
        }

    }

    private void motivateButtonClick(Button motivate) {

        int motivated = -1;
        regEntry re = (regEntry) motivate.getUserData();
        for(regEntry reg: entries)
            if(reg.value==0 && reg.date==re.date && reg.subjectId == re.subjectId)
            {
                reg.setMotivated(!reg.getMotivated());
                if(reg.getMotivated()==false)
                    motivated=0;
                else
                    motivated=1;
            }

        String SQL="UPDATE register"+activeStudent.userId+" SET motivated="+motivated+" WHERE subjectId="+re.subjectId+";";
        dbConnection dbConn = new dbConnection();
        Connection connection = dbConn.getConnection();

        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        entries.clear();

        List<Student> studentList = Student.getStudents();
        for(Student s: studentList)
            if(s.userId == activeStudent.userId){
                for(regEntry ren: s.regEntries)
                    if(ren.value==0)
                        entries.add(ren);

                break;
            }

        Map<Integer, String> subjNames=Subject.initSubject();

        for(regEntry r: entries) {
            r.subject = subjNames.get(r.subjectId);
            if(r.getMotivated()==false)
                r.motivateStatus="Not Motivated";
            else
                r.motivateStatus="Motivated";

            r.motivate = new Button();
            r.motivate.setUserData(r);
            r.motivate.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
            r.motivate.setPrefWidth(84);
            r.motivate.setPrefHeight(23);

            if(r.getMotivated()==false)
                r.motivate.setText("Motivate!");
            else
                r.motivate.setText("De-Motivate!");
            r.motivate.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    motivateButtonClick(r.motivate);
                }
            });
        }

        attendanceTable.setItems(entries);
    }


    public void setId(int classId1) {
        this.classId = classId1;
    }


    public void initialize(int classId1){

        System.out.println(classId);
        List<Student> students = Student.getStudents();

        for(Student s: students)
            if(s.classId == classId)
            {
                chooseStudent.getItems().add(s.fName+" "+s.lName);
                studentsNames.put(s.userId, s.fName+" "+s.lName);
            }

    }

    public void exitButton(ActionEvent actionEvent) {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
