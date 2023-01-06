package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class registerStudentController {

    @FXML
    private ListView<String> subjectList;

    @FXML
    private ListView<Button> buttonList;

    @FXML
    private ListView<String> gradesList;

    @FXML
    private ListView<String> attendanceList;

    @FXML
    private Button exitButton;

    @FXML
    private Label dateBar;

    @FXML
    public void initialize(){

        List<SchoolPerson> users = SchoolPerson.getUsers();
        SchoolPerson user = null;

        for(SchoolPerson p: users)
            if(p.userId==loggedUser.userId)
                user=p;

        if(user != null)
        dateBar.setText("Logged in as "+ user.mail);


        Map<Integer, String> subjectNames = Subject.initSubject();
        List<Integer> attendedCourses = new ArrayList<>();


        String SQL= "Select * from student where userId="+loggedUser.userId+";";

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt= conn.createStatement();
            ResultSet res = stmt.executeQuery(SQL);

            while(res.next())
            {
                int coursesCount = res.getInt("coursesCount");
                int colIndex=res.findColumn("coursesCount");
                for(int i=1; i<=coursesCount; i++)
                    attendedCourses.add(res.getInt(colIndex+i));
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(!attendedCourses.isEmpty())
        {
            for(Integer courseId : attendedCourses)
                subjectList.getItems().add(subjectNames.get(courseId));


        }



    }

    public void exitButton() {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();

    }
}
