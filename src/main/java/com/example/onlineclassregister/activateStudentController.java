package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class activateStudentController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    private Student student;

    @FXML
    private Text title;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> className;

    @FXML
    private TextField parentFName;

    @FXML
    private TextField parentLName;

    @FXML
    private TextField parentEmail;

    @FXML
    private TextField parentPhone;


    @FXML
    public void deleteConfirmButton() {

        String SQL= "Delete from users where id= "+ student.userId+";";
        String SQL2= "DROP table register"+student.userId+";";

        dbConnection dbConn =new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            stmt.executeUpdate(SQL2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }



    @FXML
    public void initialize() throws SQLException {
        Map<Integer, String> classes = Class.getAllClassesMap();
        title.setText("Welcome to your account, "+ loggedUser.mail);
        email.setText(loggedUser.mail);



        for(String classNames: classes.values())
            className.getItems().add(classNames);
    }

    public void setStudent(Student s) {
        this.student = s;
    }
    public void addButtonClick(ActionEvent actionEvent) throws SQLException {

        boolean stmt1=false;
        String firstName = fName.getText();
        String lastName = lName.getText();
        String dob1 = null;
        String email1 = email.getText();
        String password1 = password.getText();

        String updateStmt ="UPDATE users SET ";


        if(!fName.getText().isEmpty()) {
            updateStmt += "fName = '" + firstName + "', ";
        }
        else {
            fName.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty First Name Field");
        }
        if(!lName.getText().isEmpty()) {

            updateStmt += "lName = '" + lastName + "', ";
        }
        else {
            lName.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty Last Name Field");
        }

        if(!password1.isEmpty()) {

            md5 md5Helper = new md5();
            password1=md5Helper.getMd5(password1);
            updateStmt += "password = '" + password1 + "', ";
        }
        else {
            password.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty Password Field");
        }
        // Remove trailing comma
        updateStmt = updateStmt.substring(0, updateStmt.length() - 2);
        // Add WHERE clause to update specific record
        updateStmt += " WHERE id = "+ loggedUser.userId + ";";

        dbConnection dbConn = new dbConnection();
        Connection conn= dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(updateStmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<Integer, String> classNames= Class.getAllClassesMap();
        List<Student> students = Student.getStudents();
        Student student= null;

        for(Student s: students)
            if(s.userId== loggedUser.userId)
                student= s;

        String updateStmt2="";
        boolean stmt2=false;
        if(!className.getValue().isEmpty())
        {
            stmt2=true;
            int classId=-1;

            for(Integer key: classNames.keySet())
                if(classNames.get(key).equals(className.getValue()))
                {
                    classId = key;
                    break;
                }

            updateStmt2="UPDATE student SET classId="+classId+" where userId="+ student.userId+";";
        } else {
            className.setStyle("-fx-border-color: red;");
            throw new RuntimeException("No class chosen");
        }

        String updateStmt3="UPDATE users SET ";
        boolean stmt3=false;

        if(!parentFName.getText().isEmpty()){
            stmt3=true;
            updateStmt3+="fName='"+parentFName.getText()+"', ";
        }else {
            parentFName.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty parent first name");
        }
        if(!parentLName.getText().isEmpty()){
            stmt3=true;
            updateStmt3+="lName='"+parentLName.getText()+"', role=3, ";
        }else {
            parentLName.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty parent last name");
        }

        if(!parentEmail.getText().isEmpty() && emailOk(parentEmail.getText())){
            stmt3=true;
            updateStmt3+="mail='"+parentEmail.getText()+"', ";
        }else {
            parentEmail.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty parent email");
        }

        if(!parentPhone.getText().isEmpty()){
            stmt3=true;
            md5 md5Helper = new md5();
            String pass=parentEmail.getText().substring(0,5);
            pass=md5Helper.getMd5(pass);
            int pId = loggedUser.userId+1;
            updateStmt3+="phone='"+parentPhone.getText()+"', password='"+pass+"' WHERE id="+pId+";";
        }else {
            parentPhone.setStyle("-fx-border-color: red;");
            throw new RuntimeException("Empty parent phone");
        }



        String stmtActivate = "UPDATE users SET isActive = 1 WHERE id=" + loggedUser.userId+"; ";
     //   String stmtParentId = "Select parent1Id from users where LOWER(mail)=LOWER("+ parentEmail.getText()+ ");";


        try{
            Statement stmt=conn.createStatement();
            stmt.executeUpdate(updateStmt2);
            System.out.println("SQL "+updateStmt3);
            stmt.executeUpdate(updateStmt3);
            stmt.executeUpdate(stmtActivate);
          //  ResultSet rs = stmt.executeQuery(stmtParentId);
            int parentId=-1;

            /// Add parent id to students table
//            while(rs.next())
//            {
//                parentId=rs.getInt("parent1Id");
//            }

            String stmt4="UPDATE student Set parent1Id="+loggedUser.userId+1+" WHERE userId="+loggedUser.userId+";";
            Statement stmtParent= conn.createStatement();
            stmtParent.executeUpdate(stmt4);

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registerStudent.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 850, 700);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ClassRegister | Register");
            stage.setScene(scene);
            stage.show();

            Stage stage1 = (Stage) cancelButton.getScene().getWindow();
            stage1.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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