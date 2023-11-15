package org.example.service;

import java.sql.SQLException;

public interface SimpleService<T> {
    T save(T entity) throws SQLException;

    T findById(Long id) throws SQLException;
}
