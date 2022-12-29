package com.example.onlineclassregister;

import java.util.ArrayList;
import java.util.List;

public class Course implements failable{

    int id, teacherId;
    List<Student> studentsEnrolled = new ArrayList<>();
    List<Teacher> teachers = new ArrayList<>();

    public Course(int id, int teacherId, List<Student> studentsEnrolled, List<Teacher> teachers) {
        this.id = id;
        this.teacherId = teacherId;
        this.studentsEnrolled = studentsEnrolled;
        this.teachers = teachers;
    }

    @Override
    public boolean isFailed() {
        System.out.println("add code for failable in course class");
        return false;
    }
}
