package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
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
    private ListView<CheckBox> checkBoxList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button deleteConfirmButton;


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
    public void initialize(Teacher t){
        title.setText("You'll now be editing "+t.fName+ " "+ t.lName);
        deleteConfirmButton.setDisable(true);
        deleteConfirmButton.setOpacity(0);

        Map<Integer, String> subjectNames = Subject.initSubject();

        for (Map.Entry<Integer, String> entry : subjectNames.entrySet()) {
            subject.getItems().add(entry.getValue());
        }

        List<Class> classes1= Class.getAllClassesList();

        for(Class c: classes1)
            classTeacher.getItems().add(c.name);

        classes.setFixedCellSize(30);

        for(Class c: classes1)
        {
            classes.getItems().add(c.name);

            checkBoxList.setFixedCellSize(30);
            CheckBox cbx = new CheckBox();
            cbx.setUserData(c);

            if(teacher.ClassesId.contains(c))
                cbx.setSelected(true);
            checkBoxList.getItems().add(cbx);
        }


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

    boolean ok4=false;

        List<Integer> classesIds = new ArrayList<>();
        for(CheckBox cbx: checkBoxList.getItems())
            if(cbx.isSelected()) {
                Class c = (Class) cbx.getUserData();
                ok4=true;
                classesIds.add(c.id);

            }

        for(Integer i: classesIds)
        {
            if(!teacher.ClassesId.contains(i))
                teacher.ClassesId.add(i);
        }

        for(Integer i: teacher.ClassesId)
            if(!classesIds.contains(i))
                teacher.ClassesId.remove(i);

        int len=0;
        for(Integer i: teacher.ClassesId)
            len++;
        ///SEt classes count to length of classes Id
        ///Rewrite columns, deleting not reqiored
        String updateStmt5 = "UPDATE teacher SET classesCount= "+len+", ";
        String updateStmt6 = "";
        if(teacher.classesCount<len)
            for(int i=1; i<=(teacher.classesCount-len); i++)
                updateStmt6+="ALTER TABLE teacher ADD class"+teacher.classesCount+i+" INT NOT NULL;";

        int index=1;
        for(Integer id: teacher.ClassesId) {
            updateStmt5 += " class" + index + " = " +id+", ";

        }

        updateStmt5 = updateStmt5.substring(0, updateStmt5.length() - 2);
// Add WHERE clause to update specific record
        updateStmt5 += " WHERE userId = " + teacher.userId + ";";

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
            if(ok4)
            {
                stmt.executeUpdate(updateStmt6);
                stmt.executeUpdate(updateStmt5);
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
