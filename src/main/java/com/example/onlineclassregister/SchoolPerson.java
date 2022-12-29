package com.example.onlineclassregister;

import java.util.Date;

public class SchoolPerson {

    String fName, lName;
    int userId, role;
    String mail, phone;

    Date birthDate;

    public SchoolPerson(String fName, String lName, int id, int role, String mail, String phone, Date birthDate) {
        this.fName = fName;
        this.lName = lName;
        this.userId = id;
        this.role = role;
        this.mail = mail;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
