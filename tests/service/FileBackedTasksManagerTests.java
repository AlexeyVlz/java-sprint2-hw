package service;

import org.junit.jupiter.api.BeforeEach;
import service.FileBackedTasksManager;
import exceptions.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManagerTests extends TaskManagerTest<FileBackedTasksManager> {

    FileBackedTasksManager taskManager;

    public FileBackedTasksManagerTests() {
        super(new FileBackedTasksManager("test.txt"));
    }

    @Test
    public void shouldSave() {
        taskManager = new FileBackedTasksManager("test.txt");
        ZonedDateTime startTimeFirstTask = ZonedDateTime.of(  // Стартовое время первой задачи
                LocalDateTime.of(2022, 5, 1, 9, 0, 0, 0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewTask(new Task("Задача 1", Status.NEW, "Первая задача", startTimeFirstTask,
                Duration.ofMinutes(60)));
        List<String> expected = new ArrayList<>();
        expected.add("id,type,name,status,description,startTime,duration,epic");
        expected.add("1,TASK,Задача 1,NEW,Первая задача,2022;05;01;09;00;00,60");
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
        expected.add(expected.size() - 1, "2,EPIC,Эпик 1,NEW,Первый Эпик,null,0");
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

        ZonedDateTime startTimeFirstSubtask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022, 5, 1, 15, 0, 0, 0),
                ZoneId.of("Europe/Moscow"));
        taskManager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2,
                startTimeFirstSubtask, Duration.ofMinutes(60)));
        expected.remove(expected.size() - 2);
        expected.add(expected.size() - 1, "2,EPIC,Эпик 1,NEW,Первый Эпик,2022;05;01;15;00;00,60");
        expected.add(expected.size() - 1, "3,SUBTASK,Подзадача 1,NEW,Первая подзадача," +
                "2022;05;01;15;00;00,60,2");
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
        taskManager = new FileBackedTasksManager("noStatus.txt");
        ManagerSaveException ex = Assertions.assertThrows(ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile (taskManager.getPath().getFileName().toString()));
        Assertions.assertEquals("Нет данных для восстановления статуса объекта", ex.getMessage());

        taskManager = new FileBackedTasksManager("lackOfData.txt");
        ArrayIndexOutOfBoundsException ex1 = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> FileBackedTasksManager.loadFromFile (taskManager.getPath().getFileName().toString()));
        Assertions.assertEquals("Недостаточно данных для восстановления объекта", ex1.getMessage());

        taskManager = new FileBackedTasksManager("noType.txt");
        ManagerSaveException ex2 = Assertions.assertThrows(ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile (taskManager.getPath().getFileName().toString()));
        Assertions.assertEquals("Нет данных для определения типа объекта", ex2.getMessage());

        taskManager = new FileBackedTasksManager("test.txt");
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile (taskManager.getPath().
                getFileName().toString());

        Epic epic = new Epic("Эпик 1", "Первый Эпик");
        epic.setStatus(Status.NEW);
        epic.setId(2);
        ZonedDateTime startTimeFirstSubtask = ZonedDateTime.of(  // Стартовое время первой подзадачи
                LocalDateTime.of(2022, 5, 1, 15, 0, 0, 0),
                ZoneId.of("Europe/Moscow"));
        Subtask subtask = new Subtask("Подзадача 1", Status.NEW, "Первая подзадача", 2,
                startTimeFirstSubtask, Duration.ofMinutes(60));
        subtask.setId(3);
        epic.getSubtasks().put(subtask.getId(), subtask);
        epic.setStartTime();
        epic.setDuration();
        epic.setEndTime();

        Assertions.assertEquals(subtask, manager.getEpics().get(2).getSubtasks().get(3));
        Assertions.assertEquals(epic, manager.getEpics().get(2));

        List<Records> shouldBeHistory = new ArrayList<>();
        shouldBeHistory.add(epic);
        shouldBeHistory.add(subtask);
        Assertions.assertEquals(shouldBeHistory, manager.getHistoryManager().getHistory());
    }

}
