package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class addGradeController {

    @FXML
    private Text title;

    @FXML
    private TextField gradeField;

    @FXML
    private TextField datePicker;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField teacherField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private Student activeStudent;
    private Teacher activeTeacher;

    @FXML
    private void initialize(){

        List<Student> auxStudent= Student.getStudents();


        for(Student s: auxStudent)
            if(s.userId==gradedUser.id)
                activeStudent=s;

        title.setText("You'll now be adding a grade for " + activeStudent.fName + " " + activeStudent.lName );

        List<Teacher> auxTeachers= Teacher.getTeachers();
        for(Teacher t: auxTeachers)
            if(t.userId==loggedUser.userId)
                activeTeacher=t;

        teacherField.setText(activeTeacher.fName+ " "+activeTeacher.lName);

        Map<Integer, String> auxSubject = Subject.initSubject();
        subjectField.setText(auxSubject.get(activeTeacher.subjectId));

        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());

        datePicker.setText(formatter.format(date));
    }
    @FXML
    private void addButtonClick(){

        double grade = Double.parseDouble(gradeField.getText());


        String SQL = "INSERT INTO register2(studentId, classId, teacherId, subjectId, isGrade, date, gradeValue) VALUES ("+ activeStudent.userId + ", " + activeStudent.classId + ", "+ activeTeacher.teacherId + ", "+ activeTeacher.subjectId+", 1, CURRENT_DATE, "+ grade +" ) ";

        dbConnection dbConn = new dbConnection();
        Connection conn =dbConn.getConnection();

        try{

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        Stage stageToClose = (Stage) addButton.getScene().getWindow();
        stageToClose.close();

    }

    @FXML
    private void cancelButtonClick(){

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

}
