package org.example.model.mgr;

import org.example.model.Data;
import org.example.model.Page;
import org.example.model.dao.DataDAO;
import org.example.model.dto.Pageable;
import org.example.model.dto.PageableDTO;

public class NoCountPageableConsumer extends Thread {

    public NoCountPageableConsumer(String name) {
        super(name);
    }

    public void run() {
        while (true) {
            if (!DataMgr.isReady)
                continue;

            if (DataMgr.queue.isEmpty())
                return;

            try {
                Pageable pageable = DataMgr.queue.take();
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
        DataMgr.retrievedDataQueue.addAll(dataPage.getContent());
        DataMgr.queue.put(new PageableDTO(pageable.getPageNumber() + DataMgr.THREAD_COUNTS, pageable.getPageSize()));
    }
}
