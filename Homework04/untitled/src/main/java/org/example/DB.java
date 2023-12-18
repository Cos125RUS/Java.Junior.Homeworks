package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.List;

public class DB implements ImplCRUD<Course> {
    SessionFactory sessionFactory;

    public DB() {
        newDB();
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();
    }

    private void newDB(){
        String url = "jdbc:mysql://localhost:33306/schoolDB";
        String user = "root";
        String password = "password";
        try (Connection connection = DriverManager.getConnection(url, user, password)){
            String sql = "CREATE TABLE IF NOT EXISTS Courses (id INT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(255), duration DOUBLE)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit(){
        sessionFactory.close();
    }


    @Override
    public void create(Course course) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(course);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(int id, String title, double duration) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        course.setTitle(title);
        course.setDuration(duration);
        session.update(course);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        session.delete(course);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Course select(int id) {
        return null;
    }

    @Override
    public List<Course> selectAll() {
        List<Course> courses;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            courses = session.createQuery("SELECT u FROM Course u", Course.class).getResultList();
            session.getTransaction();
        }
        return courses;
    }
}
