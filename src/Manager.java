import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int identifierTask;
    private int identifierEpic;
    private int identifierSubtask;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;


    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        identifierTask = 0;
        identifierEpic = 0;
        identifierSubtask = 0;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public ArrayList<Task> getTasksList () { // получаем список задач
        ArrayList <Task> tasksList = new ArrayList<>();
        for (Integer keys : tasks.keySet()){
            tasksList.add(tasks.get(keys));
        }
        return tasksList;
    }

    public HashMap<Integer, Task> clearTasksList () { // очистка списка задач
        tasks.clear();
        return tasks;
    }

    public Task getTaskById (int identifier) { // получение задачи по идентификатору
        return tasks.get(identifier);
    }

    public HashMap<Integer, Task> getNewTask (Task task) { // добавление новой задачи
        identifierTask = identifierTask + 1;
        tasks.put(identifierTask, task);
        return tasks;
    }

    public HashMap<Integer, Task> updateTask (Task task, int identifier) { // обновляем задачу
        tasks.put(identifier, task);
        return tasks;
    }

    public HashMap<Integer, Task> removeTask (int identifier) { // удаляем задачу по ID
        tasks.remove(identifier);
        return tasks;
    }

    public ArrayList<Subtask> getSubtaskList (int identifier) { // получение списка подзадач по ID эпика
        Epic epic = epics.get(identifier);
        ArrayList <Subtask> tasksList = new ArrayList<>();
        HashMap <Integer, Subtask> subtasks = epic.getSubtasks(); //достаем из эпика таблицу подзадач
        for (Integer keys : subtasks.keySet()){
            tasksList.add(subtasks.get(keys));
        }
        return tasksList;
    }

    public Epic clearSubtasks (int identifier) { // очищаем список подзадач по ID эпика
        Epic epic = epics.get(identifier);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
        subtasks.clear();
        return epic;
    }

    public Subtask getSubtaskById (int EpicId, int SubtaskId) { // получаем подзадачу по идентификатору
        Epic epic = epics.get(EpicId);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
        return subtasks.get(SubtaskId);
    }

    public HashMap<Integer, Epic> getNewSubtask (Subtask subtask, int epicId) { // создаем подзадачи
        identifierSubtask = identifierSubtask + 1;
        epics.get(epicId).getSubtasks().put(identifierSubtask, subtask);
        return epics;
    }

    public HashMap<Integer, Subtask> updateSubtask (int EpicId, int SubtaskId, Subtask subtask) { //обновление подзадач
        Epic epic = epics.get(EpicId);
        HashMap <Integer, Subtask> subtasks = epic.getSubtasks();
        subtasks.put (SubtaskId, subtask);
        return subtasks;
    }

    public HashMap<Integer, Subtask> removeSubtaskById (int EpicId, int SubtaskId) { //удаление по ID
        Epic epic = epics.get(EpicId);
        HashMap <Integer, Subtask> subtasks = epic.getSubtasks();
        subtasks.remove(SubtaskId);
        return subtasks;
    }

    public ArrayList<Epic> getEpicsList () { // получаем список эпиков
        ArrayList <Epic> tasksList = new ArrayList<>();
        for (Integer keys : epics.keySet()){
            tasksList.add(epics.get(keys));
        }
        return tasksList;
    }

    public HashMap<Integer, Epic> clearEpicsList () { // очистка списка эпиков
        epics.clear();
        return epics;
    }

    public Task getEpicById (int identifier) { // получение эпика по идентификатору
        return epics.get(identifier);
    }

    public HashMap<Integer, Epic> getNewEpic (Epic epic) { // добавление нового эпика
        identifierEpic = identifierEpic + 1;
        epics.put(identifierEpic, epic);
        return epics;
    }

    public HashMap<Integer, Epic> updateEpic (Epic epic, int identifier) { // обновляем эпик
        epics.put(identifier, epic);
        return epics;
    }

    public HashMap<Integer, Epic> removeEpic (int identifier) { // удаляем эпик по ID
        epics.remove(identifier);
        return epics;
    }
}
