package model;

import java.util.Objects;

public class Task {
    private String title ="";
    private String specification;
    protected Status status;
    private int id;

    public Task(String title, String specification, Status status) {
        this.title = title;
        this.specification = specification;
        this.status = status;
        id = 0;
    }

    public Task(String title) {
        this.title = title;
        id = 0;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(specification, task.specification) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, specification, status, id);
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "title='" + title + '\'' +
                ", specification='" + specification + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
