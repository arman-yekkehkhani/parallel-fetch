package org.example.model.dao;

import org.example.model.Data;
import org.example.model.Page;
import org.example.model.dto.Pageable;
import org.example.model.DataPage;

import java.util.ArrayList;
import java.util.List;

public class DataDAO implements Repository<Data> {
    private static DataDAO instance;

    private List<Data> dataList;

    private DataDAO() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 93; i++) {
            dataList.add(new Data(i));
        }
    }

    public static DataDAO getInstance() {
        if (instance == null)
            instance = new DataDAO();
        return instance;
    }

    @Override
    public Page<Data> findAll(Pageable pageable) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startIdx = Math.min(currentPage * pageSize, dataList.size());
        int endIdx = Math.min((currentPage + 1) * pageSize, dataList.size());
        List<Data> data = dataList.subList(startIdx, endIdx);
        System.out.println("Current Thread: " + Thread.currentThread().getName() + " , page: " + currentPage);
        return new DataPage(data, currentPage, data.size());
    }

    @Override
    public long count() {
        return dataList.size();
    }
}
