package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ObjectsForTests {

    public Task firstTask() {
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой задачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow")); // Стартовое время второй задачи
        return new Task("Задача 1", Status.NEW,"Первая", startTimeFirstTask,
                Duration.ofMinutes(60));
    }

    public Task secondTask() {
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Task("Задача 2", Status.NEW, "Вторая", startTimeSecondTask,
                Duration.ofMinutes(60));
    }

    public Subtask firstSubtask() {
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60));
    }

    public Subtask secondSubtask() {
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60));
    }

    public Subtask thirdSubtask() {
        ZonedDateTime startTimeThirdTask = ZonedDateTime.of(  // Стартовое время третьей подзадачи
                LocalDateTime.of(2022,5,1,15,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 1,
                startTimeThirdTask, Duration.ofMinutes(60));
    }

    /*public Epic getNewEpicWithTwoSubtasks() {
        Epic epic = new Epic("Эпик 1", "Первый");
        epic.setId(1);
        epic.setStatus(Status.NEW);
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        Subtask subtask = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60));
        subtask.setId(2);
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        Subtask subtask1 = new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60));
        subtask1.setId(3);
        epic.getSubtasks().put(subtask.getId(), subtask);
        epic.getSubtasks().put(subtask1.getId(),subtask1);
        epic.setStartTime();
        epic.setDuration();
        epic.setEndTime();
        return  epic;
    }*/
}
