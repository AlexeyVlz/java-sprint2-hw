package test;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import controllers.TaskManager;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

abstract class TaskManagerTest <T extends TaskManager> {
    T taskManager;

    public TaskManagerTest (T taskManager) {
        this.taskManager = taskManager;
    }

    @Test
    public void shouldGiveTasksList() {
        Assertions.assertTrue(taskManager.getTasksList().isEmpty());

        List<Task> newTasks = new ArrayList<>();
        newTasks.add(new Task("Задача 1", Status.NEW,"Первая"));
        newTasks.add(new Task("Задача 2", Status.NEW, "Вторая"));
        newTasks.get(0).setId(1);
        newTasks.get(1).setId(2);
        taskManager.getNewTask(new Task("Задача 1", Status.NEW, "Первая"));
        taskManager.getNewTask(new Task("Задача 2", Status.NEW, "Вторая"));
        Assertions.assertEquals(newTasks, taskManager.getTasksList(), "Списки не совпадают");
    }

    @Test
    public void tasksShouldBeEmpty() {
        boolean isEmpty = taskManager.clearTasksList().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    public void shouldGetTaskById() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getTaskById (777));
        Assertions.assertEquals("Задача не найдена", ex.getMessage());

        Task task = new Task("Задача 1", Status.NEW,"Первая");
        Task task1 = new Task("Задача 2", Status.NEW, "Вторая");
        task.setId(1);
        task1.setId(2);
        taskManager.getNewTask(new Task("Задача 1", Status.NEW, "Первая"));
        taskManager.getNewTask(new Task("Задача 2", Status.NEW, "Вторая"));
        Assertions.assertEquals(task, taskManager.getTaskById(1));
        Assertions.assertEquals(task1, taskManager.getTaskById(2));
    }

    @Test
    public void shouldCreateNewTask() {
        Task task = new Task("Задача 1", Status.NEW,"Первая");
        task.setId(1);
        taskManager.getNewTask(new Task("Задача 1", Status.NEW,"Первая"));
        Assertions.assertEquals(task, taskManager.getTasks().get(1));
    }

    @Test
    public void shouldUpdateTask() {
        taskManager.getNewTask(new Task("Задача 1", Status.NEW,"Первая"));
        Task task1 = new Task("Задача 2", Status.NEW, "Вторая");
        task1.setId(2);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateTask(task1));
        Assertions.assertEquals("Такая задача не создавалась", ex.getMessage());
        Task task = new Task("Задача 1", Status.IN_PROGRESS,"Обновленная");
        task.setId(1);
        taskManager.updateTask(task);
        Assertions.assertEquals(task, taskManager.getTasks().get(1));
    }

    @Test
    public void shouldRemoveTask() {
        taskManager.getNewTask(new Task("Задача 1", Status.NEW,"Первая"));
        taskManager.getNewTask(new Task("Задача 2", Status.NEW, "Вторая"));
        taskManager.removeTask(1);
        Assertions.assertFalse(taskManager.getTasks().containsValue(taskManager.getTasks().get(1)));
        taskManager.removeTask(2);
        Assertions.assertFalse(taskManager.getTasks().containsValue(taskManager.getTasks().get(2)));
    }

    @Test
    public void shouldGetSubtaskListByEpicId() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getSubtaskList(777));
        Assertions.assertEquals("Эпик не найден", ex.getMessage());

        List<Subtask> list = new ArrayList<>();
        Subtask subtask1 = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1);
        subtask1.setId(2);
        list.add(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1);
        subtask2.setId(3);
        list.add(subtask2);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1));
        Assertions.assertEquals(list, taskManager.getSubtaskList(1));

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
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1));
        taskManager.clearSubtasks(1);
        Assertions.assertTrue(taskManager.getEpics().get(1).getSubtasks().isEmpty(), "Подзадачи не удалены");
    }

    @Test
    public void shouldGetSubtaskById() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getSubtaskById(777, 333));
        Assertions.assertEquals("Объект не найден", ex.getMessage());

        Subtask subtask = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1);
        subtask.setId(2);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1));
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(1, 2));
    }

    @Test
    public void shouldGetNewSubtask() {
        Subtask subtask = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2);
        subtask.setId(777);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getNewSubtask(subtask));
        Assertions.assertEquals("Объект не найден", ex.getMessage());

        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        Subtask subtask1 = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2);
        subtask1.setId(3);
        taskManager.getNewSubtask(subtask1);
        Assertions.assertEquals(subtask1, taskManager.getEpics().get(2).getSubtasks().get(3));
     }


    @Test
    public void shouldUpdateSubtask() {
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1));
        Subtask subtask = new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 1);
        subtask.setId(777);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateSubtask(subtask));
        Assertions.assertEquals("Объект не найден", ex.getMessage());
        Subtask subtask1 = new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 333);
        NullPointerException ex1 = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.updateSubtask(subtask1));
        Assertions.assertEquals("Объект не найден", ex1.getMessage());

        taskManager.getNewEpic(new Epic("Эпик 2", "Первый"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 4));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 4));
        Subtask subtask2 = new Subtask("Обновленная подзадача 1", Status.NEW,
                "Обновленная первая подзадача", 4);
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
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1));
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача", 1));
        Subtask subtask = taskManager.getEpics().get(1).getSubtasks().get(2);
        taskManager.removeSubtaskById(1, 2);
        Assertions.assertFalse(taskManager.getEpics().get(1).getSubtasks().containsKey(subtask.getId()));
    }

    @Test
    public void shouldGetEpicsList() {
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty());

        List<Epic> newEpics = new ArrayList<>();
        newEpics.add(new Epic("Эпик 1", "Первый"));
        newEpics.add(new Epic("Эпик 2", "Второй"));
        newEpics.get(0).setId(1);
        newEpics.get(0).setStatus(Status.NEW);
        newEpics.get(1).setId(2);
        newEpics.get(1).setStatus(Status.NEW);
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        taskManager.getNewEpic(new Epic("Эпик 2", "Второй"));
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
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.calculateStatus(777));
        Assertions.assertEquals("Такой эпик не создавался", ex.getMessage());
        taskManager.getNewEpic(new Epic("Эпик 1", "Первый"));
        Assertions.assertEquals(Status.NEW, taskManager.getEpics().get(1).getStatus());
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 1));
        Assertions.assertEquals(Status.NEW, taskManager.getEpics().get(1).getStatus());
        taskManager.getNewSubtask(new Subtask("Подзадача 2", Status.IN_PROGRESS, "Вторая подзадача",
                1));
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpics().get(1).getStatus());
        taskManager.getNewSubtask(new Subtask("Подзадача 3", Status.DONE, "Третья подзадача",
                1));
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpics().get(1).getStatus());
        for (Subtask subtask : taskManager.getEpics().get(1).getSubtasks().values()){
            subtask.setStatus(Status.DONE);
        }
        taskManager.getEpics().get(1).setStatus(taskManager.calculateStatus(1));
        Assertions.assertEquals(Status.DONE, taskManager.getEpics().get(1).getStatus());
    }

}
