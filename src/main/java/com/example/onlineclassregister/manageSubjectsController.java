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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class manageSubjectsController {

    @FXML
    private ListView<Integer> id;

    @FXML
    private ListView<String> name;

    @FXML
    private ListView<String> teacher;

    @FXML
    private ListView<Double> average;

    @FXML
    private ListView<Button> edit;


    @FXML
    private Button exitButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button addSubject;


    @FXML
    private TextField filterField;


    private final ObservableList<Student> dataList = FXCollections.observableArrayList();


    private void filterSubjects(String filter) {
        List<Subject> subjects = Subject.initSubjectList();

        // Create new lists to hold filtered data
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> teachers = FXCollections.observableArrayList();
        ObservableList<Double> averages = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Subject s : subjects) {
            if (s.name.toLowerCase().contains(filter.toLowerCase())) {
                ids.add(s.id);
                names.add(s.name);

                String teacherList = "";
                for (Teacher t : Teacher.getTeachers()) {
                    if (t.subjectId == s.id) {
                        teacherList += t.fName + " " + t.lName + ", ";
                    }
                }

                if (!teacherList.isEmpty()) {
                    teacherList = teacherList.substring(0, teacherList.length() - 2);
                }

                teachers.add(teacherList);
                averages.add(s.getAverage());

                if (s != null) {
                    Button b = new Button();
                    b.setText("Edit");
                    b.setUserData(s);
                    b.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                    b.setPrefWidth(44);
                    b.setPrefHeight(23);
                    b.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                editButtonClick(b);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    edits.add(b);
                }
            }
        }

        // Platform.runLater()
        Platform.runLater(() -> {
            id.setItems(ids);
            name.setItems(names);
            teacher.setItems(teachers);
            average.setItems(averages);
            edit.setItems(edits);
        });
    }

    @FXML
    private void initialize() throws SQLException {

        id.setFixedCellSize(30);
        name.setFixedCellSize(30);
        teacher.setFixedCellSize(30);
        average.setFixedCellSize(30);
        edit.setFixedCellSize(30);

        List<Student> studentList = Student.getStudents();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();
        List<Subject> subjects= Subject.initSubjectList();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> teachers = FXCollections.observableArrayList();
        ObservableList<Double> averages = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Subject s : subjects) {
            ids.add(s.id);
            names.add(s.name);

            for(int i = 0; i < Teacher.getTeachers().size(); i++) {
                Teacher t = Teacher.getTeachers().get(i);
                if(t.subjectId==s.id) {
                    if(i == Teacher.getTeachers().size() - 1) {
                        teachers.add(t.fName+ " "+ t.lName);
                    } else {
                        teachers.add(t.fName+ " "+ t.lName+", ");
                    }
                }
            }

            averages.add(s.getAverage());

            if(s!=null) {
                Button b = new Button();
                b.setText("Edit");
                b.setUserData(s);
                b.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                b.setPrefWidth(44);
                b.setPrefHeight(23);
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            editButtonClick(b);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                edits.add(b);
            }

            ///Thread for filtering
            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                Thread thread = new Thread(() -> {
                    filterSubjects(newValue);
                });
                thread.start();
            });

        }

        id.setItems(ids);
        name.setItems(names);
        teacher.setItems(teachers);
        average.setItems(averages);
        edit.setItems(edits);


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
