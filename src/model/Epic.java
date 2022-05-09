package model;

import service.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;


public class Epic extends Records {
    final private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String specification) {
        super(title, 0, specification);
        this.subtasks = new HashMap<>();
        this.duration = calculateDuration();
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
        if(!subtasks.isEmpty()) {
            startTime = ZonedDateTime.of(
                    LocalDateTime.of(9999, 1, 1, 0, 0, 0, 0),
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

    @Override
    public ZonedDateTime getEndTime() {
        try {
            return startTime.plus(duration);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public  void setEndTime() {
        this.endTime = getEndTime();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy;MM;dd;HH;mm;ss");
        String stringStartTime;
        if(startTime != null){
            stringStartTime = formatter.format(startTime);
        } else {
            stringStartTime = "null";
        }
        return id + "," + Types.EPIC + "," + title + "," + status + "," + specification + "," +
                stringStartTime + "," + (int) duration.toMinutes();
    }
}
