package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class registerStudentController {

    @FXML
    private ListView<String> subjectList;

    @FXML
    private ListView<Button> buttonList;

    @FXML
    private ListView<String> gradesList;

    @FXML
    private ListView<String> attendanceList;

    @FXML
    private Button exitButton;

    @FXML
    private Label dateBar;

    @FXML
    private Text average;

    Student activeStudent = null;

    @FXML
    public void initialize(){

        subjectList.setFixedCellSize(30);
        buttonList.setFixedCellSize(30);
        gradesList.setFixedCellSize(30);
        attendanceList.setFixedCellSize(30);

        average.setText("");

        List<SchoolPerson> users = SchoolPerson.getUsers();
        SchoolPerson user = null;

        for(SchoolPerson p: users)
            if(p.userId==loggedUser.userId)
                user=p;

        if(user != null)
        dateBar.setText("Logged in as "+ user.mail);


        Map<Integer, String> subjectNames = Subject.initSubject();
        List<regEntry> regEntries = new ArrayList<>();
        List<Student> students = Student.getStudents();



        for(Student s: students)
            if(s.userId==loggedUser.userId)
                activeStudent=s;

        if(activeStudent!=null)
        regEntries = activeStudent.regEntries;


        if(!regEntries.isEmpty()) {
            for (Teacher t: Teacher.getTeachers())
            if(t.ClassesId.contains(activeStudent.classId))
            {

                subjectList.getItems().add(subjectNames.get(t.subjectId));

            Button b = new Button();
            b.setText("Choose");
            b.setUserData(t.subjectId);
                b.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
                b.setPrefWidth(84);
                b.setPrefHeight(23);

            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editButtonClick(b);
                }
            });

            buttonList.getItems().add(b);

        }


        }



    }

    private void editButtonClick(Button b) {

        gradesList.getItems().clear();
        attendanceList.getItems().clear();

        double avg=0;
        double avgCount=0;

        Map<Integer, String> subjectNames = Subject.initSubject();

        int courseId = (int) b.getUserData();

        List<regEntry> regEntries = activeStudent.regEntries;

        for(regEntry r: regEntries)
            if(r.value!=0 && r.subjectId==courseId) {

                String subject= subjectNames.get(r.subjectId);
                double grade = r.getValue();
                avg+=grade;
                avgCount++;
                String date = String.valueOf(r.date);
                gradesList.getItems().add(subject+": "+grade+" / "+date);

            }
        else if(r.value==0 && r.subjectId==courseId)
                {
                    String subject= subjectNames.get(r.subjectId);
                    boolean motivated = r.getMotivated();
                    String date = String.valueOf(r.date);

                    if(motivated==false)
                        attendanceList.getItems().add(subject+": Absence / "+date);
                    else
                        attendanceList.getItems().add(subject+": Absence / "+date+" - Motivated ");
            }

            average.setText("Current average: "+avg/avgCount);


    }

    public void exitButton() {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();

    }
}
