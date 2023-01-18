package com.example.onlineclassregister;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class manageTeachersController {



    @FXML
    private Button exitButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button addStudent;

    @FXML
    private Button editStudent;

    @FXML
    private Button removeStudent;

    @FXML
    private TextField filterField;

    @FXML
    private TableView<Teacher> teachersTable;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws SQLException {

        Map<Integer, String> classNames= Class.getAllClassesMap();
        Map<Integer, String> subjectNames= Subject.initSubject();

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            Thread thread = new Thread(() -> {
                // Use the newValue from the filterField to filter the data in the table
                List<Teacher> filteredTeachers = teachers.stream()
                        .filter(teacher -> teacher.fullName.contains(newValue))
                        .collect(Collectors.toList());

                // Use Platform.runLater() to schedule the update on the JavaFX Application Thread
                Platform.runLater(() -> {
                    teachersTable.setItems(FXCollections.observableArrayList(filteredTeachers));
                });
            });
            thread.start();
        });



        for(Teacher t: Teacher.getTeachers()) {
            t.fullName=t.fName+" "+t.lName;
            t.email2=t.mail;
            t.subjectName= subjectNames.get(t.subjectId);
            t.classTeacherName = classNames.get(t.classTeacherId);
            t.editBtn = new Button();
            t.editBtn.setText("Edit");
            t.editBtn.setUserData(t);
            t.editBtn.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
            t.editBtn.setPrefWidth(64);
            t.editBtn.setPrefHeight(23);
            t.editBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        editButtonClick(t.editBtn);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            teachers.add(t);
        }

        teachersTable.setItems(teachers);

    }

    private void editButtonClick(Button b) throws IOException, SQLException {
        Teacher t = (Teacher) b.getUserData();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editTeacher.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

       editTeacherController controller = fxmlLoader.getController();

       controller.setTeacher(t);
       controller.initialize(t);

        stage.showAndWait();

        teachersTable.getItems().clear();
       initialize();
    }

    @FXML
    private void goHome()  {

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

    public void exitButton() {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();
    }


    public void addStudentClick() throws IOException, SQLException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createTeacher.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();

        teachersTable.getItems().clear();
       initialize();
    }
}
