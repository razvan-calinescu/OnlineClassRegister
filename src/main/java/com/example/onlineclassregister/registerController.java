package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.onlineclassregister.Student.getStudents;
import static com.example.onlineclassregister.Teacher.getTeachers;

public class registerController {


    @FXML
    private Label dateBar;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> classesList;

    @FXML
    private ListView<String> studentsList;

    @FXML
    private TextField classId;

    @FXML
    private TextField studentId;

    @FXML
    private Text studentsListHead;

    @FXML
    private Text currentSituationHead;

    @FXML
    private Button classGet;

    @FXML
    private Button studentGet;

    @FXML
    private ListView<String> gradesList;

    @FXML
    private ListView<String> absenceList;

    @FXML
    private Text studentAlert;

    public void addGradeClick(){

        if(gradedUser.id==-1)
            studentAlert.setFill(Paint.valueOf("Red"));
        else
            studentAlert.setFill(Paint.valueOf("White"));



    }


    public void getClassById() throws SQLException {

        int id = Integer.parseInt(classId.getText());
        Map<Integer, String> classes= Class.getAllClassesMap();

        if(classes.entrySet().size()>0)
        {
            studentsListHead.setText("Students - Class "+Class.getAllClassesMap().get(id));
            List<Student> students = Student.getStudents();

            for(Student s: students)
                if(s.classId == id)
                    studentsList.getItems().add(s.fName + " " + s.lName + " - id: " + s.userId );
        }
        else
        {
            studentsListHead.setText("Class Inexistent");
            studentsList.getItems().add("List Empty");
        }


    }

    public void getStudentById() throws SQLException{

        gradedUser.id=-1;
        gradedUser.studentId=-1;
        gradedUser.grade=-1;

        int id= Integer.parseInt(studentId.getText());
        List<Student> students= Student.getStudents();

        Student currentStudent;

        for(Student s: students)
            if(s.userId==id) {
                currentSituationHead.setText("Current Situation for " + s.fName + " " + s.lName);
                currentStudent=s;

                gradedUser.id=s.userId;
                gradedUser.studentId=s.studId;

                String SQL="Select * from register"+currentStudent.userId+" WHERE teacherId="+loggedUser.userId;

                Map<Integer, String> subjectIdName = new HashMap<>();
                subjectIdName = Subject.initSubject();

                try{

                    gradesList.getItems().clear();
                    absenceList.getItems().clear();

                    dbConnection dbConn = new dbConnection();
                    Connection conn = dbConn.getConnection();

                    Statement stmt= conn.createStatement();
                    ResultSet res= stmt.executeQuery(SQL);

                    String rowGrades="", rowAbsence="";
                    while(res.next()){
                        rowGrades="";
                        rowAbsence="";

                        if(res.getInt("isGrade")==1) {
                            rowGrades += subjectIdName.get(res.getInt("subjectId")) + ": " + res.getInt("gradeValue") + " / " + res.getDate("date");
                            gradesList.getItems().add(rowGrades);
                        }

                        if(res.getInt("isAbsence")==1){
                            rowAbsence+=subjectIdName.get(res.getInt("subjectId"))+ ": Absent" + " / " + res.getDate("date");
                            if(res.getInt("motivated")==1)
                                rowAbsence+=" - Motivated";

                            absenceList.getItems().add(rowAbsence);

                        }
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            else {
                gradesList.getItems().clear();
                absenceList.getItems().clear();
                currentSituationHead.setText("User not found");
            }


    }


    public void registerView() throws IOException {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ClassRegister | Register");
        stage.setScene(scene);
        stage.show();

    }

    public void initialize() {

        Teacher teacher = null;

        studentAlert.setFill(Paint.valueOf("White"));

        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        teachers=getTeachers();
        if(teachers.size()==0)
            throw new RuntimeException("Teachers list empty;");

        students=getStudents();
        if(students.size()==0)
            throw new RuntimeException("Students list empty;");

        for(Teacher t: teachers)
            if(loggedUser.userId==t.userId )
            {
                teacher=t;
                break;
            }


        dateBar.setText("Currently logged-in: "+ teacher.mail);

        try {
            Class.allClassesName = Class.getAllClassesMap();
        }   catch (SQLException e){
            System.out.println("SQL EXCEPTION WHILE FETCHING ALL CLASSES");
            e.printStackTrace();
        }

        for(Integer classId: teacher.ClassesId)
            classesList.getItems().add(Class.allClassesName.get(classId) + " - id: "+ classId);





    }

    public void exitButton(ActionEvent e){

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}