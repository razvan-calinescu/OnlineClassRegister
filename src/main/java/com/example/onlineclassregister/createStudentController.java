package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class createStudentController {
    @FXML
    private TextField email;

    @FXML
    private Button chooseFileButton;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private md5 md5Helper = new md5();

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
    public void addButtonClick() {

        if (!email.getText().isEmpty()) {

            if (validateEmail(email.getText())==false) {
                email.clear();
                email.setPromptText("Please provide a correct email address!");
            } else if(emailExists(email.getText())==true){
                email.clear();
                email.setPromptText("User already exists!");
            }else {

                String emailSQL = email.getText();
                String password = email.getText(0, 5);
                password = md5Helper.getMd5(password);

                String SQL = "INSERT INTO users (mail, role, password) VALUES (?, ?, ?)";
                String SQL11 = "Insert into users() VALUES();";


                try{

                    dbConnection dbConn = new dbConnection();
                    Connection conn =dbConn.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, emailSQL);
                    stmt.setInt(2, 2); //2= student
                    stmt.setString(3, password);
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    int id=-1;
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                    rs.close();

                    Statement stmt11 = conn.createStatement();
                    stmt11.executeUpdate(SQL11);

                    if(id!=-1)
                    {
                        String SQL1="Create Table register"+id+" like grades";
                        Statement stmt1=conn.createStatement();
                        stmt1.executeUpdate(SQL1);

                        String SQL2 = "INSERT INTO student (userId) VALUES (?)";
                        PreparedStatement stmt2= conn.prepareStatement(SQL2);
                        stmt2.setInt(1, id);
                        stmt2.executeUpdate();


                    }

                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();

                } catch (SQLException e) {
                    System.out.println("Error inserting record into users table: " + e.getMessage());
                }

            }
        }
        else
        {
            email.clear();
            email.setPromptText("Please provide an email before clicking 'add'! ");

        }
    }

    private boolean emailExists(String text) {

        dbConnection dbconn = new dbConnection();
        Connection conn = dbconn.getConnection();

        String SQL = "Select mail from users";

        try{
            Statement stmt = conn.createStatement();
            ResultSet res=stmt.executeQuery(SQL);

            while(res.next())
            {
                if(res.getString("mail")!=null)
                if(res.getString("mail").equals(text))
                    return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    public void cancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void chooseFile() {

        loggedUser.fromCsv=1;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {

            try {
                FileReader reader = new FileReader(file);
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

                for (CSVRecord record : records) {
                    String emailSQL = record.get(0);
                    String password = emailSQL.substring(0, 5);
                    password = md5Helper.getMd5(password);

                    if (!emailSQL.isBlank() && !emailExists(emailSQL)) {

                      //  String SQL = "INSERT INTO users (mail, role, password) VALUES (?, ?, ?)";

                        String SQL = "INSERT INTO users (mail, role, password) VALUES (?, ?, ?)";
                        String SQL11 = "Insert into users() VALUES();";

                        try{

                            dbConnection dbConn = new dbConnection();
                            Connection conn =dbConn.getConnection();
                            PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                            stmt.setString(1, emailSQL);
                            stmt.setInt(2, 2); //2= student
                            stmt.setString(3, password);
                            stmt.executeUpdate();

                            ResultSet rs = stmt.getGeneratedKeys();
                            int id=-1;
                            if (rs.next()) {
                                id = rs.getInt(1);
                            }
                            rs.close();

                            Statement stmt11 = conn.createStatement();
                            stmt11.executeUpdate(SQL11);

                            if(id!=-1)
                            {
                                String SQL1="Create Table register"+id+" like grades";
                                Statement stmt1=conn.createStatement();
                                stmt1.executeUpdate(SQL1);

                                String SQL2 = "INSERT INTO student (userId) VALUES (?)";
                                PreparedStatement stmt2= conn.prepareStatement(SQL2);
                                stmt2.setInt(1, id);
                                stmt2.executeUpdate();


                            }


                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();

                        } catch (SQLException e) {
                            System.out.println("Error inserting record into users table: " + e.getMessage());
                        }


                    }
                }
                reader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

