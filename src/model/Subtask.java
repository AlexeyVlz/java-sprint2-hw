package model;

public class Subtask extends Task {
    int epicId;
    public Subtask (String title, String specification, Status status, int epicId) {
        super(title, specification, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
