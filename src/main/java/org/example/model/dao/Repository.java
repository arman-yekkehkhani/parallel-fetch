package org.example.model.dao;

import org.example.model.Page;
import org.example.model.dto.Pageable;

public interface Repository<T> {
    Page<T> findAll(Pageable pageable);

    long count();
}

