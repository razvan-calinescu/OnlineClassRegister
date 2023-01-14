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

public class Subject implements hasAverage {

    int id;
    String name;
    double average;

    public Subject(int id, String name, double average) {
        this.id = id;
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

    public static List<Subject> initSubjectList(){

        List<Subject> aux =new ArrayList<>();

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        String SQL="Select id, name, average FROM subject";

        try{

            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery(SQL);

            while(rs.next())
            {
                int id=rs.getInt("id");
                String name = rs.getString("name");
                double average = rs.getDouble("average");

                aux.add(new Subject(id,name, average));
            }
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return aux;
    }


    @Override
    public double getAverage() {

        List<Class> classes=Class.getAllClassesList();
        List<Student> students= Student.getStudents();

        double avg=0;

        for(Class c: classes) {
            double cAvg=0;
            int avgCount=0;
            if (c.subjectsTaught.contains(this.id)) {
                double avg1=0;
                int avgCount1=0;
                for (Student s : students)
                    if (s.classId == c.id)
                        if (s.averages.containsKey(this.id))
                {
                    avg1+=s.averages.get(this.id);
                    avgCount1++;
                }
                cAvg+=(avg1/avgCount1);
                avgCount++;
            }


        }

        if(avg!=0)
        return avg;
        else
            return 0.0;
    }
}
