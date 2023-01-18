package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Student extends SchoolPerson implements hasAverage{

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

    @Override
    public double getAverage() {
        double m=0;
        int k=0;

        for(regEntry reg: this.regEntries)
            if(reg.value!=0)
            {
                m+=reg.value;
                k++;
            }

        if(k>0)
        return m/k;
        else
            return 0;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getTotalMissingAttendance() {
        return totalMissingAttendance;
    }

    public void setTotalMissingAttendance(int totalMissingAttendance) {
        this.totalMissingAttendance = totalMissingAttendance;
    }

    public int getTotalMotivated() {
        return totalMotivated;
    }

    public void setTotalMotivated(int totalMotivated) {
        this.totalMotivated = totalMotivated;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParent1Id() {
        return parent1Id;
    }

    public void setParent1Id(int parent1Id) {
        this.parent1Id = parent1Id;
    }

    public int getParent2Id() {
        return parent2Id;
    }

    public void setParent2Id(int parent2Id) {
        this.parent2Id = parent2Id;
    }

    public int getCoursesCount() {
        return coursesCount;
    }

    public void setCoursesCount(int coursesCount) {
        this.coursesCount = coursesCount;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public List<Integer> getCoursesAttended() {
        return coursesAttended;
    }

    public void setCoursesAttended(List<Integer> coursesAttended) {
        this.coursesAttended = coursesAttended;
    }

    public List<regEntry> getRegEntries() {
        return regEntries;
    }

    public void setRegEntries(List<regEntry> regEntries) {
        this.regEntries = regEntries;
    }

    public Map<Integer, Double> getAverages() {
        return averages;
    }

    public void setAverages(Map<Integer, Double> averages) {
        this.averages = averages;
    }
}
