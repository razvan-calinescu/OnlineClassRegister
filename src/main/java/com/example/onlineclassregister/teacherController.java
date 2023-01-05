package com.example.onlineclassregister;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlineclassregister.Student.getStudents;
import static com.example.onlineclassregister.Teacher.getTeachers;


public class teacherController {

    @FXML
    private Button exitButton;
    @FXML
    private Label titleAdmin;
    @FXML
    private Label dateBar;
    @FXML
    private Label role;

    @FXML
    private Label fName;

    @FXML
    private Label lName;

    @FXML
    private Label phone;

    @FXML
    private Label mail;

    @FXML
    private ListView<String> classesList;


    public void initialize() {

        Teacher teacher = null;

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
                teacher=t;
                break;
            }


        titleAdmin.setText("Hello "+ teacher.fName + ", this is your main page: ");
        dateBar.setText("Currently logged-in: "+ teacher.mail);

        ///personal details
        role.setText("Role: "+loggedUser.getRole(teacher));
        fName.setText("First name: "+teacher.fName);
        lName.setText("Last name: "+teacher.lName);
        mail.setText("E-mail address: "+teacher.mail);
        phone.setText("Phone number: "+teacher.phone);

        for(Integer classId: teacher.ClassesId){
            String name=teacher.classesName.get(classId);
            classesList.getItems().add(name);
        }


    }

    public void exitButton() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }

    public void registerView() {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();


        try{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ClassRegister | Register");
        stage.setScene(scene);
        stage.show(); } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void messagesView() {
    }
}
