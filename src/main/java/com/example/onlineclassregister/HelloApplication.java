package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlineclassregister.Student.getStudents;
import static com.example.onlineclassregister.Teacher.getTeachers;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 600);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ClassRegister | Login");
        stage.setScene(scene);
        stage.show();
    }
    

    public static void main(String[] args) throws SQLException {


        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();


        launch();

        conn.close();
    }
}