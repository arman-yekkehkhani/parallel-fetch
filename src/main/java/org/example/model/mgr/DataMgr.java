package org.example.model.mgr;

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
import java.util.stream.Collectors;

public class DataMgr {
    public static final int PAGE_SIZE = 5;
    public static final int THREAD_COUNTS = 2;

    static BlockingDeque<Pageable> queue;
    static ConcurrentLinkedQueue<Data> retrievedDataQueue;

    volatile static boolean isReady;

    private static DataMgr instance;

    private DataMgr() {
    }

    public static DataMgr getInstance() {
        if (instance == null)
            instance = new DataMgr();
        return instance;
    }

    private void init() {
        queue = new LinkedBlockingDeque<>(100);
        retrievedDataQueue = new ConcurrentLinkedQueue<>();
        isReady = false;
    }

    public List<Data> fetchAllUsingCount() {
        init();

        populatePageableQueue_count();
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNTS; i++) {
            PageableConsumer consumer = new PageableConsumer("consumer thread - " + i);
            consumer.start();
            threadList.add(consumer);
        }
        isReady = true;

        waitUntilCompletion(threadList);

        return retrievedDataQueue.stream()
                .sorted(Comparator.comparingInt(Data::getNumber))
                .collect(Collectors.toList());

    }

    public List<Data> fetchAllNoCount() throws InterruptedException {
        init();

        for (int i = 0; i < THREAD_COUNTS; i++) {
            queue.put(new PageableDTO(i, PAGE_SIZE));
        }

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNTS; i++) {
            NoCountPageableConsumer consumer = new NoCountPageableConsumer("consumer thread - " + i);
            consumer.start();
            threadList.add(consumer);
        }
        isReady = true;

        waitUntilCompletion(threadList);

        return retrievedDataQueue.stream()
                .sorted(Comparator.comparingInt(Data::getNumber))
                .collect(Collectors.toList());
    }

    private void waitUntilCompletion(List<Thread> threadList) {
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
    }

    private void populatePageableQueue_count() {
        long total = DataDAO.getInstance().count();
        int pageNum = 0;
        while ((long) pageNum * DataMgr.PAGE_SIZE < total) {
            queue.add(new PageableDTO(pageNum, DataMgr.PAGE_SIZE));
            pageNum++;
        }
    }
}
