package model;

import java.util.HashMap;
import java.util.Objects;


public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;


    public Epic(String title) {
        super(title);
        this.subtasks = new HashMap<>();
        status = Status.NEW;
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
        return Objects.equals(subtasks, epic.subtasks) && Objects.equals(status, epic.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks, status);
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "subtasks=" + subtasks +
                ", status='" + status + '\'' +
                '}';
    }
}
