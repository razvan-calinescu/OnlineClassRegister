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

        double ms=0;
        int ks=0;

        for(Teacher t: Teacher.getTeachers()){

            double mt=0;
            int kt=0;
            if(t.subjectId==this.id){
            for(Integer i: t.ClassesId)
            {
                double mc=0;
                int kc=0;

                for(Student s: Student.getStudents())
                {
                    double mst=0;
                    int kst=0;

                    for(regEntry re: s.regEntries)
                        if(re.subjectId==this.id && re.value!=0)
                        {
                            mst+=re.value;
                            kst++;
                        }
                    mc+=mst/kst;
                    kc++;
                }
                mt+=mc/kc;
                kt++;
            }
            ms+=mt/kt;
            ks++;
            }



        }

        if(ks>0)
            return ms/ks;
        else
            return 0;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
