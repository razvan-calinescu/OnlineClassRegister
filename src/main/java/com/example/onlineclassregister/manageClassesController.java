package com.example.onlineclassregister;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class manageClassesController {


    @FXML
    private TableView<Class> classesTable;


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


    private ObservableList<Class> classes = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws SQLException {


        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            Thread thread = new Thread(() -> {
                // Use the newValue from the filterField to filter the data in the table
                List<Class> filteredTeachers = classes.stream()
                        .filter(class1 -> class1.name.contains(newValue))
                        .collect(Collectors.toList());

                // Use Platform.runLater() to schedule the update on the JavaFX Application Thread
                Platform.runLater(() -> {
                    classesTable.setItems(FXCollections.observableArrayList(filteredTeachers));
                });
            });
            thread.start();
        });

        classes.clear();

        for (final Class s: Class.getAllClassesList()) {
            Thread thread = new Thread(() -> {

                for(Teacher t: Teacher.getTeachers())
                    if(t.userId==s.classTeacherId)
                    {
                        s.classTeacherName =  t.fName+ " "+ t.lName;
                        break;
                    }

                s.average=s.getAverage();


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
                    classes.add(s);
                });
            });
            thread.start();
        }

        classesTable.setItems(classes);
    }

    private void editButtonClick(Button b) throws IOException, SQLException {
        Class c = (Class) b.getUserData();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editClass.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        editClassController controller = fxmlLoader.getController();

        controller.setStudent(c);
      controller.initialize(c);

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


    public void addClassClick() throws IOException, SQLException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createClass.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();

        initialize();


    }
}
