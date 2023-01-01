package com.example.onlineclassregister;

public class loggedUser {
    public static String mail;
    public static int userId;

    public static String getRole(SchoolPerson p)
    {
        if(p.role==1)
            if(p instanceof Teacher && ((Teacher) p).isAdmin==1)
            return "System Administrator";
            else return "Teacher";
        else if(p.role==2)
            return "Student";
        else if(p.role==3)
            return "Parent";
        else
            return "Error determining role";
    }
}
