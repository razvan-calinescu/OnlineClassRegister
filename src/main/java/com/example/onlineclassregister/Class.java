package com.example.onlineclassregister;

import java.util.ArrayList;
import java.util.List;

public class Class {

    int id;
    String name;

    List<Student> studentsEnrolled = new ArrayList<>();
    List<Course> coursesTaught= new ArrayList<>();
    Teacher classTeacher;

    public Class(int id, String name, List<Student> studentsEnrolled, List<Course> coursesTaught, Teacher classTeacher) {
        this.id = id;
        this.name = name;
        this.studentsEnrolled = studentsEnrolled;
        this.coursesTaught = coursesTaught;
        this.classTeacher = classTeacher;
    }
}
