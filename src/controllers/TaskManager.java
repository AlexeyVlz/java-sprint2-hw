package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public interface TaskManager {


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

    HashMap<Integer, Epic> updateSubtask(int SubtaskId, Subtask subtask); //обновление подзадач

    HashMap<Integer, Subtask> removeSubtaskById(int epicId, int subtaskId); //удаление по ID

    ArrayList<Epic> getEpicsList(); // получаем список эпиков

    HashMap<Integer, Epic> clearEpicsList(); // очистка списка эпиков

    Task getEpicById(int identifier); // получение эпика по идентификатору

    HashMap<Integer, Epic> getNewEpic(Epic epic); // добавление нового эпика

    HashMap<Integer, Epic> updateEpic(Epic epic, int identifier); // обновляем эпик

    HashMap<Integer, Epic> removeEpic(int identifier); // удаляем эпик по ID

    //List<Task> history();
}

