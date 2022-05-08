package controllers;


import model.*;
import org.opentest4j.AssertionFailedError;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;


public class InMemoryTaskManager implements TaskManager {
    final protected HashMap<Integer, Task> tasks;
    final protected HashMap<Integer, Epic> epics;
    final protected HistoryManager<Records> historyManager;
    TreeSet<Records> prioritizedTasks;
    int id;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        id = 0;
        this.prioritizedTasks = new TreeSet<>(new Comparator<Records>() {
            @Override
            public int compare(Records o1, Records o2) {
                ZonedDateTime zeroTime = ZonedDateTime.of(
                        LocalDateTime.of(0,0,0,0,0,0,0),
                        ZoneId.of("Europe/Moscow"));
                if(o1.getStartTime().equals(zeroTime)) {
                    return 1;
                }
                ZonedDateTime time = ZonedDateTime.of(
                        LocalDateTime.of(2022,0,0,0,0,0,0),
                        ZoneId.of("Europe/Moscow"));
                Duration first = Duration.between(time, o1.getStartTime());
                Duration second = Duration.between(time, o2.getStartTime());
                return (int) first.toMinutes() - (int) second.toMinutes();
            }
        });
    }

    public TreeSet<Records> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public HistoryManager<Records> getHistoryManager() {
        return historyManager;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public ArrayList<Task> getTasksList () { // получаем список задач
        ArrayList <Task> tasksList = new ArrayList<>();
        for (Integer keys : tasks.keySet()){
            tasksList.add(tasks.get(keys));
        }
        return tasksList;
    }

    @Override
    public HashMap<Integer, Task> clearTasksList () { // очистка списка задач
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }

        if(!tasks.isEmpty()) {
            tasks.clear();
        }
        return tasks;
    }

    @Override
    public Task getTaskById (int id) { // получение задачи по идентификатору
        try {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } catch (NullPointerException e) {
            throw new NullPointerException("Задача не найдена");
        }
    }

    @Override
    public HashMap<Integer, Task> getNewTask (Task task) { // добавление новой задачи
        try {
            task.setId(++id);
            tasks.put(id, task);
            return tasks;
        } catch (NullPointerException e) {
            throw new NullPointerException("Данные не получены");
        }
    }

    @Override
    public HashMap<Integer, Task> updateTask (Task task) { // обновляем задачу
        if(tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            return tasks;
        } else {
            throw new NullPointerException("Такая задача не создавалась");
        }
    }

    @Override
    public HashMap<Integer, Task> removeTask (int identifier) { // удаляем задачу по ID
        historyManager.remove(identifier);
        tasks.remove(identifier);
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList (int identifier) { // получение списка подзадач по ID эпика
        try {
            ArrayList<Subtask> tasksList = new ArrayList<>();
            for (Integer keys : epics.get(identifier).getSubtasks().keySet()) {
                tasksList.add(epics.get(identifier).getSubtasks().get(keys));
            }
            return tasksList;
        } catch (NullPointerException e) {
            throw new NullPointerException("Эпик не найден");
        }
    }

    @Override
    public Epic clearSubtasks (int identifier) { // очищаем список подзадач по ID эпика
        try {
            for (Subtask subtask : epics.get(identifier).getSubtasks().values()) {
                historyManager.remove(subtask.getId());
            }
            epics.get(identifier).getSubtasks().clear();
            Status newStatus = calculateStatus(identifier);
            epics.get(identifier).setStatus(newStatus);
            return epics.get(identifier);
        } catch (NullPointerException e) {
            throw new NullPointerException("Эпик не найден");
        }
    }

    @Override
    public Subtask getSubtaskById (int EpicId, int SubtaskId) { // получаем подзадачу по идентификатору
        try {
            historyManager.add(epics.get(EpicId).getSubtasks().get(SubtaskId));
            return epics.get(EpicId).getSubtasks().get(SubtaskId);
        } catch (NullPointerException e) {
            throw new NullPointerException("Объект не найден");
        }
    }

    @Override
    public HashMap<Integer, Epic> getNewSubtask (Subtask subtask) { // создаем подзадачи
        try {
            subtask.setId(++id);
            epics.get(subtask.getEpicId()).getSubtasks().put(id, subtask);
            Status newStatus = calculateStatus(subtask.getEpicId());
            epics.get(subtask.getEpicId()).setStatus(newStatus);
            return epics;
        } catch (NullPointerException e) {
            throw new NullPointerException("Объект не найден");
        }
    }

    @Override
    public HashMap<Integer, Epic> updateSubtask (Subtask subtask) { //обновление подзадач
        try {
            if (!epics.get(subtask.getEpicId()).getSubtasks().containsKey(subtask.getId())) {
                throw new NullPointerException("Объект не найден");
            }
            epics.get(subtask.getEpicId()).getSubtasks().put(subtask.getId(), subtask);
            Status newStatus = calculateStatus(subtask.getEpicId());
            epics.get(subtask.getEpicId()).setStatus(newStatus);
            return epics;
        } catch (NullPointerException e) {
            throw new NullPointerException("Объект не найден");
        }
    }

    @Override
    public HashMap<Integer, Subtask> removeSubtaskById (int epicId, int subtaskId) { //удаление по ID
        try {
            epics.get(epicId).getSubtasks().remove(subtaskId);
            Status newStatus = calculateStatus(epicId);
            epics.get(epicId).setStatus(newStatus);
            historyManager.remove(subtaskId);
            return epics.get(epicId).getSubtasks();
        } catch (NullPointerException e) {
            throw new NullPointerException("Объект не найден");
        }
    }

    @Override
    public ArrayList<Epic> getEpicsList () { // получаем список эпиков
        ArrayList <Epic> tasksList = new ArrayList<>();
        for (Integer keys : epics.keySet()){
            tasksList.add(epics.get(keys));
        }
        return tasksList;
    }

    @Override
    public HashMap<Integer, Epic> clearEpicsList () { // очистка списка эпиков
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        return epics;
    }

    @Override
    public Epic getEpicById (int identifier) { // получение эпика по идентификатору
        try {
            historyManager.add(epics.get(identifier));
            return epics.get(identifier);
        } catch (NullPointerException e) {
            throw new NullPointerException("Объект не найден");
        }
    }

    @Override
    public HashMap<Integer, Epic> getNewEpic (Epic epic) { // добавление нового эпика
        epic.setId(++id);
        epics.put(id, epic);
        epic.setStatus(calculateStatus(id));
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> updateEpic (Epic epic) { // обновляем эпик
        if(epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            return epics;
        } else {
            throw new NullPointerException("Такой эпик не создавался");
        }
    }

    @Override
    public HashMap<Integer, Epic> removeEpic (int identifier) { // удаляем эпик по ID
        if(epics.containsKey(identifier)) {
            epics.remove(identifier);
            historyManager.remove(identifier);
            return epics;
        } else {
            throw new NullPointerException("Такой эпик не создавался");
        }
    }

    public Status calculateStatus (int epicId) {
        try {
            Status newStatus = Status.NEW;
            ArrayList<Status> statuses = new ArrayList<>();
            for (Integer key : epics.get(epicId).getSubtasks().keySet()) { // достаем каждую подзадачу
                statuses.add(epics.get(epicId).getSubtasks().get(key).getStatus()); // складываем ее в список
            }
            if (statuses.isEmpty()) {
                return newStatus;
            }
            int statusNew = 0;
            int statusDone = 0;
            for (Status status : statuses) {
                if (Status.NEW.equals(status)) {
                    statusNew = statusNew + 1;
                }
                if (Status.DONE.equals(status)) {
                    statusDone = statusDone + 1;
                }
            }
            if (statuses.size() == statusNew) {
                return newStatus;
            } else if (statuses.size() == statusDone) {
                newStatus = Status.DONE;
                return newStatus;
            } else {
                newStatus = Status.IN_PROGRESS;
                return newStatus;
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("Такой эпик не создавался");
        }
    }


}
