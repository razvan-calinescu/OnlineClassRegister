package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Teacher extends SchoolPerson implements hasAverage{

    public Button getEditBtn() {
        return editBtn;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public String getEmail2() {
        return email2;
    }


    Button editBtn;


    String fullName, subjectName, classTeacherName, email2; /// TO DO Average
    Double average;
    int subjectId, weeklyClasses, classTeacherId, teacherId, classesCount;
    List<Integer> ClassesId = new ArrayList<>();


    Map<Integer, String> classesName = new HashMap<>();



    public Teacher(String fName, String lName, int id, int role, String mail, String phone, Date birthDate, int subjectId, int weeklyClasses, int classTeacherId, int teacherId, int classesCount, List<Integer> classes, Map<Integer, String> classesNames, int isActive) {
        super(fName, lName, id, role, mail, phone, birthDate, false, isActive);
        this.subjectId = subjectId;
        this.weeklyClasses = weeklyClasses;
        this.classTeacherId = classTeacherId;
        this.teacherId = teacherId;
        this.classesCount = classesCount;
        ClassesId = classes;
        classesName = classesNames;
    }

    public static List<Teacher> getTeachers()
    {
        List<Teacher> aux= new ArrayList<>();

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Teacher auxT;
            Statement stmt  = conn.createStatement();
            ResultSet rs;

            rs=stmt.executeQuery("Select * FROM teacher\n" +
                    "INNER JOIN users ON\n" +
                    "teacher.userId = users.id");
            while(rs.next())
            {
                int auxTeacherId=rs.getInt("teacher.id");
                int auxSubjId = rs.getInt("subjectId");
                int auxWeeklyClasses = rs.getInt("weeklyClasses");
                int auxClassTeacherId = rs.getInt("classTeacherId");
                int auxClassesCount = rs.getInt("classesCount");
                List<Integer> auxClasses = new ArrayList<>();

                int classesCountColId=rs.findColumn("classesCount");
                for(int i=1; i<=28; i++)
                    if(rs.getInt("class"+i)!=-1)
                    auxClasses.add(rs.getInt("class"+i));


                int auxUserId = rs.getInt("users.id");
                String auxFName= rs.getString("fName");
                String auxLName= rs.getString("lName");
                int auxRole=rs.getInt("role");
                String auxMail=rs.getString("mail");
                String auxPhone=rs.getString("phone");
                Date auxBDate=rs.getDate("birthDate");

                Map<Integer, String> auxClassesName = new HashMap<>();

                    Statement stmt2=conn.createStatement();
                    ResultSet rs2=stmt2.executeQuery("Select * FROM class");

                    while(rs2.next())
                    {
                        String className=rs2.getString("name");
                        Integer classId = rs2.getInt("id");

                        if(auxClasses.contains(classId))
                            auxClassesName.put(classId, className);

                    }

                int auxIsActive = rs.getInt("isActive");


                auxT = new Teacher(auxFName, auxLName, auxUserId, auxRole, auxMail, auxPhone, auxBDate, auxSubjId, auxWeeklyClasses, auxClassTeacherId, auxTeacherId, auxClassesCount, auxClasses, auxClassesName, auxIsActive);
                aux.add(auxT);

            }

        } catch (SQLException e)
        {
            System.out.println("SQL statement creation error: ");
            e.printStackTrace();
        }

        return aux;
    }

    @Override
    public double getAverage() {
        double media = 0;
        int k=0;
        for(Integer i: this.ClassesId)
        {
            double mediaClasei=0, kClasa=0;
            for(Student s: Student.getStudents())
            {
                double ms=0;
                int ks=0;

                if(s.classId==i) {
                    for (regEntry reg : s.regEntries)
                        if (reg.value != 0 && reg.subjectId == this.subjectId) {
                            ms += reg.value;
                            ks++;
                        }


                mediaClasei+=ms/ks;
                kClasa++;
                }

            }

            media+=mediaClasei/kClasa;
            k++;
        }

        if(k>0)
            return media/k;
        else
            return 0;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getWeeklyClasses() {
        return weeklyClasses;
    }

    public int getClassTeacherId() {
        return classTeacherId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getClassesCount() {
        return classesCount;
    }

    public List<Integer> getClassesId() {
        return ClassesId;
    }

    public Map<Integer, String> getClassesName() {
        return classesName;
    }
}
