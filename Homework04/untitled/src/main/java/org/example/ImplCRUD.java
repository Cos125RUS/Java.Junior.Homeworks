package org.example;

import org.hibernate.Session;

import java.sql.ResultSet;
import java.util.List;

public interface ImplCRUD<T> {
    void create(T obj);
    void update(int id, String title, double duration);
    void delete(int id);
    T select(int id);
    List<Course> selectAll();
}
