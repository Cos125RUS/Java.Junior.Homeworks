package org.example;

import java.sql.ResultSet;

public interface ImplCRUD<T> {
    void create(T obj);
    void update(T obj);
    void delete(T obj);
    T select(int id);
    ResultSet selectAll();
}
