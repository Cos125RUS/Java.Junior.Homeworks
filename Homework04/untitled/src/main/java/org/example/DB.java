package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.ResultSet;
import java.util.List;

public class DB implements ImplCRUD<Course> {
    SessionFactory sessionFactory;

    public DB() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();
    }

    public void exit(){
        sessionFactory.close();
    }


    @Override
    public void create(Course obj) {

    }

    @Override
    public void update(Course obj) {

    }

    @Override
    public void delete(Course obj) {

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
