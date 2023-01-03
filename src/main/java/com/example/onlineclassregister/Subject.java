package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Subject {

    int id, classesWeekly;
    String name;
    double average;

    public Subject(int id, int classesWeekly, String name, double average) {
        this.id = id;
        this.classesWeekly = classesWeekly;
        this.name = name;
        this.average = average;

    }

    public static Map<Integer, String> initSubject(){

        Map<Integer, String> aux = new HashMap<>();

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        String SQL="Select id, name FROM subject";

        try{

            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery(SQL);

            while(rs.next())
            {
                int id=rs.getInt("id");
                String name = rs.getString("name");
                aux.put(id, name);
            }
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return aux;
    }


}
