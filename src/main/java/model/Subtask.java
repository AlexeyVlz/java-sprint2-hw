package model;

import controllers.Types;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Subtask extends Records {
    int epicId;

    public Subtask(String title, Status status, String specification, int epicId, ZonedDateTime startTime,
                   Duration duration) {
        super(title, status, 0, specification, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId && Objects.equals(specification, subtask.specification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, specification);
    }

    @Override
    public String toString() {
        return id + "," + Types.SUBTASK + "," + title + "," + status + "," + specification + "," +
                startTime + "," + duration + "," + endTime + "," + epicId;
    }
}
