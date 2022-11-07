package org.example.model.dto;

public interface Pageable {
    int getPageNumber();
    int getPageSize();
    Pageable next();
}
