package test;

import controllers.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

class EpicTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    public void beforeEach() {
        if(!taskManager.getEpics().isEmpty()) {
            taskManager.getEpics().clear();
        }
    }

    @Test
    public void statusShouldBeNewIfSubtasksEmpty() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        Assertions.assertEquals(Status.valueOf("NEW"), taskManager.getEpics().get(1).getStatus(),
                "Статус не совпадает");
    }

    @Test
    public void statusShouldBeNewIfAllSubtasksHasStatusNew() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60)));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.valueOf("NEW"), taskManager.getEpics().get(1).getStatus(),
                "Статус не совпадает");
    }

    @Test
    public void statusShouldBeDoneIfAllSubtasksHasStatusDone() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.DONE, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60)));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.DONE, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.valueOf("DONE"), taskManager.getEpics().get(1).getStatus(),
                "Статус не совпадает");
    }

    @Test
    public void statusShouldBeIn_ProgressIfSubtasksHasStatusNewAndDone() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60)));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.DONE, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), taskManager.getEpics().get(1).getStatus(),
                "Статус не совпадает");
    }

    @Test
    public void statusShouldBeIn_ProgressIfSubtasksHasStatusIn_Progress() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,1,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.IN_PROGRESS, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60)));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.IN_PROGRESS, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), taskManager.getEpics().get(1).getStatus(),
                "Статус не совпадает");
    }


}