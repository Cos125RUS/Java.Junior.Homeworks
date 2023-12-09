package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        Student student = new Student("Valerii", 35, 5.0);

        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        String fileName = String.format("%s_%s",
                student.getName().toLowerCase(), student.getAge());

        System.out.println("Before:\n" + student);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(fileName + ".dat"))){
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

        //Десериализация
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileName + ".dat"))) {
            Student serStudent = (Student) objectInputStream.readObject();
            Student jsonStudent = objectMapper.readValue(new File(fileName + ".json"),
                    objectMapper.getTypeFactory().constructType(Student.class));
            Student xmlStudent = xmlMapper.readValue(new File(fileName + ".xml"),
                    xmlMapper.getTypeFactory().constructType(Student.class));

            System.out.println("\nAfter:\nObject Serializable:\t" + serStudent);
            System.out.println("JSON Serializable:\t" + jsonStudent);
            System.out.println("XML Serializable:\t" + xmlStudent);
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}