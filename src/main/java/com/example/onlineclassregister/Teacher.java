package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Teacher extends SchoolPerson{


    int subjectId, weeklyClasses, classTeacherId, isAdmin, teacherId, classesCount; //classesCount = cate clase are in TOTAL, gen 3 daca are o a 9a, 2 a 10a etc
    List<Integer> ClassesId = new ArrayList<>();

    public Teacher(String fName, String lName, int id, int role, String mail, String phone, Date birthDate, int subjectId, int weeklyClasses, int classTeacherId, int isAdmin, int teacherId, int classesCount, List<Integer> classes) {
        super(fName, lName, id, role, mail, phone, birthDate);
        this.subjectId = subjectId;
        this.weeklyClasses = weeklyClasses;
        this.classTeacherId = classTeacherId;
        this.isAdmin = isAdmin;
        this.teacherId = teacherId;
        this.classesCount = classesCount;
        ClassesId = classes;
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
                int auxIsAdmin = rs.getInt("isAdmin");
                int auxClassesCount = rs.getInt("classesCount");
                List<Integer> auxClasses = new ArrayList<>();

                int classesCountColId=rs.findColumn("classesCount");
                for(int i=0; i<auxClassesCount; i++)
                    auxClasses.add(rs.getInt(classesCountColId)+i+1);


                int auxUserId = rs.getInt("users.id");
                String auxFName= rs.getString("fName");
                String auxLName= rs.getString("lName");
                int auxRole=rs.getInt("role");
                String auxMail=rs.getString("mail");
                String auxPhone=rs.getString("phone");
                Date auxBDate=rs.getDate("birthDate");

                auxT = new Teacher(auxFName, auxLName, auxUserId, auxRole, auxMail, auxPhone, auxBDate, auxSubjId, auxWeeklyClasses, auxClassTeacherId, auxIsAdmin, auxTeacherId, auxClassesCount, auxClasses );
                aux.add(auxT);
            }

        } catch (SQLException e)
        {
            System.out.println("SQL statement creation error: ");
            e.printStackTrace();
        }

        return aux;
    }
}
