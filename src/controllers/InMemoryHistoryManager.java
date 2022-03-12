package controllers;

import model.Records;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager <T extends Records> implements HistoryManager <T>  {

    final private ArrayList<T> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void add(T task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.add(task);
            history.remove(0);
        }
    }

    @Override
    public List<T> getHistory() {
        return history;
    }
}
