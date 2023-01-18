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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class manageSubjectsController {

    @FXML
    private TableView<Subject> subjectsTable;


    @FXML
    private Button exitButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button addSubject;


    @FXML
    private TextField filterField;


    private ObservableList<Subject> subjects = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws SQLException {

        Map<Integer, String> classNames = Class.getAllClassesMap();
        Map<Integer, String> subjectNames = Subject.initSubject();

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            Thread thread = new Thread(() -> {
                // Use the newValue from the filterField to filter the data in the table
                List<Subject> filteredTeachers = subjects.stream()
                        .filter(subject -> subject.getName().contains(newValue))
                        .collect(Collectors.toList());

                // Use Platform.runLater() to schedule the update on the JavaFX Application Thread
                Platform.runLater(() -> {
                    subjectsTable.setItems(FXCollections.observableArrayList(filteredTeachers));
                });
            });
            thread.start();
        });

        subjects.clear();
        for (final Subject s : Subject.initSubjectList()) {
            Thread thread = new Thread(() -> {
                s.teachersString = "";
                for (Teacher t : Teacher.getTeachers()) {
                    if (t.subjectId == s.id) {
                        s.teachersString += t.fName + " " + t.lName + ", ";
                    }
                }
                if(s.teachersString.contains(","))
                    s.teachersString = s.teachersString.substring(0, s.teachersString.length() - 2);

                s.average = s.getAverage();

                s.edit = new Button();
                s.edit.setText("Edit");
                s.edit.setUserData(s);
                s.edit.setStyle("-fx-background-color: " + properties.mainColor + "; -fx-border-radius: 25px; -fx-background-radius: 25px");
                s.edit.setPrefWidth(64);
                s.edit.setPrefHeight(23);
                s.edit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            editButtonClick(s.edit);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                Platform.runLater(() -> {
                    subjects.add(s);
                });
            });
            thread.start();
        }

        subjectsTable.setItems(subjects);

    }


    private void editButtonClick(Button b) throws IOException, SQLException {
        Subject s = (Subject) b.getUserData();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editSubject.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        editSubjectController controller = fxmlLoader.getController();
        controller.setSubject(s);
        controller.initialize(s);

        stage.showAndWait();

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


    public void addSubjectClick() throws IOException, SQLException {


        loggedUser.fromCsv = 0;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addSubject.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();

        initialize();

    }

}
