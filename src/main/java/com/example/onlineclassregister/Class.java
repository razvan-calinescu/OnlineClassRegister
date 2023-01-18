package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Class implements  hasAverage{

    public int id;
    public int classTeacherId;
    public int studentsCount;
    public int year;
    public int subjectsCount;
    public String name;
    public String room;

    public double average;

    String classTeacherName;
    public List<Integer> subjectsTaught= new ArrayList<>();

    public Button edit;

    public static Map<Integer, String> allClassesName = new HashMap<>();

    public Class(int id, int classTeacherId, int studentsCount, int year,  String name, String room, int subjectsCount, List<Integer> subjectsTaught) {
        this.id = id;
        this.classTeacherId = classTeacherId;
        this.studentsCount = studentsCount;
        this.year= year;
        this.name = name;
        this.room = room;
        this.subjectsCount = subjectsCount;
        this.subjectsTaught = subjectsTaught;
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

               int auxSubjectsCount = res.getInt("coursesCount");

               List<Integer> auxSList = new ArrayList<>();
               for(int i=1; i<=auxSubjectsCount; i++)
                    auxSList.add(res.getInt(res.findColumn("coursesCount")+i));

               aux.add(new Class(auxClassId, auxClassTeacherId, auxStudentsCount, auxYear, auxName, auxRoom,auxSubjectsCount, auxSList));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }




        return aux;

    }

    @Override
    public double getAverage() {
        double m=0;
        int k=0;

        for(Student s: Student.getStudents())
            if(s.classId==this.id)
            {
                m+=s.getAverage();
                k++;
            }

        if(k>0)
            return m/k;
        else
            return 0;
    }

    public int getId() {
        return id;
    }

    public int getClassTeacherId() {
        return classTeacherId;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public int getYear() {
        return year;
    }

    public int getSubjectsCount() {
        return subjectsCount;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public List<Integer> getSubjectsTaught() {
        return subjectsTaught;
    }

    public static Map<Integer, String> getAllClassesName() {
        return allClassesName;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public Button getEdit() {
        return edit;
    }
}
