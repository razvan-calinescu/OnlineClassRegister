package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class loginController {

    @FXML
    private Button loginExitButton;

    @FXML
    private Label wrongEmail;

    @FXML
    private Label wrongPassword;

    @FXML
    private TextField emailInputField;

    @FXML
    private PasswordField passwordInputField;

    @FXML
    private Label statusMessage;

    SchoolPerson loggedIn;


    public void exitButton(ActionEvent e)
    {

        Stage stage = (Stage) loginExitButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateEmail(String text) {

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

    public void loginButton(ActionEvent e) throws IOException {
        Boolean emailCheck= false, passwordCheck=true;
        if(emailInputField.getText().isBlank())
        wrongEmail.setText("Please provide a correct email");
        else if(validateEmail(emailInputField.getText())==false)
            wrongEmail.setText("Please provide a correct email");
        else
            emailCheck=true;

        if(passwordInputField.getText().isBlank())
        wrongPassword.setText("Please provide a correct password");
        else
            passwordCheck=true;

        int loginResult = 0;


        if(passwordCheck && emailCheck)
            loginResult= attemptLogin(emailInputField.getText(),passwordInputField.getText());


        if(loginResult==0)
        {
            statusMessage.setText("User does not exist. Please check credentials again!");
            statusMessage.setTextFill(Paint.valueOf("red"));
        }
        else
        {
            statusMessage.setText("Logged in successfully! You'll now be redirected to your account.");
            statusMessage.setTextFill(Paint.valueOf("green"));

            loggedUser.mail= emailInputField.getText();
            Stage stage = (Stage) loginExitButton.getScene().getWindow();
            stage.close();
            changeWindow();

        }

    }

    private void changeWindow() throws IOException {

        List<SchoolPerson> users = new ArrayList<>();
        users=SchoolPerson.getUsers();

        SchoolPerson activeUser = null;
        for(SchoolPerson user: users)
            if(user.userId == loggedUser.userId)
                activeUser=user;

        if(activeUser.isAdmin) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("adminTeacher.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 850, 700);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ClassRegister | Teacher Admin");
            stage.setScene(scene);
            stage.show();
        }
        else if(activeUser.role == 1)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 850, 700);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ClassRegister | Teacher");
            stage.setScene(scene);
            stage.show();
        }
        else if(activeUser.role == 2)
        {
        ;
        }
        else if(activeUser.role == 3)
        {
          ;
        }
    }

    private int attemptLogin(String email, String password) {


        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        md5 md5Helper = new md5();
        String hashedPassword = md5Helper.getMd5(password);
        int countRes=0, userId=0;

        try{
            Statement stmt = conn.createStatement();
            ResultSet res;

            res=stmt.executeQuery("Select id, mail, password from users where LOWER(mail)='"+email+"' and password='"+hashedPassword+"'");


            while(res.next())
            {
                countRes++;
                System.out.println(res.getString("mail"));
                userId=res.getInt("id");
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if(countRes==1)
        {
            loggedUser.userId=userId;
            return 1;

        }
        else
        return 0;

    }


}
