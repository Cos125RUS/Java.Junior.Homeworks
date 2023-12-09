package org.example;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private int age;
    private transient double GPA; // Поле не подлежит сериализации

    private Student() {
    }


    public Student(String name, int age, double GPA) {
        this.name = name;
        this.age = age;
        this.GPA = GPA;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", GPA=" + GPA +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
