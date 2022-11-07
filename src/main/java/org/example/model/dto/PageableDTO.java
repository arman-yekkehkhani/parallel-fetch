package org.example.model.dto;

public class PageableDTO implements Pageable {

    private int pageNumber;
    private int pageSize;

    public PageableDTO(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Pageable next() {
        return new PageableDTO(pageNumber + 1, pageSize);
    }
}
