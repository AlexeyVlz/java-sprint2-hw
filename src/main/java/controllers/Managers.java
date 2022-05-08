package controllers;

import model.Records;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager<Records> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }

}
