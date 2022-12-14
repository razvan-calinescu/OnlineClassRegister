package com.example.onlineclassregister;

import java.util.ArrayList;
import java.util.List;

public class Student extends SchoolPerson{

    int id, classId;
    List<Course> CoursesEnrolled = new ArrayList<>();

    public Student(String fName, String lName, int id, int classId, List<Course> coursesEnrolled) {
        super(fName, lName);
        this.id = id;
        this.classId = classId;
        CoursesEnrolled = coursesEnrolled;
    }
}
