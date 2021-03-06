package service;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerTests {

    InMemoryHistoryManager<Records> inMemoryHistoryManager = new InMemoryHistoryManager<>();

    static ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
            LocalDateTime.of(2022,5,1,9,0,0,0),
            ZoneId.of("Europe/Moscow"));
    static ZonedDateTime startTimeFirstSubtask = ZonedDateTime.of(  // Стартовое время второй подзадачи
            LocalDateTime.of(2022,5,1,11,0,0,0),
            ZoneId.of("Europe/Moscow"));
    static ZonedDateTime startTimeSecondSubtask = ZonedDateTime.of(  // Стартовое время второй подзадачи
            LocalDateTime.of(2022,5,1,15,0,0,0),
            ZoneId.of("Europe/Moscow"));
    static Task task = new Task("Задача 1", Status.NEW,"Первая", startTimeFirstTask,
            Duration.ofMinutes(60));
    static Epic epic1 = new Epic("Эпик 1", "Первый эпик");
    static Epic epic2 = new Epic("Эпик 2", "Второй эпик");
    static Subtask subtask1 = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 3,
            startTimeFirstSubtask, Duration.ofMinutes(60));
    static Subtask subtask2 = new Subtask("Подзадача 2", Status.IN_PROGRESS, "Вторая подзадача", 3,
            startTimeSecondSubtask, Duration.ofMinutes(60));

    @BeforeAll
    public static void beforeAll() {
        task.setId(1);
        epic1.setId(2);
        epic1.setStatus(Status.NEW);
        epic2.setId(3);
        epic2.setStatus(Status.IN_PROGRESS);
        subtask1.setId(4);
        subtask2.setId(5);
    }

    @Test
    public void shouldAddAndRemoveTasks() {
        inMemoryHistoryManager.add(task);
        List<Records> list = new ArrayList<>();
        list.add(task);
        Assertions.assertEquals(list, inMemoryHistoryManager.getHistory());
        inMemoryHistoryManager.add(epic1);
        list.add(epic1);
        Assertions.assertEquals(list, inMemoryHistoryManager.getHistory());
        inMemoryHistoryManager.add(epic2);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask2);
        list.add(epic2);
        list.add(subtask1);
        list.add(subtask2);
        Assertions.assertEquals(list, inMemoryHistoryManager.getHistory());

        inMemoryHistoryManager.remove(3);
        list.remove(2);
        Assertions.assertEquals(list, inMemoryHistoryManager.getHistory());
        inMemoryHistoryManager.remove(1);
        list.remove(0);
        Assertions.assertEquals(list, inMemoryHistoryManager.getHistory());
        inMemoryHistoryManager.remove(5);
        list.remove(2);
        Assertions.assertEquals(list, inMemoryHistoryManager.getHistory());
    }
}
