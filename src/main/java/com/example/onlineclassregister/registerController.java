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
import java.util.*;

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

    @FXML
    private Text studentAlert1;

    @FXML
    private Button addAbsence;

    @FXML
    private Button homeButton;

    @FXML
    private Button motivate;



    private int classId1;
    private Teacher loggedTeacher;



    private Student currentStudent;
    private Teacher currentTeacher;
    
    @FXML
    private void goHome(){

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();
        
        List<SchoolPerson> users= new ArrayList<>();
        users= SchoolPerson.getUsers();
        
        SchoolPerson user=null;
        
        for(SchoolPerson s: users)
            if(s.userId == loggedUser.userId)
            {
                user=s;
                break;
            }


        try{
            FXMLLoader fxmlLoader = null;
            if(user.isAdmin)
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("adminTeacher.fxml"));
            else if (user.role==1) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher.fxml"));
            } else if (user.role==2) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("student.fxml"));
                
            } else if(user.role==3){
               fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("parent.fxml"));
            }
            Scene scene = new Scene(fxmlLoader.load(), 850, 700);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show(); } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    @FXML
    private void addAbsenceClick() throws IOException {

        if(gradedUser.id==-1)
            studentAlert1.setFill(Paint.valueOf("Red"));
        else {
            studentAlert1.setFill(Paint.valueOf("White"));


            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addAbsence.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ClassRegister | Add Absence");
            stage.setScene(scene);
            stage.showAndWait();

            if (gradedUser.absence) {

                List<Teacher> aux = Teacher.getTeachers();

                for (Teacher t : aux)
                    if (t.userId == loggedUser.userId)
                        currentTeacher = t;


                String SQL = "INSERT INTO register"+gradedUser.id+"(studentId, classId, teacherId, subjectId, isAbsence, date) VALUES (" + currentStudent.userId + ", " + currentStudent.classId + ", " + currentTeacher.teacherId + ", " + currentTeacher.subjectId + ", 1, CURRENT_DATE ) ";

                dbConnection dbConn = new dbConnection();
                Connection conn = dbConn.getConnection();

                try {

                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(SQL);
                    getStudentById2();


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            }
        }

    }

    public void addGradeClick() throws IOException {

        if(gradedUser.id==-1)
            studentAlert.setFill(Paint.valueOf("Red"));
        else {
            studentAlert.setFill(Paint.valueOf("White"));


            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addGrade.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ClassRegister | Add Grade");
            stage.setScene(scene);
            stage.showAndWait();

            if (gradedUser.grade != -1) {

                List<Teacher> aux = Teacher.getTeachers();

                for (Teacher t : aux)
                    if (t.userId == loggedUser.userId)
                        currentTeacher = t;


                String SQL = "INSERT INTO register"+gradedUser.id+"(studentId, classId, teacherId, subjectId, isGrade, date, gradeValue) VALUES (" + currentStudent.userId + ", " + currentStudent.classId + ", " + currentTeacher.teacherId + ", " + currentTeacher.subjectId + ", 1, CURRENT_DATE, " + gradedUser.grade + " ) ";

                dbConnection dbConn = new dbConnection();
                Connection conn = dbConn.getConnection();

                try {

                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(SQL);
                    getStudentById2();


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }



            }
        }

    }


    public void getClassById() throws SQLException {

        motivate.setVisible(false);
        int id = -1;
        studentsList.getItems().clear();
        Map<Integer, String> classes= Class.getAllClassesMap();

        if(!classId.getText().isBlank() && Class.getAllClassesMap().containsKey(Integer.parseInt(classId.getText())));
        id=Integer.parseInt(classId.getText());



        if(classes.entrySet().size()>0 && id!=-1)
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
            studentsList.getItems().clear();
        }


        List<Teacher> teachers = Teacher.getTeachers();
        for(Teacher t: teachers)
            if(t.userId==loggedUser.userId && t.classTeacherId == id)
            {
                classId1 = id;
                motivate.setVisible(true);
            }
    }

    public void getStudentById() throws SQLException{

        gradedUser.id=-1;
        gradedUser.studentId=-1;
        gradedUser.grade=-1;
        gradedUser.absence=false;
        List<Student> students= Student.getStudents();

        int id=-1;

        if(!studentId.getText().isBlank())
         id=Integer.parseInt(studentId.getText());

    boolean userFound=false;

    for(Student s: students)
        if(s.userId==id)
            userFound=true;

    if(id!=-1 && userFound) {
        for (Student s : students)
            if (s.userId == id) {
                currentSituationHead.setText("Current Situation for " + s.fName + " " + s.lName);
                currentStudent = s;

                gradedUser.id = s.userId;
                gradedUser.studentId = s.studId;

                String SQL = "Select * from register" + gradedUser.id + " WHERE teacherId=" + loggedUser.userId;

                Map<Integer, String> subjectIdName = new HashMap<>();
                subjectIdName = Subject.initSubject();

                try {

                    gradesList.getItems().clear();
                    absenceList.getItems().clear();

                    dbConnection dbConn = new dbConnection();
                    Connection conn = dbConn.getConnection();

                    Statement stmt = conn.createStatement();
                    ResultSet res = stmt.executeQuery(SQL);

                    String rowGrades = "", rowAbsence = "";
                    while (res.next()) {
                        rowGrades = "";
                        rowAbsence = "";

                        if (res.getInt("isGrade" ) == 1 && res.getInt("teacherId")==loggedUser.userId) {
                            rowGrades += subjectIdName.get(res.getInt("subjectId")) + ": " + res.getInt("gradeValue") + " / " + res.getDate("date");
                            gradesList.getItems().add(rowGrades);
                        }

                        if (res.getInt("isAbsence") == 1 && res.getInt("teacherId")==loggedUser.userId) {
                            rowAbsence += subjectIdName.get(res.getInt("subjectId")) + ": Absent" + " / " + res.getDate("date");
                            if (res.getInt("motivated") == 1)
                                rowAbsence += " - Motivated";

                            absenceList.getItems().add(rowAbsence);

                        }
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
    }

            else
            {
                System.out.println("HEREEE1");
                gradesList.getItems().clear();
                absenceList.getItems().clear();
                currentSituationHead.setText("Student not found");
            }


    }

    public void getStudentById2() throws SQLException{

        List<Student> students =Student.getStudents();
        Map<Integer, String> courseNames = Subject.initSubject();

        absenceList.getItems().clear();
        gradesList.getItems().clear();

        for(Student s: students)
            if(s.userId == gradedUser.id)
            {
                String SQL = "Select * from register"+gradedUser.id+";";
                dbConnection dbConn = new dbConnection();
                Connection conn = dbConn.getConnection();

                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(SQL);

                while(res.next())
                {
                    if(res.getInt("isAbsence")==1 && res.getInt("teacherId")==loggedUser.userId)
                    {
                        String date = String.valueOf(res.getDate("date"));
                        int motivated = res.getInt("motivated");
                        int courseId = res.getInt("subjectId");

                        if(motivated==0)
                            absenceList.getItems().add(courseNames.get(courseId)+": "+date);
                        else
                            absenceList.getItems().add(courseNames.get(courseId)+": "+date+" - Motivated");

                    }
                    else if(res.getInt("isGrade")==1 && res.getInt("teacherId")==loggedUser.userId){
                        double grade = res.getDouble("gradeValue");
                        String date = String.valueOf(res.getDate("date"));
                        int courseId = res.getInt("subjectId");
                        gradesList.getItems().add(courseNames.get(courseId)+": "+grade+" / "+date);


                    }


                }

                conn.close();


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


        motivate.setVisible(false);

        studentAlert.setFill(Paint.valueOf("White"));
        studentAlert1.setFill(Paint.valueOf("White"));

        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        teachers=Teacher.getTeachers();
        students=Student.getStudents();


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

    public void motivateClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("attendance.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        attendanceController act= fxmlLoader.getController();
        act.setId(classId1);
        act.initialize(classId1);
        stage.showAndWait();

    }
}