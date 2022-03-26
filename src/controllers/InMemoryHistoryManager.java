package controllers;

import model.Records;

import java.util.*;

public class InMemoryHistoryManager <T extends Records> implements HistoryManager <T>  {

    final private List<T> history;
    final private Map<Integer, Node<T>> links;
    private Node<T> head = null;
    private Node<T> tail = null;

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
        if (head == null) {
            head = new Node<T>(task);
            links.put(task.getId(), head);
        } else if (tail == null) {
            tail = new Node<>(task, head);
            head.setNext(tail);
            links.put(task.getId(), tail);
        } else {
            Node<T> newNode = new Node<>(task, tail);
            tail.setNext(newNode);
            tail = newNode;
            links.put(task.getId(), tail);
        }
    }

    public void getTasks () {
        Node<T> node = head;
        while (node != null){
            history.add(node.getValue());
            node = node.getNext();
        }
    }

    public void removeNode (Node<T> node) {
        if(node.getPrev() == null) {
            head = head.getNext();
            head.setPrev(null);
        } else if (node.getNext() == null) {
            tail = tail.getPrev();
            tail.setNext(null);
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
    }

}
