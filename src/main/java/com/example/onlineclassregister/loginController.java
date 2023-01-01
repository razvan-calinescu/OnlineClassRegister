package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void loginButton(ActionEvent e)
    {
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

        }

    }

    private int attemptLogin(String email, String password) {


        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        md5 md5Helper = new md5();
        String hashedPassword = md5Helper.getMd5(password);
        int countRes=0;

        try{
            Statement stmt = conn.createStatement();
            ResultSet res;

            res=stmt.executeQuery("Select mail, password from users where LOWER(mail)='"+email+"' and password='"+hashedPassword+"'");


            while(res.next())
            {
                countRes++;
                System.out.println(res.getString("mail"));
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if(countRes==1)
            return 1;
        else
        return 0;

    }


}
