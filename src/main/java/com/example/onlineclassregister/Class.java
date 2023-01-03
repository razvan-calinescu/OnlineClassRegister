package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Class {

    int id, classTeacherId, studentsCount, year;
    String name, room;

    public static Map<Integer, String> allClassesName = new HashMap<>();

    public Class(int id, int classTeacherId, int studentsCount, int year,  String name, String room) {
        this.id = id;
        this.classTeacherId = classTeacherId;
        this.studentsCount = studentsCount;
        this.year= year;
        this.name = name;
        this.room = room;
    }



    public static Map<Integer, String> getAllClassesMap() throws SQLException {


        dbConnection dbconn= new dbConnection();
        Connection conn = dbconn.getConnection();

        String SQL="Select id, name from CLASS";

        Statement stmt = conn.createStatement();
        ResultSet res=stmt.executeQuery(SQL);

        while(res.next())
        {
            allClassesName.put(res.getInt("id"), res.getString("name"));
        }

        return allClassesName;

    }

    public static List<Class> getAllClassesList() {

        List<Class> aux = new ArrayList<>();

        dbConnection dbconn = new dbConnection();
        Connection conn = dbconn.getConnection();

        String SQL = "Select * from CLASS";

        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(SQL);

            while(res.next())
            {
               int auxClassId = res.getInt("id");
               String auxName = res.getString("name");
               int auxYear = res.getInt("year");
               int auxClassTeacherId = res.getInt("classTeacherId");
               int auxStudentsCount = res.getInt("studentsCount");
               String auxRoom = res.getString("room");

               aux.add(new Class(auxClassId, auxClassTeacherId, auxStudentsCount, auxYear, auxName, auxRoom));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }




        return aux;

    }
}
