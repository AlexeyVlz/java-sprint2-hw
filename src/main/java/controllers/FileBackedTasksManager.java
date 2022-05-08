package controllers;
import model.*;

import java.io.*;
import model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final Path path;

    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public HashMap<Integer, Task> clearTasksList () { // очистка списка задач
        super.clearTasksList();
        save ();
        return tasks;
    }

    @Override
    public Task getTaskById (int id) { // получение задачи по идентификатору
        super.getTaskById(id);
        save ();
        return tasks.get(id);
    }

    @Override
    public HashMap<Integer, Task> getNewTask (Task task) { // добавление новой задачи
        super.getNewTask(task);
        save ();
        return tasks;
    }

    @Override
    public HashMap<Integer, Task> updateTask (Task task) { // обновляем задачу
        super.updateTask(task);
        save ();
        return tasks;
    }

    @Override
    public HashMap<Integer, Task> removeTask (int identifier) { // удаляем задачу по ID
        super.removeTask(identifier);
        save ();
        return tasks;
    }

    @Override
    public Epic clearSubtasks (int identifier) { // очищаем список подзадач по ID эпика
        super.clearSubtasks(identifier);
        save ();
        return epics.get(identifier);
    }

    @Override
    public Subtask getSubtaskById (int EpicId, int SubtaskId) { // получаем подзадачу по идентификатору
        super.getSubtaskById(EpicId, SubtaskId);
        save ();
        return epics.get(EpicId).getSubtasks().get(SubtaskId);
    }

    @Override
    public HashMap<Integer, Epic> getNewSubtask (Subtask subtask) { // создаем подзадачи
        super.getNewSubtask(subtask);
        save ();
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> updateSubtask (Subtask subtask) { //обновление подзадач
        super.updateSubtask(subtask);
        save ();
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> removeSubtaskById (int epicId, int subtaskId) { //удаление по ID
        super.removeSubtaskById(epicId, subtaskId);
        save ();
        return epics.get(epicId).getSubtasks();
    }

    @Override
    public HashMap<Integer, Epic> clearEpicsList () { // очистка списка эпиков
        super.clearEpicsList();
        save();
        return epics;
    }

    @Override
    public Epic getEpicById (int identifier) { // получение эпика по идентификатору
        super.getEpicById(identifier);
        save();
        return epics.get(identifier);
    }

    @Override
    public HashMap<Integer, Epic> getNewEpic (Epic epic) { // добавление нового эпика
        super.getNewEpic(epic);
        save();
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> updateEpic (Epic epic) { // обновляем эпик
        super.updateEpic(epic);
        save();
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> removeEpic (int identifier) { // удаляем эпик по ID
        super.removeEpic(identifier);
        save();
        return epics;
    }


    public void save () { // сохранение задач в файл
        final String tableNames = "id,type,name,status,description,epic";
        try (Writer fileWriter = new FileWriter(path.getFileName().toString())) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(tableNames + "\n");
            for(Integer id : tasks.keySet()) {
                bufferedWriter.write(tasks.get(id).toString() + "\n");
            }
            for(Integer id : epics.keySet()) {
                bufferedWriter.write(epics.get(id).toString() + "\n");
                if(!(epics.get(id).getSubtasks().isEmpty())) {
                    for(Integer idSubtask : epics.get(id).getSubtasks().keySet()) {
                        bufferedWriter.write(epics.get(id).getSubtasks().get(idSubtask).toString() + "\n");
                    }
                }
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(toString(historyManager));
            bufferedWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    public FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager manager = new FileBackedTasksManager(path);
        try (FileReader reader = new FileReader(path.getFileName().toString())) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                if(!line.isBlank()) {
                    Records record = manager.fromString(line);
                    if(record != null) {
                        if (record instanceof Task) {
                            Task task = (Task) record;
                            manager.getTasks().put(task.getId(), task);
                        } else if (record instanceof Epic) {
                            Epic epic = (Epic) record;
                            manager.getEpics().put(epic.getId(), epic);
                        } else if (record instanceof Subtask) {
                            Subtask subtask = (Subtask) record;
                            manager.getEpics().get(subtask.getEpicId()).getSubtasks().put(subtask.getId(), subtask);
                        }
                    }
                } else {
                    line = br.readLine();
                    List <Integer> history = fromStringToList(line);
                    for(Integer value : history) {
                        if(manager.tasks.containsKey(value)) {
                            manager.historyManager.add(manager.tasks.get(value));
                        } else if (manager.epics.containsKey(value)) {
                            manager.historyManager.add(manager.epics.get(value));
                        } else {
                            for(Epic epic : manager.epics.values()) {
                                if(epic.getSubtasks().containsKey(value)) {
                                    manager.historyManager.add(epic.getSubtasks().get(value));
                                }
                            }
                        }
                    }
                    break;
                }
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        return manager;
    }

    private Records fromString(String value) { // создание задачи из строки
        Status status;
        try {
            String[] split = value.split(",");
            if(split[0].equals("id")) {
                return null;
            }
            switch (split[3]) {
                case "NEW":
                    status = Status.NEW;
                    break;
                case "IN_PROGRESS":
                    status = Status.IN_PROGRESS;
                    break;
                case "DONE":
                    status = Status.DONE;
                    break;
                default:
                    throw new ManagerSaveException("Нет данных для восстановления статуса объекта");
            }
            if (split[1].equals(Types.TASK.toString())) {
                Task task = new Task(split[2], status, split[4]);
                task.setId(Integer.parseInt(split[0]));
                return task;
            } else if (split[1].equals(Types.EPIC.toString())) {
                Epic epic = new Epic(split[2], split[4]);
                epic.setStatus(status);
                epic.setId(Integer.parseInt(split[0]));
                return epic;
            } else if (split[1].equals(Types.SUBTASK.toString())) {
                Subtask subtask = new Subtask(split[2], status, split[4], Integer.parseInt(split[5]));
                subtask.setId(Integer.parseInt(split[0]));
                return subtask;
            } else {
                throw new ManagerSaveException("Нет данных для определения типа объекта");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Недостаточно данных для восстановления объекта");
        }
    }

    private String toString(HistoryManager<Records> manager) { //перевод истории в стринг
        List<String> history = new ArrayList<>();
        for(Records record : manager.getHistory()) {
            history.add(Integer.toString(record.getId()));
        }
        return String.join(",", history);
    }

    private List<Integer> fromStringToList(String value) {
        List<Integer> history = new ArrayList<>();
        String[] values = value.split(",");
        for (String id : values) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }



    /*public static void main(String[] args) {
            Path data;
        try {
            data = Files.createFile(Paths.get("data.txt"));
        } catch (IOException e) {
            data = Paths.get("data.txt");
            System.out.println("Файл уже существует");
        }

        FileBackedTasksManager manager =  FileBackedTasksManager.loadFromFile(data);

        manager.getNewTask(new Task("Задача1", Status.NEW, "Первая")); // создаем задачи
        manager.getNewTask(new Task("Задача2", Status.NEW, "Вторая"));
        ArrayList<Integer> taskKeys = new ArrayList<>(manager.getTasks().keySet());// вытаскиваем ключи задач

        manager.getNewEpic(new Epic("Эпик 1", "Первый")); // создаем эпики
        manager.getNewEpic(new Epic("Эпик 2", "Второй"));
        ArrayList<Integer> epicKeys = new ArrayList<>(manager.getEpics().keySet()); // вытаскиваем ключи эпиков

        manager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача",
                epicKeys.get(0)));                                                               // создаем подзадачи
        manager.getNewSubtask(new Subtask("Подзадача 2", Status.DONE, "Вторая подзадача",
                epicKeys.get(0)));
        manager.getNewSubtask(new Subtask("Подзадача 3", Status.NEW, "Третья подзадача",
                epicKeys.get(0)));
        ArrayList<Integer> subtaskKeysFirstEpic = new ArrayList<>(manager.getEpics().get(epicKeys.get(0)).getSubtasks().
                keySet()); // вытащил ключи подзадач 1 эпика для аргументов

        manager.getSubtaskById(epicKeys.get(0), subtaskKeysFirstEpic.get(0)); // проверяем работу истории.
        manager.getTaskById(taskKeys.get(0));
        manager.getTaskById(taskKeys.get(1));
        manager.getEpicById(epicKeys.get(1));
        manager.getSubtaskById(epicKeys.get(0), subtaskKeysFirstEpic.get(1));

        manager.removeTask(1);
        manager.getSubtaskById(epicKeys.get(0), subtaskKeysFirstEpic.get(2));

        try {
            FileReader reader = new FileReader(manager.path.getFileName().toString());
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                System.out.println(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            e.getStackTrace();
        }

    }*/
}
