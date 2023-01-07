package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class registerParentController {

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> studentsList;

    @FXML
    private ListView<Button> buttonStudents;

    @FXML
    private ListView<String> subjectList;

    @FXML
    private ListView<Button> buttonSubject;

    @FXML
    private Text average;

    @FXML
    private ListView<String> gradesList;

    @FXML
    private ListView<String> attendanceList;

    public void exitButton() {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();
    }

    public void initialize(){

        average.setText("");

        studentsList.setFixedCellSize(60);
        buttonStudents.setFixedCellSize(60);
        List<Student> students = Student.getStudents();

        for(Student s: students)
            if(s.parent1Id==loggedUser.userId)
            {
                studentsList.getItems().add(s.fName + " " + s.lName);

                Button b = new Button();
                b.setText("Choose");
                b.setUserData(s);
                b.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                b.setPrefWidth(54);
                b.setPrefHeight(23);

                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        editButtonClick(b);
                    }
                });

                buttonStudents.getItems().add(b);
            }

    }

    private void editButtonClick(Button b) {

        Student student = (Student) b.getUserData();
        subjectList.setFixedCellSize(60);
        buttonSubject.setFixedCellSize(60);

        Map<Integer, String> subjectNames = Subject.initSubject();
        List<Integer> attendedCourses = new ArrayList<>();


        String SQL= "Select * from student where userId="+student.userId+";";

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt= conn.createStatement();
            ResultSet res = stmt.executeQuery(SQL);

            while(res.next())
            {
                int coursesCount = res.getInt("coursesCount");
                int colIndex=res.findColumn("coursesCount");
                for(int i=1; i<=coursesCount; i++)
                    attendedCourses.add(res.getInt(colIndex+i));
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(!attendedCourses.isEmpty()) {
            for (Integer courseId : attendedCourses)
            {

                subjectList.getItems().add(subjectNames.get(courseId));

                Button bSubject = new Button();
                bSubject.setText("Choose");
                bSubject.setUserData(courseId);
                bSubject.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bSubject.setPrefWidth(54);
                bSubject.setPrefHeight(23);

                bSubject.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        editButtonClick1(bSubject);
                    }
                });

                buttonSubject.getItems().add(b);

            }


        }



    }

    private void editButtonClick1(Button b) {

        double avg=0;
        double avgCount=0;

        Map<Integer, String> subjectNames = Subject.initSubject();

        int courseId = (int) b.getUserData();

        String SQL = "SELECT * from register"+loggedUser.userId+";";

        dbConnection dbconn = new dbConnection();
        Connection conn = dbconn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(SQL);

            while(res.next()){
                if(res.getInt("isGrade")==1){

                    String subject= subjectNames.get(res.getInt("subjectId"));
                    double grade = res.getDouble("gradeValue");
                    avg+=grade;
                    avgCount++;
                    String date = String.valueOf(res.getDate("date"));
                    gradesList.getItems().add(subject+": "+grade+" / "+date);

                }
                else if(res.getInt("isAbsence")==1)
                {
                    String subject= subjectNames.get(res.getInt("subjectId"));
                    int motivated = res.getInt("motivated");
                    String date = String.valueOf(res.getDate("date"));

                    if(motivated==0)
                        attendanceList.getItems().add(subject+": Absence / "+date);
                    else
                        attendanceList.getItems().add(subject+": Absence / "+date+" - Motivated ");

                }



            }

            conn.close();
            average.setText("Current average: "+avg/avgCount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
