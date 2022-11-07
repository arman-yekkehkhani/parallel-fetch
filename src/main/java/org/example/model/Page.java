package org.example.model;

import org.example.model.dto.Pageable;

import java.util.List;

public interface Page<T> {
    int getNumber();
    int getSize();
    List<T> getContent();
    boolean hasContent();
    boolean hasNext();
    Pageable nextPageable();
}
