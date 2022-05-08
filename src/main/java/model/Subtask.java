package model;

import controllers.Types;

import java.util.Objects;

public class Subtask extends Records {
    int epicId;
    final private String specification;

    public Subtask(String title, Status status, String specification, int epicId) {
        super(title, status, 0);
        this.epicId = epicId;
        this.specification = specification;
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
        return id + "," + Types.SUBTASK + "," + title + "," + status + "," + specification + "," + epicId;
    }
}
