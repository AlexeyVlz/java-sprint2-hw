package test;

import controllers.InMemoryHistoryManager;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryHistoryManagerTests {

    InMemoryHistoryManager<Records> inMemoryHistoryManager = new InMemoryHistoryManager<>();

    static Task task = new Task("Задача 1", Status.NEW, "Первая задача");
    static Epic epic1 = new Epic("Эпик 1", "Первый эпик");
    static Epic epic2 = new Epic("Эпик 2", "Второй эпик");
    static Subtask subtask1 = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 3);
    static Subtask subtask2 = new Subtask("Подзадача 2", Status.IN_PROGRESS, "Вторая подзадача", 3);

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
