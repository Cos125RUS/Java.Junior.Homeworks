package org.example;

import org.example.ChatModels.Contact;
import org.example.ChatModels.Group;
import org.example.ChatModels.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:13306/Messenger";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "password";
    private static final String CONFIG = "hibernate.cfg.xml";
    private final SessionFactory sessionFactory;
    private final Logger logger;

    public DB(Logger logger) {
        this.logger = logger;
        newDB();
        sessionFactory = new Configuration()
                .configure(CONFIG)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(Group.class)
                .buildSessionFactory();
    }

    private void newDB() {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            String sqlUsers = "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT, ChatsList VARCHAR(255), Name VARCHAR(255))";
            String sqlContacts = "CREATE TABLE IF NOT EXISTS Contacts (id INT PRIMARY KEY AUTO_INCREMENT, user1 INT NOT NULL, user2 INT NOT NULL)";
            String sqlGroups = "CREATE TABLE IF NOT EXISTS `Groups` (id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(255), UsersList VARCHAR(255))";
            if (newState(connection, sqlUsers)) logger.log(Level.INFO, "sqlUsers Done");
            if (newState(connection, sqlContacts)) logger.log(Level.INFO, "sqlContacts Done");
            if (newState(connection, sqlGroups)) logger.log(Level.INFO, "sqlGroups Done");
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private boolean newState(Connection connection, String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.execute();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public void exit() {
        sessionFactory.close();
    }

    public <T> void create(T object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
        session.close();
        logger.log(Level.INFO, "New " + object.getClass().getSimpleName() +
                " add to DataBase (" + object + ")");
    }

    public <T> void update(T object){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(object);
        session.getTransaction().commit();
        session.close();
        logger.log(Level.INFO, "Delete data from DataBase " + object);
    }

    public <T> void delete(Class<T> clazz, long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(session.get(clazz, id));
        session.getTransaction().commit();
        session.close();
        logger.log(Level.INFO, "Delete from DataBase " + clazz.getSimpleName() +
                " by id: " + id);
    }

    public <T> T select(Class<T> clazz, long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T object = session.get(clazz, id);
        session.getTransaction().commit();
        session.close();
        return object;
    }

    public User getUserFromName(String name) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = null;
        try {
            user = session.createQuery("from User where name = :name", User.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        session.getTransaction().commit();
        session.close();
        return user;
    }

    public Contact getContactFromUsersId(long user1, long user2) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Contact contact = null;
        try {
            contact = session.createQuery("from Contact where user1 = :user1 and user2 = :user2",
                            Contact.class)
                    .setParameter("user1", user1)
                    .setParameter("user2", user2)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        session.getTransaction().commit();
        session.close();
        return contact;
    }
}
