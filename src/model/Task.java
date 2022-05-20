package model;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import service.Types;

public class Task extends Records {

    //Types type = Types.TASK;


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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy;MM;dd;HH;mm;ss");
        return id + "," + Types.TASK + "," + title + "," + status + "," + specification + "," +
                formatter.format(startTime) + "," + (int) duration.toMinutes();
    }

}
