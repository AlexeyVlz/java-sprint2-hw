package controllers;

import model.Records;

import java.util.List;

public interface HistoryManager <T extends Records> {
    void add(T task);
    void remove (int id);
    List<T> getHistory();
}
