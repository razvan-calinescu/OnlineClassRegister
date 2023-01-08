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
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class manageTeachersController {


    @FXML
    private ListView<Integer> id;

    @FXML
    private ListView<String> name;

    @FXML
    private ListView<String> email;

    @FXML
    private ListView<String> subject;

    @FXML
    private ListView<String> className;

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


    private final ObservableList<Teacher> dataList = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws SQLException {

        List<Teacher> teacherList = Teacher.getTeachers();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();
        Map<Integer, String> subjectNames = Subject.initSubject();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> emails = FXCollections.observableArrayList();
        ObservableList<String> subjects = FXCollections.observableArrayList();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Teacher t : teacherList) {
            ids.add(t.userId);
            if(t.fName==null)
                names.add("Account Inactive");
            else
                names.add(t.fName + " " + t.lName);

            if(t.mail==null)
                emails.add("Account Inactive");
            else
                emails.add(t.mail);

            if(t.subjectId!=-1)
                subjects.add(subjectNames.get(t.subjectId));
            else
                subjects.add("-");


            if(t.classTeacherId==0)
                classNames.add(" - ");
            else
                classNames.add(classNamesMap.get(t.classTeacherId));

            if(t!=null) {
                Button b = new Button();
                b.setText("Edit");
                b.setUserData(t);
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
        email.setItems(emails);
        subject.setItems(subjects);
        className.setItems(classNames);
        edit.setItems(edits);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // When the search string changes, update the list to only show items that match
            ObservableList<Integer> filteredIds = FXCollections.observableArrayList();
            ObservableList<String> filteredNames = FXCollections.observableArrayList();
            ObservableList<String> filteredEmails = FXCollections.observableArrayList();
            ObservableList<String> filteredSubjects = FXCollections.observableArrayList();
            ObservableList<String> filteredClassNames = FXCollections.observableArrayList();
            ObservableList<Button> filteredEdits = FXCollections.observableArrayList();

            for (int i = 0; i < ids.size(); i++) {
                String teacherName = names.get(i);
                if (teacherName.toLowerCase().contains(newValue.toLowerCase())) {
                    filteredIds.add(ids.get(i));
                    filteredNames.add(teacherName);
                    filteredEmails.add(emails.get(i));
                    filteredSubjects.add(subjects.get(i));
                    filteredClassNames.add(classNames.get(i));
                    filteredEdits.add(edits.get(i));
                }
            }

            id.setItems(filteredIds);
            name.setItems(filteredNames);
            email.setItems(filteredEmails);
            subject.setItems(filteredSubjects);
            className.setItems(filteredClassNames);
            edit.setItems(filteredEdits);
        });
    }

    private void editButtonClick(Button b) throws IOException, SQLException {
        Teacher t = (Teacher) b.getUserData();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editTeacher.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

       //////////////// editTeacherController controller = fxmlLoader.getController();

      ////////////////  controller.setStudent(t);
       /////////////// controller.initialize(t);

        stage.showAndWait();

        id.refresh();
        name.refresh();
        email.refresh();
        subject.refresh();
        className.refresh();
        edit.refresh();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> emails = FXCollections.observableArrayList();
        ObservableList<String> subjects = FXCollections.observableArrayList();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();


       List<Teacher> teachers = Teacher.getTeachers();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();
        Map<Integer, String> subjectNames = Subject.initSubject();


        for (Teacher te : teachers) {
            ids.add(te.userId);
            if(te.fName==null)
                names.add("Account Inactive");
            else
                names.add(te.fName + " " + te.lName);

            if(te.mail==null)
                emails.add("Account Inactive");
            else
                emails.add(te.mail);

            if(te.subjectId!=-1)
                subjects.add(subjectNames.get(te.subjectId));
            else
                subjects.add("-");


            if(te.classTeacherId==0)
                classNames.add(" - ");
            else
                classNames.add(classNamesMap.get(te.classTeacherId));


            if(te!=null) {
                Button bt = new Button();
                bt.setText("Edit");
                bt.setUserData(te);
                bt.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bt.setPrefWidth(54);
                bt.setPrefHeight(23);
                bt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            editButtonClick(bt);
                        } catch (IOException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                edits.add(bt);
            }


            id.setItems(ids);
            name.setItems(names);
            email.setItems(emails);
            subject.setItems(subjects);
            className.setItems(classNames);
            edit.setItems(edits);
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


    public void addStudentClick() throws IOException, SQLException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createTeacher.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();

        id.refresh();
        name.refresh();
        email.refresh();
        subject.refresh();
        className.refresh();
        edit.refresh();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> emails = FXCollections.observableArrayList();
        ObservableList<String> subjects = FXCollections.observableArrayList();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();


        List<Teacher> teacherList = Teacher.getTeachers();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();
        Map<Integer, String> subjectNames = Subject.initSubject();


        for (Teacher te : teacherList) {
            ids.add(te.userId);
            if(te.fName==null)
                names.add("Account Inactive");
            else
                names.add(te.fName + " " + te.lName);

            if(te.mail==null)
                emails.add("Account Inactive");
            else
                emails.add(te.mail);

            if(te.subjectId!=-1)
                subjects.add(subjectNames.get(te.subjectId));
            else
                subjects.add("-");


            if(te.classTeacherId==0)
                classNames.add(" - ");
            else
                classNames.add(classNamesMap.get(te.classTeacherId));

            if(te!=null) {
                Button bt = new Button();
                bt.setText("Edit");
                bt.setUserData(te);
                bt.setStyle("-fx-background-color: #66bbc4; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bt.setPrefWidth(54);
                bt.setPrefHeight(23);
                bt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            editButtonClick(bt);
                        } catch (IOException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                edits.add(bt);
            }


            id.setItems(ids);
            name.setItems(names);
            subject.setItems(subjects);
            className.setItems(classNames);
            edit.setItems(edits);

        }
    }
}
