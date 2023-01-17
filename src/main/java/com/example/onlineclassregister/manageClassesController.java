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
import java.util.List;
import java.util.Map;

public class manageClassesController {


    @FXML
    private ListView<Integer> id;

    @FXML
    private ListView<String> name;

    @FXML
    private ListView<String> classTeacher;

    @FXML
    private ListView<String> room;

    @FXML
    private ListView<Integer> studentsCount;

    @FXML
    private ListView<Button> edit;


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


    private final ObservableList<Class> dataList = FXCollections.observableArrayList();

    private void filterClasses(String filter) throws SQLException {
        List<Class> classList = Class.getAllClassesList();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();

        // Create new lists to hold filtered data
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> classTeachers = FXCollections.observableArrayList();
        ObservableList<String> rooms = FXCollections.observableArrayList();
        ObservableList<Integer> studentCounts = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Class c : classList) {
            if (c.name.toLowerCase().contains(filter.toLowerCase())) {
                List<Teacher> teachers = Teacher.getTeachers();

                ids.add(c.id);
                names.add(c.name);

                for (Teacher t : teachers) {
                    if (t.userId == c.classTeacherId) {
                        classTeachers.add(t.fName + " " + t.lName);
                    }
                }

                //if no match found
                if(classTeachers.size() != ids.size()){
                    classTeachers.add("-");
                }
                rooms.add(c.room);
                studentCounts.add(c.studentsCount);

                if (c != null) {
                    Button b = new Button();
                    b.setText("Edit");
                    b.setUserData(c);
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

        // Set the filtered data to the ListViews
        Platform.runLater(() -> {
            id.setItems(ids);
            name.setItems(names);
            classTeacher.setItems(classTeachers);
            room.setItems(rooms);
            studentsCount.setItems(studentCounts);
            edit.setItems(edits);
        });

    }

    @FXML
    private void initialize() throws SQLException {

        List<Class> classList = Class.getAllClassesList();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();

        id.setFixedCellSize(30);
        name.setFixedCellSize(30);
        classTeacher.setFixedCellSize(30);
        studentsCount.setFixedCellSize(30);
        room.setFixedCellSize(30);
        edit.setFixedCellSize(30);

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> classTeachers = FXCollections.observableArrayList();
        ObservableList<String> rooms = FXCollections.observableArrayList();
        ObservableList<Integer> studentCounts = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Class c : classList) {
            System.out.println(c.getAverage());

            List<Teacher> teachers = Teacher.getTeachers();

            ids.add(c.id);
            names.add(c.name);

            boolean ok1=false;

            for(Teacher t: teachers)
                if(t.userId==c.classTeacherId){
                    ok1=true;
                    classTeachers.add(t.fName+" "+t.lName);
                }

            if(!ok1)
                classTeachers.add("-");

            rooms.add(c.room);
            studentCounts.add(c.studentsCount);

            if(c!=null) {
                Button b = new Button();
                b.setText("Edit");
                b.setUserData(c);
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

        id.setItems(ids);
        name.setItems(names);
        classTeacher.setItems(classTeachers);
        studentsCount.setItems(studentCounts);
        room.setItems(rooms);
        edit.setItems(edits);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a new thread to perform the filtering
            Thread thread = new Thread(() -> {
                try {
                    filterClasses(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        });
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
