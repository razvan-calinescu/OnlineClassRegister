package com.example.onlineclassregister;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class addAbsenceController {

    @FXML
    private Text title;

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

        gradedUser.absence=false;

        for(Student s: auxStudent)
            if(s.userId==gradedUser.id)
                activeStudent=s;

        title.setText("You'll now be grading " + activeStudent.fName + " " + activeStudent.lName+ " absent today: " );

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

        gradedUser.absence=true;


        Stage stageToClose = (Stage) addButton.getScene().getWindow();
        stageToClose.close();

    }

    @FXML
    private void cancelButtonClick(){

        gradedUser.absence=false;
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }
}
