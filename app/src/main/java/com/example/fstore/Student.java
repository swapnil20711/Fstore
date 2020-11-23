package com.example.fstore;

public class Student {
    public String name;
    public String section;
    public int age;
    public float cgpa;

    public Student(String name, String section, int age, float cgpa) {
        this.name = name;
        this.section = section;
        this.age = age;
        this.cgpa = cgpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", section='" + section + '\'' +
                ", age=" + age +
                ", cgpa=" + cgpa +
                '}';
    }

    public  Student(){

    }
}
