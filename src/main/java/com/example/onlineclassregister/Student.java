package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Student extends SchoolPerson{

    public int  classId, totalMissingAttendance, totalMotivated, userId, parent1Id, parent2Id, coursesCount, studId;
    public double average;
    public List<Integer> coursesAttended = new ArrayList<>();
    public List<regEntry> regEntries = new ArrayList<>();

    public Map<Integer, Double> averages = new HashMap<>();

    public Student(String fName, String lName, int id, int role, String mail, String phone, Date birthDate, int classId, int totalMissingAttendance, int totalMotivated, int userId, int parent1Id, int parent2Id, int coursesCount, double average, List<Integer> coursesAttended, int studId, int isActive, List<regEntry> regEntries, Map<Integer, Double> averages) {
        super(fName, lName, id, role, mail, phone, birthDate, false, isActive);
        this.classId = classId;
        this.totalMissingAttendance = totalMissingAttendance;
        this.totalMotivated = totalMotivated;
        this.userId = userId;
        this.parent1Id = parent1Id;
        this.parent2Id = parent2Id;
        this.coursesCount = coursesCount;
        this.average = average;
        this.coursesAttended = coursesAttended;
        this.regEntries= regEntries;
        this.averages = averages;
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

                int isAct = rs.getInt("isActive");


                List<regEntry> regEnts = new ArrayList<>();

                Statement stmt2=conn.createStatement();
                ResultSet res2=stmt2.executeQuery("Select * from register"+userId+";");

                Map<Integer, Double> avgs = new HashMap<>();


                while(res2.next()) {
                    if (res2.getInt("isGrade") == 1) {

                        regEntry rg = new regEntry(res2.getInt("subjectId"), res2.getDate("date"));
                        rg.setValue(res2.getDouble("gradeValue"));
                        regEnts.add(rg);

                    } else if (res2.getInt("isAbsence") == 1) {

                        regEntry rg = new regEntry(res2.getInt("subjectId"), res2.getDate("date"));
                        if (res2.getInt("motivated") == 1)
                            rg.setMotivated(true);
                        else
                            rg.setMotivated(false);

                        regEnts.add(rg);
                    }



                    List<Subject> subjects = Subject.initSubjectList();
                    for (Subject s : subjects) {

                    double savg=0;
                    int count = 0;

                    for (regEntry regEnt : regEnts)
                        if (regEnt.subjectId == s.id)
                            if(regEnt.value != 0)
                        {
                            savg+=regEnt.value;
                            count ++;
                        }

                        avgs.put(s.id, savg);

                     }

            }



                auxS=new Student(fName, lName, userId, role, mail, phone, birthDate, classId, totalMissingAttendance, totalMotivated, userId, parent1Id, parent2Id, coursesCount, average, courseIds, studId, isAct, regEnts, avgs );
                aux.add(auxS);
            }

            conn.close();
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
