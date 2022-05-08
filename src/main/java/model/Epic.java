package model;

import controllers.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Objects;


public class Epic extends Records {
    final private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String specification) {
        super(title, 0, specification);
        this.subtasks = new HashMap<>();
        this.startTime = calculateStartTime();
        this.duration = calculateDuration();
        this.endTime = getEndTime();
    }

    public Duration calculateDuration() {
        long minutes = 0;
        for(Subtask subtask : subtasks.values()){
            minutes = minutes + subtask.getDuration().toMinutes();
        }
        duration = Duration.ofMinutes(minutes);
        return duration;
    }

    public void setDuration () {
        this.duration = calculateDuration();
    }

    public ZonedDateTime calculateStartTime() {
        startTime = ZonedDateTime.of(
                LocalDateTime.of(0,0,0,0,0,0,0),
                ZoneId.of("Europe/Moscow"));
        if(!subtasks.isEmpty()) {
            startTime = ZonedDateTime.of(
                    LocalDateTime.of(9999, 0, 0, 0, 0, 0, 0),
                    ZoneId.of("Europe/Moscow"));
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
            }
        }
        return startTime;
    }

    public  void setStartTime() {
        this.startTime = calculateStartTime();
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(toString(), epic.toString());
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
