package service;

import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ObjectsForTestsServer {

    public Task firstTask() {
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой задачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow")); // Стартовое время второй задачи
                Task task = new Task("Задача 1", Status.NEW,"Первая", startTimeFirstTask,
                        Duration.ofMinutes(60));
                task.setId(1);
        return task;
    }

    public Task secondTask() {
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        Task task = new Task("Задача 2", Status.NEW,"Вторая", startTimeSecondTask,
                Duration.ofMinutes(60));
        task.setId(2);
        return task;
    }

    public Subtask firstSubtask() {
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,6,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60));
    }

    public Subtask secondSubtask() {
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,6,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60));
    }

    public Subtask thirdSubtask() {
        ZonedDateTime startTimeThirdTask = ZonedDateTime.of(  // Стартовое время третьей подзадачи
                LocalDateTime.of(2022,6,1,15,0,0,0),
                ZoneId.of("Europe/Moscow"));
        return new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 1,
                startTimeThirdTask, Duration.ofMinutes(60));
    }
}
