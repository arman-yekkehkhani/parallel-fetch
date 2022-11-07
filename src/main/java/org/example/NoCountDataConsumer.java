package org.example;

import org.example.model.Page;
import org.example.model.dto.Pageable;
import org.example.model.dto.PageableDTO;
import org.example.model.Data;
import org.example.model.dao.DataDAO;

import static org.example.Main.THREAD_COUNTS;

public class NoCountDataConsumer extends Thread {

    public void run() {
        while (true) {
            if (!NoCountMain.isReady)
                continue;

            if (Main.queue.isEmpty())
                return;

            try {
                Pageable pageable = Main.queue.take();
                consume(pageable);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void consume(Pageable pageable) throws InterruptedException {
        Page<Data> dataPage = DataDAO.getInstance().findAll(pageable);
        if (dataPage.getSize() == 0)
            return;
        Main.retrievedDataQueue.addAll(dataPage.getContent());
        Main.queue.put(new PageableDTO(pageable.getPageNumber() + THREAD_COUNTS, pageable.getPageSize()));
    }
}
