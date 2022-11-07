package org.example;

import org.example.model.Data;
import org.example.model.dao.DataDAO;
import org.example.model.dto.Pageable;
import org.example.model.dto.PageableDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static final int THREAD_COUNTS = 2;

    public static final BlockingDeque<Pageable> queue = new LinkedBlockingDeque<>(100);
    public static final ConcurrentLinkedQueue<Data> retrievedDataQueue = new ConcurrentLinkedQueue<>();

    volatile static boolean isReady;

    public static void main(String[] args) {
        int pageSize = 5;
        populatePageableQueue_count(pageSize);


        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNTS; i++) {
            DataConsumer dataConsumer = new DataConsumer();
            dataConsumer.start();
            threadList.add(dataConsumer);
        }
        isReady = true;

        while (true) {
            boolean noAliveThread = true;
            for (Thread thread : threadList) {
                if (thread.isAlive()) {
                    noAliveThread = false;
                    break;
                }
            }
            if (noAliveThread)
                break;
        }

        Main.retrievedDataQueue.stream()
                .sorted(Comparator.comparingInt(Data::getNumber))
                .forEach(System.out::println);
        
    }

    private static void populatePageableQueue_count(int pageSize) {
        long total = DataDAO.getInstance().count();
        int pageNum = 0;
        while ((long) pageNum * pageSize < total) {
            queue.add(new PageableDTO(pageNum, pageSize));
            pageNum++;
        }
    }
}