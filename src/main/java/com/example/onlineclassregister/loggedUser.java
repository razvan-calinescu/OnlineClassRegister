package com.example.onlineclassregister;

public class loggedUser {
    public static String mail;
    public static int userId;

    public static String getRole(SchoolPerson p)
    {
        if(p.role==1)
            return "Teacher";
        else if(p.role==2)
            return "Student";
        else if(p.role==3)
            return "Parent";
        else
            return "Error determining role";
    }
}
