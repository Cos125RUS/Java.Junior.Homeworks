package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final XmlMapper xmlMapper = new XmlMapper();

    public static void main(String[] args) {
//        Student student = new Student("Valerii", 35, 5.0);
        Student student = newStudent();
        String fileName = String.format("%s_%s",
                student.getName().toLowerCase(), student.getAge());

        write(student, fileName);
        Student[] allFormatStudents = read(fileName);

        if (allFormatStudents != null){
            System.out.println("\nAfter:\nObject Serializable:\t" + allFormatStudents[0]);
            System.out.println("JSON Serializable:\t" + allFormatStudents[1]);
            System.out.println("XML Serializable:\t" + allFormatStudents[2]);
        }
    }

    public static Student newStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("\nAge: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("\nGPA: ");
        double GPA = Double.parseDouble(scanner.nextLine());

        return new Student(name, age, GPA);
    }

    public static void write(Student student, String fileName) {
        System.out.println("\nBefore:\n" + student);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(fileName + ".dat"))) {
            //Сериализация
            objectOutputStream.writeObject(student);

            //Json сериализация
            objectMapper.writeValue(new File(fileName + ".json"), student);

            //XML сериализация
            xmlMapper.writeValue(new File(fileName + ".xml"), student);
        } catch (IOException e) {
            System.err.println(e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static Student[] read(String fileName) {
        //Десериализация
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileName + ".dat"))) {
            Student serStudent = (Student) objectInputStream.readObject();
            Student jsonStudent = objectMapper.readValue(new File(fileName + ".json"),
                    objectMapper.getTypeFactory().constructType(Student.class));
            Student xmlStudent = xmlMapper.readValue(new File(fileName + ".xml"),
                    xmlMapper.getTypeFactory().constructType(Student.class));

            return new Student[] {serStudent, jsonStudent, xmlStudent };
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e.getMessage() + "\n");
            e.printStackTrace();
        }
        return null;
    }
}