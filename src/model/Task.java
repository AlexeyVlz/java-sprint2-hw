package model;

import java.util.Objects;
import controllers.Types;

public class Task extends Records {
    final private String specification;


    public Task(String title, Status status, String specification) {
        super(title, status, 0);
        this.specification = specification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(specification, task.specification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specification);
    }

    @Override
    public String toString() {
        return id + "," + Types.TASK + "," + title + "," + status + "," + specification;
    }

}
