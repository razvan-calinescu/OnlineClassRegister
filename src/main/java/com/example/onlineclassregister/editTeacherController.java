package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class editTeacherController {


    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    private Teacher teacher;

    @FXML
    private Text title;

    @FXML
    private TextField fName;

    @FXML
    private TextField email;
    @FXML
    private TextField lName;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> classTeacher;

    @FXML
    private ComboBox<String> subject;

    @FXML
    private ListView<String> classes;

    @FXML
    private ChoiceBox<String> availClasses;

    @FXML
    private Button deleteButton;

    @FXML
    private Button deleteConfirmButton;

    @FXML
    private Button addClass;

    @FXML
    private Button deleteClass;

    @FXML
    private TextField deleteClassField;

    @FXML
    private Label alertText;

    private List<Integer> addedClasses = new ArrayList<>();
    private List<Integer> removedClasses = new ArrayList<>();

    @FXML
    private void removeClassClick() throws SQLException {
        if(deleteClassField.getText().isEmpty())
        {
            deleteClassField.setStyle("-fx-border-color: red;");
            deleteClassField.setPromptText("id");
        } else
        {
            int id = Integer.parseInt(deleteClassField.getText());
            teacher.ClassesId.remove((Integer)id);
        }

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        Statement stmt = conn.createStatement();
        classes.getItems().clear();
        for (int i = 1; i <= teacher.ClassesId.size(); i++) {
            String SQL = "UPDATE teacher SET class" + i + "= " + teacher.ClassesId.get(i-1) + " WHERE userId=" + teacher.userId + ";";
            stmt.executeUpdate(SQL);
        }

        for (int i = teacher.ClassesId.size() + 1; i <= 28; i++) {
            String SQL = "UPDATE teacher SET class" + i + "= -1 WHERE userId=" + teacher.userId + ";";
            stmt.executeUpdate(SQL);
        }
        for(Integer id: teacher.ClassesId)
            classes.getItems().add(Class.getAllClassesMap().get(id) + " - id: "+ id);

        for(Integer i: teacher.ClassesId)
            classTeacher.getItems().add(Class.getAllClassesMap().get(i));
    }


    @FXML
    private void addClassClick() throws SQLException {

        if(availClasses.getValue().isEmpty())
        {
            alertText.setStyle("-fx-color: red");
            alertText.setText("Choose a class before adding!");
        }
        else
        {
            alertText.setStyle("-fx-color: black");
            alertText.setText("Already Assigned Classes");
            for(Class c: Class.getAllClassesList())
                if(c.name.equals(availClasses.getValue()))
                    teacher.ClassesId.add(c.id);
        }

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        PreparedStatement pstmt;
        classes.getItems().clear();

        for(int i=0; i<teacher.ClassesId.size(); i++)
        {
            String SQL = "UPDATE teacher SET class"+(i+1)+"= ? WHERE userId= ?";
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, teacher.ClassesId.get(i));
            pstmt.setInt(2, teacher.userId);
            pstmt.executeUpdate();
        }

        for(Integer id: teacher.ClassesId)
            classes.getItems().add(Class.getAllClassesMap().get(id));

        for(Integer i: teacher.ClassesId)
            classTeacher.getItems().add(Class.getAllClassesMap().get(i));
    }



    @FXML
    public void deleteButtonClick(){

        deleteConfirmButton.setDisable(false);
        deleteConfirmButton.setOpacity(100);
        deleteButton.setDisable(true);
        deleteButton.setOpacity(0);

    }

    @FXML
    public void deleteConfirmButton() {

        String SQL= "Delete from users where id= "+ teacher.userId+";";
      //  String SQL2= "DROP table teacher"+teacher.userId+";";

        dbConnection dbConn =new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
         //   stmt.executeUpdate(SQL2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }



    @FXML
    public void initialize(Teacher t) throws SQLException {
        title.setText("You'll now be editing "+t.fName+ " "+ t.lName);
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);


        List<Class> classes1= Class.getAllClassesList();



        classes.setFixedCellSize(30);

        for(Class c: classes1)
        {
            if(teacher.ClassesId.contains(c.id))
            classes.getItems().add(c.name+" - id: "+c.id);
            else
                availClasses.getItems().add(c.name);

        }

        for(Integer i: teacher.ClassesId)
            classTeacher.getItems().add(Class.getAllClassesMap().get(i));

        for(Subject s: Subject.initSubjectList())
            subject.getItems().add(s.name);


    }

    public void setTeacher(Teacher s) {
        this.teacher = s;
    }
    public void addButtonClick(ActionEvent actionEvent) {

        Map<Integer, String> subjectNames = Subject.initSubject();

        String updateStmt = "UPDATE users SET ";
        boolean ok = false;

        if (!fName.getText().isEmpty()) {
            ok = true;
            updateStmt += "fName = '" + fName.getText() + "', ";
        }
        if (!lName.getText().isEmpty()) {
            ok = true;
            updateStmt += "lName = '" + lName.getText() + "', ";
        }
        if (!email.getText().isEmpty() && emailOk(email.getText())) {
            ok = true;
            updateStmt += "email = '" + email.getText() + "', ";
        } else if (!email.getText().isEmpty()) {
            email.setPromptText("Please provide a correct email!");
        }

        if (!password.getText().isEmpty()) {
            ok = true;
            updateStmt += "password = '" + password.getText() + "', ";
        }

// Remove trailing comma
        updateStmt = updateStmt.substring(0, updateStmt.length() - 2);
// Add WHERE clause to update specific record
        updateStmt += " WHERE id = " + teacher.userId + ";";

        Boolean ok2 = false;
        String updateStmt2 = "UPDATE teacher SET";
        String updateStmt3= "";
        String updateStmt4= "";

        Boolean ok3=false;
        if (classTeacher.getValue() != null) {
            ok2 = true;
            ok3=true;

            List<Class> classes = Class.getAllClassesList();

            int id = -1;
            for (Class c : classes) {
                if (c.name.equals(classTeacher.getValue())) {
                    id = c.id;
                }
            }

            updateStmt2 += " classTeacherId = " + id + ", ";
            updateStmt4 = "Update class set classTeacherId = 0 WHERE classTeacherId= "+teacher.userId+ ";";
            updateStmt3 = " Update class set classTeacherId =" + teacher.userId + " WHERE id = "+ id + ";";
        }


        if (subject.getValue() != null) {
            ok2 = true;
            int id = -1;
            for (Map.Entry<Integer, String> entry : subjectNames.entrySet()) {
                if (entry.getValue().equals(subject.getValue())) {
                    id = entry.getKey();
                }
            }

            updateStmt2 += "subjectId = " +  id + ", ";

        }

// Remove trailing comma
        updateStmt2 = updateStmt2.substring(0, updateStmt2.length() - 2);
// Add WHERE clause to update specific record
        updateStmt2 += " WHERE userId = " + teacher.userId + ";";


        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try {
            Statement stmt = conn.createStatement();
            if (ok) {
                stmt.executeUpdate(updateStmt);
            }
            if (ok2) {
                stmt.executeUpdate(updateStmt2);
            }
            if(ok3)
            {
                stmt.executeUpdate(updateStmt4);
                stmt.executeUpdate(updateStmt3);

            }


            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        }

    private boolean emailOk(String text) {

        boolean checkAt = false, checkDot = false;
        char auxChar;

        for (int i = 0; i < text.length(); i++)
            if (text.charAt(i) == '@')
                checkAt = true;
            else if (text.charAt(i) == '.' && checkAt == true)
                checkDot = true;

        if (checkAt && checkDot)
            return true;

        return false;

    }

    public void cancelButtonClick( ) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
