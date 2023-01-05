package com.example.onlineclassregister;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class manageStudentsController {

    @FXML
    private ListView<Integer> id;

    @FXML
    private ListView<String> name;

    @FXML
    private ListView<String> email;

    @FXML
    private ListView<String> parent;

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


    private final ObservableList<Student> dataList = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws SQLException {

        List<Student> studentList = Student.getStudents();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> emails = FXCollections.observableArrayList();
        ObservableList<String> parents = FXCollections.observableArrayList();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();

        for (Student s : studentList) {
            ids.add(s.userId);
            if(s.fName==null)
                names.add("Account Inactive");
            else
                names.add(s.fName + " " + s.lName);

            if(s.mail==null)
                emails.add("Account Inactive");
            else
                emails.add(s.mail);

            if(s.classId==0)
                classNames.add(" - ");
            else
                classNames.add(classNamesMap.get(s.classId));

            if(s!=null) {
                Button b = new Button();
                b.setText("Edit");
                b.setUserData(s);
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
// parent.setItems(parents);
        className.setItems(classNames);
        edit.setItems(edits);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // When the search string changes, update the list to only show items that match
            ObservableList<Integer> filteredIds = FXCollections.observableArrayList();
            ObservableList<String> filteredNames = FXCollections.observableArrayList();
            ObservableList<String> filteredEmails = FXCollections.observableArrayList();
            ObservableList<String> filteredParents = FXCollections.observableArrayList();
            ObservableList<String> filteredClassNames = FXCollections.observableArrayList();
            ObservableList<Button> filteredEdits = FXCollections.observableArrayList();

            for (int i = 0; i < ids.size(); i++) {
                String studentName = names.get(i);
                if (studentName.toLowerCase().contains(newValue.toLowerCase())) {
                    filteredIds.add(ids.get(i));
                    filteredNames.add(studentName);
                    filteredEmails.add(emails.get(i));
                    // filteredParents.add(parents.get(i));
                    filteredClassNames.add(classNames.get(i));
                    filteredEdits.add(edits.get(i));
                }
            }

            id.setItems(filteredIds);
            name.setItems(filteredNames);
            email.setItems(filteredEmails);
// parent.setItems(filteredParents);
            className.setItems(filteredClassNames);
            edit.setItems(filteredEdits);
        });
    }

    private void editButtonClick(Button b) throws IOException, SQLException {
        Student s = (Student) b.getUserData();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        editStudentController controller = fxmlLoader.getController();

        controller.setStudent(s);
        controller.initialize(s);

        stage.showAndWait();

        id.refresh();
        name.refresh();
        email.refresh();
        //parent.refresh();
        className.refresh();
        edit.refresh();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> emails = FXCollections.observableArrayList();
        ObservableList<String> parents = FXCollections.observableArrayList();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();


        List<Student> studentList = Student.getStudents();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();


        for (Student st : studentList) {
            ids.add(st.userId);
            if(st.fName==null)
                names.add("Account Inactive");
            else
            names.add(st.fName + " " + st.lName);

            if(st.mail==null)
                emails.add("Account Inactive");
            else
            emails.add(st.mail);

            if(st.classId==0)
                classNames.add(" - ");
            else
            classNames.add(classNamesMap.get(st.classId));

            if(s!=null) {
                Button bt = new Button();
                bt.setText("Edit");
                bt.setUserData(st);
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
// parent.setItems(parents);
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


        loggedUser.fromCsv=0;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();

        id.refresh();
        name.refresh();
        email.refresh();
        //parent.refresh();
        className.refresh();
        edit.refresh();

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ObservableList<String> names = FXCollections.observableArrayList();
        ObservableList<String> emails = FXCollections.observableArrayList();
        ObservableList<String> parents = FXCollections.observableArrayList();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        ObservableList<Button> edits = FXCollections.observableArrayList();


        List<Student> studentList = Student.getStudents();
        Map<Integer, String> classNamesMap = Class.getAllClassesMap();


        for (Student st : studentList) {
            ids.add(st.userId);
            if(st.fName==null)
                names.add("Account Inactive");
            else
                names.add(st.fName + " " + st.lName);

            if(st.mail==null)
                emails.add("Account Inactive");
            else
                emails.add(st.mail);

            if(st.classId==0)
                classNames.add(" - ");
            else
                classNames.add(classNamesMap.get(st.classId));

            if(st!=null) {
                Button bt = new Button();
                bt.setText("Edit");
                bt.setUserData(st);
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
// parent.setItems(parents);
            className.setItems(classNames);
            edit.setItems(edits);

        }
    }
}
