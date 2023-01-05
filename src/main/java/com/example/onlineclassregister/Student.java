package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Student extends SchoolPerson{

    public int  classId, totalMissingAttendance, totalMotivated, userId, registerId, parent1Id, parent2Id, coursesCount, studId;
    public double average;
    public List<Integer> coursesAttended = new ArrayList<>();

    public Student(String fName, String lName, int id, int role, String mail, String phone, Date birthDate, int classId, int totalMissingAttendance, int totalMotivated, int userId, int registerId, int parent1Id, int parent2Id, int coursesCount, double average, List<Integer> coursesAttended, int studId) {
        super(fName, lName, id, role, mail, phone, birthDate, false);
        this.classId = classId;
        this.totalMissingAttendance = totalMissingAttendance;
        this.totalMotivated = totalMotivated;
        this.userId = userId;
        this.registerId = registerId;
        this.parent1Id = parent1Id;
        this.parent2Id = parent2Id;
        this.coursesCount = coursesCount;
        this.average = average;
        this.coursesAttended = coursesAttended;
    }


    public static List<Student> getStudents(){

        List<Student> aux= new ArrayList<>();

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try{

            Student auxS;
            Statement stmt= conn.createStatement();
            ResultSet rs;

            rs=stmt.executeQuery("Select * FROM student\n" +
                    "INNER JOIN users ON \n" +
                    "student.userId = users.id");

            while(rs.next())
            {
                int studId=rs.getInt("student.id");
                int classId=rs.getInt("classId");
                double average=rs.getDouble("average");
                int totalMissingAttendance=rs.getInt("totalMissingAttendance");
                int totalMotivated=rs.getInt("totalMotivated");
                int userId= rs.getInt("users.id");
                int registerId = rs.getInt("registerId");
                int parent1Id = rs.getInt("parent1Id");
                int parent2Id = rs.getInt("parent2Id");

                String fName = rs.getString("fName");
                String lName = rs.getString("lName");
                int role = rs.getInt("role");
                String mail = rs.getString("mail");
                String phone = rs.getString("phone");
                Date birthDate = rs.getDate("birthDate");

                int coursesCount = rs.getInt("coursesCount");
                List<Integer> courseIds = new ArrayList<>();
                int coursesCountColId=rs.findColumn("coursesCount");
                for(int i=0; i<coursesCount; i++)
                    courseIds.add(rs.getInt(coursesCountColId)+i+1);


                auxS=new Student(fName, lName, userId, role, mail, phone, birthDate, classId, totalMissingAttendance, totalMotivated, userId, registerId, parent1Id, parent2Id, coursesCount, average, courseIds, studId );
                aux.add(auxS);
            }


        } catch (SQLException e){
            System.out.println("Error at SQL statement: ");
            e.printStackTrace();
        }


        return aux;
    }

//    public static Map<String, java.sql.Date> getGrades(int studentId)
//    {
//            Map<String, java.sql.Date> aux = new HashMap<>();
//
//            dbConnection dbConnection = new dbConnection();
//            Connection conn = dbConnection.getConnection();
//
//            String SQL="Select * from register"+studentId;
//
//            try{
//
//                Statement stmt = conn.createStatement();
//                ResultSet res= stmt.executeQuery(SQL);
//
////                while(res.next())
////                {
////                    if(res.getInt("isGrade")==1)
////                    {
////
////                    }
////                }
//
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//
//            return null;
//    }
}
