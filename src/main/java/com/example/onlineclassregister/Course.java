package com.example.onlineclassregister;

import java.util.ArrayList;
import java.util.List;

public class Course {

    int id, teacherId;
    List<Student> studentsEnrolled = new ArrayList<>();
    List<Teacher> teachers = new ArrayList<>();

    public Course(int id, int teacherId, List<Student> studentsEnrolled, List<Teacher> teachers) {
        this.id = id;
        this.teacherId = teacherId;
        this.studentsEnrolled = studentsEnrolled;
        this.teachers = teachers;
    }
}
