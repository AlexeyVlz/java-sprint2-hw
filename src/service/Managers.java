package service;

import model.Records;

import java.io.IOException;

public class Managers {

    public static HTTPTaskManager getDefault(String url) throws IOException, InterruptedException {
        return new HTTPTaskManager(url);
    }

    public static HistoryManager<Records> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }

}
