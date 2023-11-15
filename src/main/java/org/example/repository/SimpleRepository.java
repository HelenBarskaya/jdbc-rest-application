package org.example.repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface SimpleRepository<T, K> {

    T findById(K id) throws SQLException;

    boolean deleteById(K id) throws SQLException;

    List<T> findAll() throws SQLException, IOException;

    T save(T t) throws SQLException;
}
