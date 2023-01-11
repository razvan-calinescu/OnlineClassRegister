package com.example.onlineclassregister;

import conn.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchoolPerson {

    String fName, lName;
    int userId, role;
    String mail, phone;

    boolean isAdmin;
    int isActive;

    Date birthDate;

    public SchoolPerson(String fName, String lName, int id, int role, String mail, String phone, Date birthDate, boolean isAdmin, int isActive) {
        this.fName = fName;
        this.lName = lName;
        this.userId = id;
        this.role = role;
        this.mail = mail;
        this.phone = phone;
        this.birthDate = birthDate;
        this.isAdmin = isAdmin;
        this.isActive= isActive;
    }

    public static List<SchoolPerson> getUsers(){

        List<SchoolPerson> aux = new ArrayList<>();

        dbConnection dbConn = new dbConnection();
        Connection conn = dbConn.getConnection();

        try{
            Statement stmt = conn.createStatement();
            ResultSet res;

            res=stmt.executeQuery("Select * from users");

            while(res.next())
            {
                String auxFName = res.getString("fName");
                String auxLName = res.getString("lName");
                int auxId = res.getInt("id");
                int auxRole = res.getInt("role");

                boolean auxIsAdmin;

                if(res.getBoolean("isAdmin"))
                    auxIsAdmin =true;
                else
                    auxIsAdmin=false;

                String auxMail = res.getString("mail");
                String auxPhone = res.getString("phone");
                Date auxBDate = res.getDate("birthDate");
                int auxIsActive = res.getInt("isActive");

                aux.add(new SchoolPerson(auxFName, auxLName, auxId, auxRole, auxMail, auxPhone, auxBDate, auxIsAdmin, auxIsActive));


            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return aux;

    }


    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public int getUserId() {
        return userId;
    }

    public String getMail() {
        return mail;
    }


}
