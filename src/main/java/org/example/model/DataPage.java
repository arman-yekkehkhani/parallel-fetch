package org.example.model;

import org.example.model.dto.Pageable;
import org.example.model.dao.DataDAO;
import org.example.model.dto.PageableDTO;

import java.util.List;

public class DataPage implements Page<Data> {

    private int pageNumber;
    private int pageSize;
    private List<Data> dataList;

    public DataPage(List<Data> dataList, int pageNumber, int pageSize) {
        this.dataList = dataList;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getNumber() {
        return pageNumber;
    }

    @Override
    public int getSize() {
        return pageSize;
    }

    @Override
    public List<Data> getContent() {
        return this.dataList;
    }

    @Override
    public boolean hasContent() {
        return dataList.size() != 0;
    }

    @Override
    public boolean hasNext() {
        if (DataDAO.getInstance().count() <= (long) (pageNumber + 1) * pageSize) {
            return false;
        }
        return true;
    }

    @Override
    public Pageable nextPageable() {
        return new PageableDTO(pageNumber + 1, pageSize);
    }
}
