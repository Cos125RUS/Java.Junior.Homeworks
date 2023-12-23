package org.example.DataBase;

import org.example.ChatModels.Contact;
import org.example.ChatModels.Group;
import org.example.ChatModels.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

    public <T>void create(T Object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(Object);
        session.getTransaction().commit();
        session.close();
    }

    public <T>void delete(Class<T> clazz, int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(session.get(clazz, id));
        session.getTransaction().commit();
        session.close();
    }

    public <T> T select(Class<T> clazz, int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T object = session.get(clazz, id);
        session.getTransaction().commit();
        session.close();
        return object;
    }
}
