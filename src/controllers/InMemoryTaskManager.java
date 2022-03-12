package controllers;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager {
    final private HashMap<Integer, Task> tasks;
    final private HashMap<Integer, Epic> epics;
    final private HistoryManager <Records> historyManager;
    int id;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        id = 0;
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
        for (int i = (historyManager.getHistory().size() - 1); i >= 0; i--) {
            for (Task task : tasks.values()) {
                if (historyManager.getHistory().get(i).getId() == task.getId()) {
                    historyManager.getHistory().remove(i);
                }
            }
        }
        tasks.clear();
        return tasks;
    }

    @Override
    public Task getTaskById (int id) { // получение задачи по идентификатору
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public HashMap<Integer, Task> getNewTask (Task task) { // добавление новой задачи
        task.setId(++id);
        tasks.put(id, task);
        return tasks;
    }

    @Override
    public HashMap<Integer, Task> updateTask (Task task) { // обновляем задачу
        tasks.put(task.getId(), task);
        return tasks;
    }

    @Override
    public HashMap<Integer, Task> removeTask (int identifier) { // удаляем задачу по ID
        for (int i = (historyManager.getHistory().size() - 1); i >= 0; i--){
            if(historyManager.getHistory().get(i).getId() == identifier){
                historyManager.getHistory().remove(i);
            }
        }
        tasks.remove(identifier);
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList (int identifier) { // получение списка подзадач по ID эпика
        Epic epic = epics.get(identifier);
        ArrayList <Subtask> tasksList = new ArrayList<>();
        HashMap <Integer, Subtask> subtasks = epic.getSubtasks(); //достаем из эпика таблицу подзадач
        for (Integer keys : subtasks.keySet()){
            tasksList.add(subtasks.get(keys));
        }
        return tasksList;
    }

    @Override
    public Epic clearSubtasks (int identifier) { // очищаем список подзадач по ID эпика
        for(int i = (historyManager.getHistory().size() - 1); i >= 0; i--) {
            for (Subtask subtask : epics.get(identifier).getSubtasks().values()){
                if (historyManager.getHistory().get(i).getId() == subtask.getId()) {
                    historyManager.getHistory().remove(i);
                }
            }
        }
        epics.get(identifier).getSubtasks().clear();
        Status newStatus = calculateStatus(identifier);
        epics.get(identifier).setStatus(newStatus);
        return epics.get(identifier);
    }

    @Override
    public Subtask getSubtaskById (int EpicId, int SubtaskId) { // получаем подзадачу по идентификатору
        historyManager.add(epics.get(EpicId).getSubtasks().get(SubtaskId));
        return epics.get(EpicId).getSubtasks().get(SubtaskId);
    }

    @Override
    public HashMap<Integer, Epic> getNewSubtask (Subtask subtask) { // создаем подзадачи
        subtask.setId(++id);
        epics.get(subtask.getEpicId()).getSubtasks().put(id, subtask);
        Status newStatus = calculateStatus(subtask.getEpicId());
        epics.get(subtask.getEpicId()).setStatus(newStatus);
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> updateSubtask (int SubtaskId, Subtask subtask) { //обновление подзадач
        epics.get(subtask.getEpicId()).getSubtasks().put (SubtaskId, subtask);
        Status newStatus = calculateStatus(subtask.getEpicId());
        epics.get(subtask.getEpicId()).setStatus(newStatus);
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> removeSubtaskById (int epicId, int subtaskId) { //удаление по ID
        epics.get(epicId).getSubtasks().remove(subtaskId);
        Status newStatus = calculateStatus(epicId);
        epics.get(epicId).setStatus(newStatus);
        for (int i = (historyManager.getHistory().size() - 1); i >= 0; i--){
            if(historyManager.getHistory().get(i).getId() == subtaskId){
                historyManager.getHistory().remove(i);
            }
        }
        return epics.get(epicId).getSubtasks();
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
        for (int i = 0; i < historyManager.getHistory().size(); i++) {
            for (Epic epic : epics.values()) {
                if (historyManager.getHistory().get(i).getId() == epic.getId()) {
                    historyManager.getHistory().remove(i);
                }
            }
        }
        epics.clear();
        return epics;
    }

    @Override
    public Epic getEpicById (int identifier) { // получение эпика по идентификатору
        historyManager.add(epics.get(identifier));
        return epics.get(identifier);
    }

    @Override
    public HashMap<Integer, Epic> getNewEpic (Epic epic) { // добавление нового эпика
        epic.setId(++id);
        epics.put(id, epic);
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> updateEpic (Epic epic, int identifier) { // обновляем эпик
        epics.put(identifier, epic);
        return epics;
    }

    @Override
    public HashMap<Integer, Epic> removeEpic (int identifier) { // удаляем эпик по ID
        epics.remove(identifier);
        for (int i = (historyManager.getHistory().size() - 1); i >= 0; i--){
            if(historyManager.getHistory().get(i).getId() == identifier){
                historyManager.getHistory().remove(i);
            }
        }
        return epics;
    }

    public Status calculateStatus (int epicId) {
        Status newStatus = Status.NEW;
        ArrayList<Status> statuses = new ArrayList<>();
        for (Integer key : epics.get(epicId).getSubtasks().keySet()){ // достаем каждую подзадачу
            statuses.add(epics.get(epicId).getSubtasks().get(key).getStatus()); // складываем ее в список
        }
        if (statuses.isEmpty()){
            return newStatus;
        }
        int statusNew = 0;
        int statusDone = 0;
        for (Status status: statuses){
            if(Status.NEW.equals(status)) {
                statusNew = statusNew + 1;
            }
            if (Status.DONE.equals(status)) {
                statusDone = statusDone + 1;
            }
        }
        if (statuses.size() == statusNew){
            return newStatus;
        } else if (statuses.size() == statusDone){
            newStatus = Status.DONE;
            return newStatus;
        } else {
            newStatus = Status.IN_PROGRESS;
            return newStatus;
        }
    }


}
