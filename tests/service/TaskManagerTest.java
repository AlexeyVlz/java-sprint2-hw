package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TaskManagerTest <T extends TaskManager> {
    T taskManager;
    ObjectsForTests objectsForTests;

    public TaskManagerTest (T taskManager) {
        this.taskManager = taskManager;
        this.objectsForTests = new ObjectsForTests();
    }

    @Test
    public void shouldGiveTasksList() {
        Assertions.assertTrue(taskManager.getTasksList().isEmpty());

        List<Task> newTasks = new ArrayList<>();
        newTasks.add(objectsForTests.firstTask());
        newTasks.add(objectsForTests.secondTask());
        newTasks.get(0).setId(1);
        newTasks.get(1).setId(2);
        taskManager.getNewTask(objectsForTests.firstTask());
        taskManager.getNewTask(objectsForTests.secondTask());
        Assertions.assertEquals(newTasks, taskManager.getTasksList(), "Списки не совпадают");
    }

    @Test
    public void tasksShouldBeEmpty() {
        taskManager.getNewTask(objectsForTests.firstTask());
        taskManager.getNewTask(objectsForTests.secondTask());
        boolean isEmpty = taskManager.clearTasksList().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    public void shouldGetTaskById() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getTaskById (777));
        Assertions.assertEquals("Задача не найдена", ex.getMessage());

        Task task = objectsForTests.firstTask();
        Task task1 = objectsForTests.secondTask();
        task.setId(1);
        task1.setId(2);
        taskManager.getNewTask(objectsForTests.firstTask());
        taskManager.getNewTask(objectsForTests.secondTask());
        Assertions.assertEquals(task, taskManager.getTaskById(1));
        Assertions.assertEquals(task1, taskManager.getTaskById(2));
    }

    @Test
    public void shouldCreateNewTask() {
        Task task = objectsForTests.firstTask();
        task.setId(1);
        taskManager.getNewTask(objectsForTests.firstTask());
        Assertions.assertEquals(task, taskManager.getTasks().get(1));

        ManagerSaveException ex = Assertions.assertThrows(ManagerSaveException.class,
                () -> taskManager.getNewTask(objectsForTests.firstTask()));
        Assertions.assertEquals("Время выполнения задачи пересекается со временем Задача 1", ex.getMessage());
    }

    @Test
    public void shouldUpdateTask() {
        taskManager.getNewTask(objectsForTests.firstTask());
        Task task = objectsForTests.firstTask();
        task.setId(1);
        Task task2 = objectsForTests.secondTask();
        task2.setId(2);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateTask(task2));
        Assertions.assertEquals("Такая задача не создавалась", ex.getMessage());
        Assertions.assertEquals(task, taskManager.updateTask(task).get(1));
    }

    @Test
    public void shouldRemoveTask() {
        taskManager.getNewTask(objectsForTests.firstTask());
        taskManager.getNewTask(objectsForTests.secondTask());
        taskManager.removeTask(1);
        Assertions.assertFalse(taskManager.getTasks().containsValue(taskManager.getTasks().get(1)));
    }

    @Test
    public void shouldGetSubtaskListByEpicId() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getSubtaskList(777));
        Assertions.assertEquals("Эпик не найден", ex.getMessage());

        List<Subtask> expect = new ArrayList<>();
        Subtask subtask1 = objectsForTests.firstSubtask();
        subtask1.setId(2);
        expect.add(subtask1);
        Subtask subtask2 = objectsForTests.secondSubtask();
        subtask2.setId(3);
        expect.add(subtask2);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(objectsForTests.firstSubtask());
        taskManager.getNewSubtask(objectsForTests.secondSubtask());
        Assertions.assertEquals(expect, taskManager.getSubtaskList(1));

        List<Subtask> list1 = new ArrayList<>();
        taskManager.getNewEpic(new Epic("Эпик 2", "Второй"));
        Assertions.assertEquals(list1, taskManager.getSubtaskList(4));
    }

    @Test
    public void shouldClearSubtasks() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.clearSubtasks(777));
        Assertions.assertEquals("Эпик не найден", ex.getMessage());

        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(objectsForTests.firstSubtask());
        taskManager.getNewSubtask(objectsForTests.secondSubtask());
        taskManager.clearSubtasks(1);
        Assertions.assertTrue(taskManager.getEpics().get(1).getSubtasks().isEmpty(), "Подзадачи не удалены");
    }

    @Test
    public void shouldGetSubtaskById() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getSubtaskById(777, 333));
        Assertions.assertEquals("Объект не найден", ex.getMessage());

        Subtask subtask = objectsForTests.firstSubtask();
        subtask.setId(2);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(objectsForTests.firstSubtask());
        taskManager.getNewSubtask(objectsForTests.secondSubtask());
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(1, 2));
    }

    @Test
    public void shouldGetNewSubtask() {
        Subtask subtask = objectsForTests.firstSubtask();
        subtask.setId(777);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getNewSubtask(subtask));
        Assertions.assertEquals("Объект не найден", ex.getMessage());

        taskManager.setId(0);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        Subtask subtask1 = objectsForTests.firstSubtask();
        subtask1.setId(2);
        taskManager.getNewSubtask(subtask1);
        Assertions.assertEquals(subtask1, taskManager.getEpics().get(1).getSubtasks().get(2));

        ManagerSaveException ex1 = Assertions.assertThrows(ManagerSaveException.class,
                () -> taskManager.getNewSubtask(subtask1));
        Assertions.assertEquals("Время выполнения задачи пересекается со временем Подзадача 1", ex1.getMessage());
    }


    @Test
    public void shouldUpdateSubtask() {
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,2,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,2,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeThirdTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,2,15,0,0,0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));

        taskManager.getNewSubtask(objectsForTests.firstSubtask());
        taskManager.getNewSubtask(objectsForTests.secondSubtask());
        Subtask subtask = objectsForTests.thirdSubtask();
        subtask.setId(777);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateSubtask(subtask));
        Assertions.assertEquals("Такая подзадача не создавалась", ex.getMessage());
        Subtask subtask1 = new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 333,
                startTimeThirdTask, Duration.ofMinutes(60));
        NullPointerException ex1 = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateSubtask(subtask1));
        Assertions.assertEquals("Эпик не найден", ex1.getMessage());

        taskManager.getNewEpic(new Epic("Эпик 2", "Первый"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 4,
                startTimeFirstTask, Duration.ofMinutes(60)));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 4,
                startTimeSecondTask, Duration.ofMinutes(60)));
        Subtask subtask2 = new Subtask("Обновленная подзадача 1", Status.NEW,
                "Обновленная первая подзадача", 4, startTimeFirstTask, Duration.ofMinutes(60));
        subtask2.setId(5);
        taskManager.updateSubtask(subtask2);
        Assertions.assertEquals(subtask2, taskManager.getEpics().get(4).getSubtasks().get(5));
    }

    @Test
    public void shouldRemoveSubtaskById() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.removeSubtaskById(777, 333));
        Assertions.assertEquals("Объект не найден", ex.getMessage());

        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(objectsForTests.firstSubtask());
        taskManager.getNewSubtask(objectsForTests.secondSubtask());
        Subtask subtask = taskManager.getEpics().get(1).getSubtasks().get(2);
        taskManager.removeSubtaskById(1, 2);
        Assertions.assertFalse(taskManager.getEpics().get(1).getSubtasks().containsKey(subtask.getId()));
    }

    @Test
    public void shouldGetEpicsList() {
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty());

        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,1,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        List<Epic> newEpics = new ArrayList<>();
        newEpics.add(new Epic("Эпик 1", "Первый"));
        newEpics.add(new Epic("Эпик 2", "Второй"));
        newEpics.get(0).setId(1);
        newEpics.get(0).setStatus(Status.NEW);
        newEpics.get(1).setId(2);
        newEpics.get(1).setStatus(Status.NEW);
        Subtask subtask = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2,
                startTimeFirstTask, Duration.ofMinutes(60));
        newEpics.get(1).getSubtasks().put(subtask.getId(), subtask);
        newEpics.get(1).setDuration();
        newEpics.get(1).setStartTime();
        newEpics.get(1).setEndTime();
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewEpic(new Epic("Эпик 2", "Второй"));
        taskManager.getNewSubtask(subtask);
        Assertions.assertEquals(newEpics, taskManager.getEpicsList(), "Списки не совпадают");
    }

    @Test
    public void shouldClearEpicsList() {
        taskManager.clearEpicsList();
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    public void shouldGetEpicById() {
        Epic epic = new Epic("Эпик 1", "Первый");
        epic.setId(1);
        epic.setStatus(Status.NEW);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getEpicById(777));
        Assertions.assertEquals("Объект не найден", ex.getMessage());
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    public void shouldGetNewEpic() {
        Epic epic = new Epic("Эпик 1", "Первый");
        epic.setId(1);
        epic.setStatus(Status.NEW);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        Assertions.assertEquals(epic, taskManager.getEpics().get(1));
    }

    @Test
    public void shouldUpdateEpic() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewEpic(new Epic("Эпик 2", "Второй"));
        Epic epic = new Epic("Эпик 1", "Первый");
        epic.setId(777);
        epic.setStatus(Status.NEW);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateEpic(epic));
        Assertions.assertEquals("Такой эпик не создавался", ex.getMessage());

        Epic epic1 = new Epic("Измененный Эпик 1", "Первый");
        epic1.setId(1);
        epic1.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        Assertions.assertEquals(epic1, taskManager.getEpics().get(1));
    }

    @Test
    public void shouldRemoveEpic() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewEpic(new Epic("Эпик 2", "Второй"));
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.removeEpic(777));
        Assertions.assertEquals("Такой эпик не создавался", ex.getMessage());
        taskManager.removeEpic(1);
        Assertions.assertFalse(taskManager.getEpics().containsKey(1));
    }

    @Test
    public void shouldCalculateStatus() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        Assertions.assertEquals(Status.NEW, taskManager.getEpics().get(1).getStatus());
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022,5,2,9,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeSecondTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,2,11,0,0,0),
                ZoneId.of("Europe/Moscow"));
        ZonedDateTime startTimeThirdTask = ZonedDateTime.of(  // Стартовое время второй подзадачи
                LocalDateTime.of(2022,5,2,15,0,0,0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1,
                startTimeFirstTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.NEW, taskManager.getEpics().get(1).getStatus());
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.IN_PROGRESS, "Вторая подзадача", 1,
                startTimeSecondTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpics().get(1).getStatus());
        taskManager.getNewSubtask(new Subtask("Подзадача 3", Status.DONE, "Третья подзадача", 1,
                startTimeThirdTask, Duration.ofMinutes(60)));
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpics().get(1).getStatus());
        for (Subtask subtask : taskManager.getEpics().get(1).getSubtasks().values()){
            subtask.setStatus(Status.DONE);
        }
        taskManager.getEpics().get(1).setStatus(taskManager.calculateStatus(1));
        Assertions.assertEquals(Status.DONE, taskManager.getEpics().get(1).getStatus());
    }

}
