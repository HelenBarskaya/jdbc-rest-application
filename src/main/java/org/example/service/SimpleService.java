package org.example.service;

import java.util.UUID;

public interface SimpleService<T> {
    T save(T entity);

    T findById(UUID uuid);
}
