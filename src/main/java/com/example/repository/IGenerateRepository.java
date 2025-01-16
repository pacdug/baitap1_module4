package com.example.repository;

import java.util.List;

public interface IGenerateRepository<T> {
    List<T> findAll();

    T findById(Long id);

    void save(T t);

    void remove(Long id);
}
