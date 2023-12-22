package org.example.DataBase;

import org.example.ChatModels.Contact;
import org.example.ChatModels.Group;
import org.example.ChatModels.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private SessionFactory sessionFactory;
    private Logger logger;

    public DB(Logger logger) {
        this.logger = logger;
        newDB();
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(Group.class)
                .buildSessionFactory();
    }

    private void newDB(){
        String url = "jdbc:mysql://localhost:13306/Messenger";
        String user = "root";
        String password = "password";
        try (Connection connection = DriverManager.getConnection(url, user, password)){
            String sql = "";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.execute();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void exit(){
        sessionFactory.close();
    }

}
