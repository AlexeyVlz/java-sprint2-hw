package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


public interface TaskManager {

    public void setId(int id);

    TreeSet<Records> getPrioritizedTasks();

    HistoryManager<Records> getHistoryManager();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    ArrayList<Task> getTasksList(); // получаем список задач

    HashMap<Integer, Task> clearTasksList(); // очистка списка задач

    Task getTaskById(int id); // получение задачи по идентификатору

    HashMap<Integer, Task> getNewTask(Task task); // добавление новой задачи

    HashMap<Integer, Task> updateTask(Task task); // обновляем задачу

    HashMap<Integer, Task> removeTask(int identifier); // удаляем задачу по ID

    ArrayList<Subtask> getSubtaskList(int identifier); // получение списка подзадач по ID эпика

    Epic clearSubtasks(int identifier); // очищаем список подзадач по ID эпика

    Subtask getSubtaskById(int EpicId, int SubtaskId); // получаем подзадачу по идентификатору

    HashMap<Integer, Epic> getNewSubtask(Subtask subtask); // создаем подзадачи

    HashMap<Integer, Epic> updateSubtask(Subtask subtask); //обновление подзадач

    HashMap<Integer, Subtask> removeSubtaskById(int epicId, int subtaskId); //удаление по ID

    ArrayList<Epic> getEpicsList(); // получаем список эпиков

    HashMap<Integer, Epic> clearEpicsList(); // очистка списка эпиков

    Epic getEpicById(int identifier); // получение эпика по идентификатору

    HashMap<Integer, Epic> getNewEpic(Epic epic); // добавление нового эпика

    HashMap<Integer, Epic> updateEpic(Epic epic); // обновляем эпик

    HashMap<Integer, Epic> removeEpic(int identifier); // удаляем эпик по ID

    Status calculateStatus (int epicId); // расчет статуса

}


