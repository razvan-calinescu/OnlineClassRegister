package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class createTeacherController {



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
    private List<Integer> ClassesId = new ArrayList<>();


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

    String updateStmt, updateStmt2, updateStmt3, updateStmt4, email1;
    Boolean ok,ok2,ok3,ok4;

    int idS=-1, check=0;

    @FXML
    private void removeClassClick() throws SQLException {
        if(deleteClassField.getText().isEmpty())
        {
            deleteClassField.setStyle("-fx-border-color: red;");
            deleteClassField.setPromptText("id");
        } else
        {
            int id = Integer.parseInt(deleteClassField.getText());
          ClassesId.remove((Integer)id);
        }

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        Statement stmt = conn.createStatement();
        classes.getItems().clear();
        for (int i = 1; i <= ClassesId.size(); i++) {
            String SQL = "UPDATE teacher SET class" + i + "= " + ClassesId.get(i-1) + " WHERE userId=" + idS + ";";
            stmt.executeUpdate(SQL);
        }

        for (int i = ClassesId.size() + 1; i <= 28; i++) {
            String SQL = "UPDATE teacher SET class" + i + "= -1 WHERE userId=" + idS + ";";
            stmt.executeUpdate(SQL);
        }
        for(Integer id: ClassesId)
            classes.getItems().add(Class.getAllClassesMap().get(id) + " - id: "+ id);

        for(Integer i: ClassesId)
            classTeacher.getItems().add(Class.getAllClassesMap().get(i));
    }


    @FXML
    private void addClassClick() throws SQLException {

        if(check==0) {
            addButtonClick1();
            check = 1;
        }

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
                    ClassesId.add(c.id);
        }

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        PreparedStatement pstmt;
        Statement stmt=conn.createStatement();
        classes.getItems().clear();


        for(int i=0; i<ClassesId.size(); i++)
        {
            String SQL = "UPDATE teacher SET class"+(i+1)+"= ? WHERE userId= ?";
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, ClassesId.get(i));
            pstmt.setInt(2, idS);
            pstmt.executeUpdate();
        }

        for(Integer id: ClassesId)
            classes.getItems().add(Class.getAllClassesMap().get(id));

        for(Integer i: ClassesId)
            classTeacher.getItems().add(Class.getAllClassesMap().get(i));
    }




    @FXML
    public void initialize() throws SQLException {

        List<Class> classes1= Class.getAllClassesList();
        classes.setFixedCellSize(30);

        for(Class c: classes1)
        {
            availClasses.getItems().add(c.name);
            classTeacher.getItems().add(c.name);
        }


        System.out.println("here");
        for(Subject s: Subject.initSubjectList()) {
            subject.getItems().add(s.name);
            System.out.println(s.name);
        }


    }

    public void setTeacher(Teacher s) {
        this.teacher = s;
    }
    public void addButtonClick1() {

        Map<Integer, String> subjectNames = Subject.initSubject();

         updateStmt = "INSERT INTO users(role, fName, lName, mail, password) VALUES (1, ";
         ok = false;

        if (!fName.getText().isEmpty()) {
            ok = true;
            updateStmt += " '" + fName.getText() + "', ";
        }
        if (!lName.getText().isEmpty()) {
            ok = true;
            updateStmt += "'" + lName.getText() + "', ";
        }
        if (!email.getText().isEmpty() && emailOk(email.getText())) {
            ok = true;
            email1=email.getText();
            updateStmt += "'" + email.getText() + "', ";
        } else if (!email.getText().isEmpty()) {
            email.setPromptText("Please provide a correct email!");
        }

        if (!password.getText().isEmpty()) {
            ok = true;
            md5 md5Helper = new md5();
            String passwordSQL = md5Helper.getMd5(password.getText());
            updateStmt +=  "'"+ passwordSQL+"')";
        }


        if (ok) {
            try {
                md5 md5Helper = new md5();
                // execute the insert statement and retrieve the id
                dbConnection dbConn = new dbConnection();
                Connection conn = dbConn.getConnection();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(updateStmt);


                String SQId = "Select id from users";
                ResultSet res = stmt.executeQuery(SQId);

                while(res.next())
                    idS=res.getInt("id");


            } catch (SQLException e) {
                System.out.println("STMT: "+updateStmt);
                e.printStackTrace();
            }
        }


         ok2 = false;
         updateStmt2 = "INSERT INTO teacher(userId, classTeacherId, subjectId) VALUES("+idS+", ";
         updateStmt3= "";
         updateStmt4= "";

         ok3=false;
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

            updateStmt2 += id + ", ";
            updateStmt4 = "Update class set classTeacherId = 0 WHERE classTeacherId= "+idS+ ";";
            updateStmt3 = " Update class set classTeacherId =" + idS + " WHERE id = "+ id + ";";
        }


        if (subject.getValue() != null) {
            ok2 = true;
            int id = -1;
            for (Map.Entry<Integer, String> entry : subjectNames.entrySet()) {
                if (entry.getValue().equals(subject.getValue())) {
                    id = entry.getKey();
                }
            }

            updateStmt2 +=   id + ")";

        }

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try {
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt1 = conn.prepareStatement("SELECT COUNT(*), id FROM users WHERE LOWER(mail) = LOWER(?)");
            pstmt1.setString(1, email1);
            ResultSet result = pstmt1.executeQuery();
            result.next();

                if (ok2) {
                    stmt.executeUpdate(updateStmt2);
                }
                if (ok3) {
                    stmt.executeUpdate(updateStmt4);
                    stmt.executeUpdate(updateStmt3);


            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }





    }

    public void addButtonClick()
    {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
