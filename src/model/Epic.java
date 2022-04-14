package model;

import controllers.Types;

import java.util.HashMap;
import java.util.Objects;


public class Epic extends Records {
    final private HashMap<Integer, Subtask> subtasks;
    final private String specification;

    public Epic(String title, String specification) {
        super(title, 0);
        this.subtasks = new HashMap<>();
        this.specification = specification;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return id + "," + Types.EPIC + "," + title + "," + status + "," + specification;
    }
}
