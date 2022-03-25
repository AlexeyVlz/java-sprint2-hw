package controllers;

import model.Records;

import java.util.*;

public class InMemoryHistoryManager <T extends Records> implements HistoryManager <T>  {

    final private List<T> history;
    final private Map<Integer, Node<T>> links;
    private Node<T> head = null;
    private Node<T> tail = null;
    int size = 0;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
        links = new HashMap<>();
    }

    @Override
    public void add(T task) {
        linkLast (task);
    }

    @Override
    public void remove(int id) {
        if(links.containsKey(id)) {
            removeNode(links.get(id));
        }
    }

    @Override
    public List<T> getHistory() {
        history.clear();
        getTasks();
        return history;
    }

    public void linkLast (T task) {
        if(links.containsKey(task.getId())) {
            removeNode(links.get(task.getId()));

        }
        if(size >= 10) {
            tail = tail.getPrev();
            tail.setNext(null);
            size--;
        }
        if (head == null) {
            head = new Node<T>(task);
            size++;
            links.put(task.getId(), head);
        } else if (tail == null) {
            tail = new Node<>(task, head);
            head.setNext(tail);
            size++;
            links.put(task.getId(), tail);
        } else {
            Node<T> newNode = new Node<>(task, tail);
            tail.setNext(newNode);
            tail = newNode;
            links.put(task.getId(), tail);
            size++;
        }
    }

    public void getTasks () {
        if(size == 1) {
            history.add(head.getValue());
        } else {
            Node <T> value = tail;
            for (int i = 0; i < size; i++) {
                history.add(value.getValue());
                value = value.getPrev();
            }
        }
    }

    public void removeNode (Node<T> node) {
        if (size <= 1) {
            head = null;
            size--;
        } else if (size == 2 && node.getPrev() == null) {
            head = head.getNext();
            head.setPrev(null);
            tail = null;
            size--;
        } else if (size == 2 && node.getNext() == null) {
            tail = null;
            head.setNext(null);
            size--;
        } else if(node.getPrev() == null) {
            head = head.getNext();
            head.getNext().setPrev(head);
            size--;
        } else if(node.getNext() == null) {
            tail = tail.getPrev();
            tail.getPrev().setNext(tail);
            size--;
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            size--;
        }
    }


}
