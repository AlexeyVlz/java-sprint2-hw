package test;

//import controllers.FileBackedTasksManager;
import controllers.InMemoryTaskManager;
import controllers.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*public class FileBackedTasksManagerTests extends TaskManagerTest<FileBackedTasksManager>{

    FileBackedTasksManager taskManager;

    public FileBackedTasksManagerTests() {
        super(new FileBackedTasksManager(Paths.get("test.txt")));
    }

    @Test
    public void shouldSave() {
        taskManager = new FileBackedTasksManager(Paths.get("test.txt"));
        taskManager.getNewTask(new Task("Задача 1", Status.NEW, "Первая задача"));
        List<String> expected = new ArrayList<>();
        expected.add("id,type,name,status,description,epic");
        expected.add("1,TASK,Задача 1,NEW,Первая задача");
        expected.add("");
        List<String> saved = new ArrayList<>();
        try (FileReader reader = new FileReader(taskManager.getPath().getFileName().toString())) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                saved.add(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        Assertions.assertEquals(expected, saved);

        taskManager.getNewEpic(new Epic("Эпик 1", "Первый Эпик"));
        taskManager.getEpics().get(2).setStatus(Status.NEW);
        expected.add(expected.size() - 1, "2,EPIC,Эпик 1,NEW,Первый Эпик");
        saved.clear();
        try (FileReader reader = new FileReader(taskManager.getPath().getFileName().toString())) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                saved.add(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        Assertions.assertEquals(expected, saved);

        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2));
        expected.add(expected.size() - 1, "3,SUBTASK,Подзадача 1,NEW,Первая подзадача,2");
        saved.clear();
        try (FileReader reader = new FileReader(taskManager.getPath().getFileName().toString())) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                saved.add(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        Assertions.assertEquals(expected, saved);

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(2, 3);
        taskManager.getHistoryManager().getHistory();
        saved.clear();
        try (FileReader reader = new FileReader(taskManager.getPath().getFileName().toString())) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                saved.add(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        expected.add("1,2,3");
        Assertions.assertEquals(expected, saved);

        taskManager.removeTask(1);
        saved.clear();
        try (FileReader reader = new FileReader(taskManager.getPath().getFileName().toString())) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                saved.add(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        expected.add("2,3");
    }

    @Test
    public void shouldLoadFromFile() {
        taskManager = new FileBackedTasksManager(Paths.get("noStatus.txt"));
        ManagerSaveException ex = Assertions.assertThrows(ManagerSaveException.class,
                () -> taskManager.loadFromFile (Paths.get(taskManager.getPath().getFileName().toString())));
        Assertions.assertEquals("Нет данных для восстановления статуса объекта", ex.getMessage());

        taskManager = new FileBackedTasksManager(Paths.get("lackOfData.txt"));
        ArrayIndexOutOfBoundsException ex1 = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> taskManager.loadFromFile (Paths.get(taskManager.getPath().getFileName().toString())));
        Assertions.assertEquals("Недостаточно данных для восстановления объекта", ex1.getMessage());

        taskManager = new FileBackedTasksManager(Paths.get("noType.txt"));
        ManagerSaveException ex2 = Assertions.assertThrows(ManagerSaveException.class,
                () -> taskManager.loadFromFile (Paths.get(taskManager.getPath().getFileName().toString())));
        Assertions.assertEquals("Нет данных для определения типа объекта", ex2.getMessage());

        taskManager = new FileBackedTasksManager(Paths.get("test.txt"));
        FileBackedTasksManager manager = taskManager.loadFromFile (Paths.get(taskManager.getPath().getFileName().toString()));

        Epic epic = new Epic("Эпик 1", "Первый Эпик");
        epic.setStatus(Status.NEW);
        epic.setId(2);
        Subtask subtask = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2);
        subtask.setId(3);

        Assertions.assertEquals(subtask, manager.getEpics().get(2).getSubtasks().get(3));
        Assertions.assertEquals(epic, manager.getEpics().get(2));

        List<Records> shouldBeHistory = new ArrayList<>();
        shouldBeHistory.add(epic);
        shouldBeHistory.add(subtask);
        Assertions.assertEquals(shouldBeHistory, manager.getHistoryManager().getHistory());
    }

}*/
