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

    @FXML
    private Button register;

    @FXML
    private Button studentsManager;

    @FXML
    private Button teachersManager;

    @FXML
    private Button classesManager;

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

        Teacher admin = null;

        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        teachers=getTeachers();
        students=getStudents();


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

    public void manageStudents() throws IOException {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("studentManager.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ClassRegister | Students Manager");
        stage.setScene(scene);
        stage.show();

    }

    public void manageClasses() throws IOException {
        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("classManager.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ClassRegister | Classes Manager");
        stage.setScene(scene);
        stage.show();
    }

    public void manageTeachers() throws IOException {
        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacherManager.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ClassRegister | Teachers Manager");
        stage.setScene(scene);
        stage.show();
    }
}
