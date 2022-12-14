package com.example.onlineclassregister;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends SchoolPerson{

    int id;
    List<Class> Classes = new ArrayList<>();

    public Teacher(String fName, String lName, int id, List<Class> classes) {
        super(fName, lName);
        this.id = id;
        Classes = classes;
    }
}
