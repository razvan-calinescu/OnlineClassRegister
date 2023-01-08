package com.example.onlineclassregister;

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

    @FXML
    private void initialize() throws SQLException {

        List<Class> classList = Class.getAllClassesList();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> classTeachers = FXCollections.observableArrayList();
        ObservableList<String> rooms = FXCollections.observableArrayList();
        ObservableList<Integer> studentCounts = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Class c : classList) {

            List<Teacher> teachers = Teacher.getTeachers();

            ids.add(c.id);
            names.add(c.name);

            for(Teacher t: teachers)
                if(t.userId==c.classTeacherId)
                    classTeachers.add(t.fName+" "+t.lName);

            rooms.add(c.room);

            if(c!=null) {
                Button b = new Button();
                b.setText("Edit");
                b.setUserData(c);
                b.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                b.setPrefWidth(54);
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
            // When the search string changes, update the list to only show items that match
            ObservableList<Integer> filteredIds = FXCollections.observableArrayList();
            ObservableList<String> filteredNames = FXCollections.observableArrayList();
            ObservableList<String> filteredClassTeachers = FXCollections.observableArrayList();
            ObservableList<Integer> filteredStudentsCount = FXCollections.observableArrayList();
            ObservableList<String> filteredRooms = FXCollections.observableArrayList();
            ObservableList<Button> filteredEdits = FXCollections.observableArrayList();

            for (int i = 0; i < ids.size(); i++) {
                String className = names.get(i);
                if (className.toLowerCase().contains(newValue.toLowerCase())) {
                    filteredIds.add(ids.get(i));
                    filteredNames.add(className);
                    filteredClassTeachers.add(classTeachers.get(i));
              //       filteredStudentsCount.add(studentCounts.get(i));
                    filteredRooms.add(rooms.get(i));
                    filteredEdits.add(edits.get(i));
                }
            }

            id.setItems(filteredIds);
            name.setItems(filteredNames);
            classTeacher.setItems(filteredClassTeachers);
            studentsCount.setItems(studentCounts);
            room.setItems(rooms);
            edit.setItems(filteredEdits);
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

        id.refresh();
        name.refresh();
        classTeacher.refresh();
        studentsCount.refresh();
        room.refresh();
        edit.refresh();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> classTeachers = FXCollections.observableArrayList();
        ObservableList<Integer> studentsCounts = FXCollections.observableArrayList();
        ObservableList<String> rooms = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();


        List<Class> classes = Class.getAllClassesList();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();


        for (Class cl : classes) {

            List<Teacher> teachers = Teacher.getTeachers();

            ids.add(cl.id);
            names.add(cl.name);

            for (Teacher t : teachers)
                if (t.userId == cl.classTeacherId)
                    classTeachers.add(t.fName + " " + t.lName);

            rooms.add(cl.room);

            if (cl != null) {
                Button bt = new Button();
                bt.setText("Edit");
                bt.setUserData(cl);
                bt.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bt.setPrefWidth(54);
                bt.setPrefHeight(23);
                bt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            editButtonClick(bt);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                edits.add(bt);
            }
        }
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

        id.refresh();
        name.refresh();
        classTeacher.refresh();
        studentsCount.refresh();
        room.refresh();
        edit.refresh();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> classTeachers = FXCollections.observableArrayList();
        ObservableList<Integer> studentsCounts = FXCollections.observableArrayList();
        ObservableList<String> rooms = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();


        List<Class> classes = Class.getAllClassesList();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();


        for (Class cl : classes) {

            List<Teacher> teachers = Teacher.getTeachers();

            ids.add(cl.id);
            names.add(cl.name);

            for (Teacher t : teachers)
                if (t.userId == cl.classTeacherId)
                    classTeachers.add(t.fName + " " + t.lName);

            rooms.add(cl.room);

            if (cl != null) {
                Button bt = new Button();
                bt.setText("Edit");
                bt.setUserData(cl);
                bt.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bt.setPrefWidth(54);
                bt.setPrefHeight(23);
                bt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            editButtonClick(bt);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                edits.add(bt);
            }
        }
    }
}
