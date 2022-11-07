import org.example.model.Data;
import org.example.model.mgr.DataMgr;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FetchTest {

    @Test
    public void fetchUsingCountTest() {
        List<Data> data = DataMgr.getInstance().fetchAllUsingCount();
        for (Data d : data) {
            System.out.println(d.getNumber());
        }
    }

    @Test
    public void fetchNoCountTest() throws InterruptedException {
        List<Data> data = DataMgr.getInstance().fetchAllNoCount();
        for (Data d : data) {
            System.out.println(d.getNumber());
        }
    }
}
