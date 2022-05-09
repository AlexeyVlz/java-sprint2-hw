package model;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import controllers.Types;

public class Task extends Records {


    public Task(String title, Status status, String specification, ZonedDateTime startTime,
                Duration duration) {
        super(title, status, 0, specification, startTime, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(toString(), task.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }

    @Override
    public String toString() {
        return id + "," + Types.TASK + "," + title + "," + status + "," + specification +
                startTime + "," + duration + "," + endTime;
    }

}
