package com.example.onlineclassregister;

import java.util.*;

public class Course implements failable{


   /// dictionar - elev cu lista de note
    /// dictionar - elev cu absente

    ///NU STERGE
   Map<Student, Double> grades = new HashMap<>();
   Map<Student, Integer> attendance = new HashMap<>();
   List<Student> enrolledStudents = new ArrayList<>();
   Teacher teacher;

    @Override
    public boolean isFailed() {
        System.out.println("add code for failable in course class");
        return false;
    }
}
