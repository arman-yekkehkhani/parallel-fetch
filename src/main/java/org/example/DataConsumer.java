package org.example;

import org.example.model.Data;
import org.example.model.Page;
import org.example.model.dao.DataDAO;
import org.example.model.dto.Pageable;

public class DataConsumer extends Thread {

    public void run() {
        while (true) {
            if (!Main.isReady)
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
        Page<Data> all = DataDAO.getInstance().findAll(pageable);
        if (all.getSize() == 0)
            return;
        Main.retrievedDataQueue.addAll(all.getContent());
    }
}
