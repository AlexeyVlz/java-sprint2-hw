package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    final private ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.add(task);
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
