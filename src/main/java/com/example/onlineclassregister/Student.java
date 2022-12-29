package com.example.onlineclassregister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student extends SchoolPerson{

    int id, classId;
    List<Course> CoursesEnrolled = new ArrayList<>();

    public Student(String fName, String lName, int id, int role, String mail, String phone, Date birthDate, int id1, int classId, List<Course> coursesEnrolled) {
        super(fName, lName, id, role, mail, phone, birthDate);
        this.id = id1;
        this.classId = classId;
        CoursesEnrolled = coursesEnrolled;
    }
}
