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
        System.out.println("parent id: "+ loggedUser.userId);

        studentsList.setFixedCellSize(30);
        buttonStudents.setFixedCellSize(30);
        List<Student> students = Student.getStudents();

        for(Student s: students)
            if(s.parent1Id==loggedUser.userId)
            {
                studentsList.getItems().add(s.fName + " " + s.lName);

                Button b = new Button();
                b.setText("Choose");
                b.setUserData(s);

                b.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
                b.setPrefWidth(64);
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

    private int chosenStudentId= -1;

    private void editButtonClick(Button b) {

        Student student = (Student) b.getUserData();
        System.out.println(student.userId);
        subjectList.setFixedCellSize(30);
        buttonSubject.setFixedCellSize(30);

        List<Student> students = new ArrayList<>();
        students=Student.getStudents();

        Student auxStudent = null;

        for(Student s1: students)
            if(s1.userId==student.userId)
                auxStudent =s1;

        chosenStudentId=auxStudent.userId;

        Map<Integer, String> subjectNames = Subject.initSubject();


        List<regEntry> registerEntries = new ArrayList<>();
        registerEntries=auxStudent.regEntries;

        subjectList.getItems().clear();

        for(regEntry re: registerEntries)
        {
            if(!subjectList.getItems().contains(subjectNames.get(re.subjectId))) {
                subjectList.getItems().add(subjectNames.get(re.subjectId));

                Button bSubject = new Button();
                bSubject.setText("Choose");
                bSubject.setUserData(re.subjectId);
                bSubject.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bSubject.setPrefWidth(54);
                bSubject.setPrefHeight(23);

                bSubject.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        editButtonClick1(bSubject);
                    }
                });

                buttonSubject.getItems().add(bSubject);
            }
        }
        }





    private void editButtonClick1(Button b) {

        double avg=0;
        double avgCount=0;

        Map<Integer, String> subjectNames = Subject.initSubject();

        int courseId = (int) b.getUserData();

        List<Student> students = new ArrayList<>();
        students= Student.getStudents();
        Student activeStudent = null;

        for(Student s2: students)
            if(s2.userId== chosenStudentId)
                activeStudent =s2;

        List<regEntry> regEntries = new ArrayList<>();
        regEntries=activeStudent.regEntries;

       gradesList.getItems().clear();
       attendanceList.getItems().clear();
       for(regEntry re: regEntries)
           if(re.getValue()!=0 && re.subjectId==courseId)
           {
               String subject= subjectNames.get(re.subjectId);
               double grade = re.getValue();
               avg+=grade;
               avgCount++;
               String date = String.valueOf(re.date);
               gradesList.getItems().add(subject+": "+grade+" / "+date);
           }
       else if(re.value==0 && re.subjectId==courseId){
               String subject= subjectNames.get(re.subjectId);
               boolean motivated = re.getMotivated();
               String date = String.valueOf(re.date);

               if(motivated==false)
                   attendanceList.getItems().add(subject+": Absence / "+date);
               else
                   attendanceList.getItems().add(subject+": Absence / "+date+" - Motivated ");

           }


            average.setText("Current average: "+avg/avgCount);


    }
}
