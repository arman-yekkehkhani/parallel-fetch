package org.example;

import org.example.model.dto.PageableDTO;
import org.example.model.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.example.Main.THREAD_COUNTS;


public class NoCountMain {
    volatile static boolean isReady;

    public static void main(String[] args) throws InterruptedException {
        int pageSize = 5;

        for (int i = 0; i < THREAD_COUNTS; i++) {
            Main.queue.put(new PageableDTO(i, pageSize));
        }

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNTS; i++) {
            NoCountDataConsumer dataConsumer = new NoCountDataConsumer();
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
}
