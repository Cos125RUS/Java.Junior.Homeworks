package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class School {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory()) {
            int choice = -1;
            while ((choice = menu()) != 0) {
                switch (choice) {
                    case 1 -> selectAll(sessionFactory.openSession());
                    case 2 -> add(sessionFactory.openSession());
                    case 3 -> update(sessionFactory.openSession());
                    case 4 -> delete(sessionFactory.openSession());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            scanner.close();
        }

        scanner.close();
    }

    public static int menu() {
        System.out.println("\n1.Просмотреть список курсов\n" +
                "2.Добавить курс\n" +
                "3.Изменить курс\n" +
                "4.Удалить курс\n" +
                "0.Выйти");
        System.out.print(">> ");
        int choice = -1;
        do {
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                comeOn();
            }
        } while (choice < 0 || choice > 9);
        scanner.nextLine();
        return choice;
    }

    public static void comeOn() {
        System.out.println("Эй, ну хватит! Число вводи.");
        System.out.print(">> ");
        scanner.nextLine();
    }

    public static int enterId() {
        int id = -1;
        while (id == -1) {
            System.out.print("id курса: ");
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                comeOn();
            }
        }
        scanner.nextLine();
        return id;
    }

    public static Course newCourse() {
        System.out.print("Название курса: ");
        String title = scanner.nextLine();
        double duration = -1.0;
        while (duration == -1.0) {
            System.out.print("Продолжительность: ");
            try {
                duration = scanner.nextDouble();
            } catch (InputMismatchException e) {
                comeOn();
            }
        }
        scanner.nextLine();
        return new Course(title, duration);
    }

    public static void add(Session session) {
        Course course = newCourse();
        session.beginTransaction();
        session.save(course);
        session.getTransaction().commit();
        session.close();
    }

    public static void update(Session session) {
        int id = enterId();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        Course updateCourse = newCourse();
        course.setTitle(updateCourse.getTitle());
        course.setDuration(updateCourse.getDuration());
        session.update(course);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Session session) {
        int id = enterId();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        session.delete(course);
        session.getTransaction().commit();
        session.close();
    }

    public static void selectAll(Session session) {
        session.beginTransaction();
        List<Course> courses = session.createQuery("SELECT u FROM Course u", Course.class).getResultList();
        courses.forEach(System.out::println);
        session.getTransaction();
        session.close();
    }
}