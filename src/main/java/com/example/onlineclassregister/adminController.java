package com.example.onlineclassregister;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlineclassregister.Student.getStudents;
import static com.example.onlineclassregister.Teacher.getTeachers;

public class adminController {

    @FXML
    private Label titleAdmin;

    @FXML
    private Label role;

    @FXML
    private Label fName;

    @FXML
    private Label lName;

    @FXML
    private Label mail;

    @FXML
    private Label phone;

    @FXML
    private Label dateBar;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> classesList;

    public void initialize() {

        Teacher admin = null;

        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        teachers=getTeachers();
        if(teachers.size()==0)
            throw new RuntimeException("Teachers list empty;");

        students=getStudents();
        if(students.size()==0)
            throw new RuntimeException("Students list empty;");

        for(Teacher t: teachers)
            if(loggedUser.userId==t.userId)
            {
                admin=t;
                break;
            }


        titleAdmin.setText("Hello "+ admin.fName + ", this is your main page: ");
        dateBar.setText("Currently logged-in: "+ admin.mail);

        ///personal details
        role.setText("Role: "+loggedUser.getRole(admin));
        fName.setText("First name: "+admin.fName);
        lName.setText("Last name: "+admin.lName);
        mail.setText("E-mail address: "+admin.mail);
        phone.setText("Phone number: "+admin.phone);

        for(Integer classId: admin.ClassesId){
            String name=admin.classesName.get(classId);
            classesList.getItems().add(name);
        }


    }

    public void exitButton(ActionEvent e){

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}
