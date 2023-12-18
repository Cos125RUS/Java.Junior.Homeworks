package org.example;

import org.hibernate.Session;

import java.sql.ResultSet;
import java.util.List;

public interface ImplCRUD<T> {
    void create(T obj);
    void update(T obj);
    void delete(T obj);
    T select(int id);
    List<Course> selectAll();
}
