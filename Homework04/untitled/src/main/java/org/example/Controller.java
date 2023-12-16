package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.ResultSet;

public class Controller implements ImplCRUD<Course>, Runnable {
    private boolean isRun;
    private int choice = -1;


    public void useDB(int choice){
        this.choice = choice;
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
    public ResultSet selectAll() {
        return null;
    }

    @Override
    public void run() {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {
            isRun = true;
            while (isRun) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
