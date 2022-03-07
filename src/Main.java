import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.println("Пришло время практики!");

        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.getNewTask(new Task("Задача1", "Первая", Status.NEW)); // создаем задачи
        manager.getNewTask(new Task("Задача2", "Вторая", Status.NEW));

        ArrayList<Integer> taskKeys = new ArrayList<>();
        for (Integer key : manager.getTasks().keySet()){ // так как нет фронтэнда вытащил ключи задач для аргументов
            taskKeys.add(key);
        }

       manager.getNewEpic(new Epic("Эпик 1")); // создаем эпики
        manager.getNewEpic(new Epic("Эпик 2"));

        ArrayList<Integer> epicKeys = new ArrayList<>();
        for (Integer key : manager.getEpics().keySet()){ // так как нет фронтэнда вытащил ключи эпиков для аргументов
            epicKeys.add(key);
        }

        manager.getNewSubtask(new Subtask("Подзадача 1", "Первая подзадача", Status.NEW,
                epicKeys.get(0)));                                                               // создаем подзадачи
        manager.getNewSubtask(new Subtask("Подзадача 2", "Вторая подзадача", Status.NEW,
                epicKeys.get(0)));
        manager.getNewSubtask(new Subtask("Подзадача", "Единственная подзадача", Status.NEW,
                epicKeys.get(1)));

        ArrayList<Integer> subtaskKeysFirstEpic = new ArrayList<>(); // вытащил ключи подзадач 1 эпика для аргументов
        for (Integer key : manager.getEpics().get(epicKeys.get(0)).getSubtasks().keySet()){
            subtaskKeysFirstEpic.add(key);
        }

        manager.updateTask(new Task("Задача 1", "Первая", Status.IN_PROGRESS));  // обновляем задачу
        manager.updateSubtask(subtaskKeysFirstEpic.get(0), new Subtask("Подзадача 1",
                "Первая подзадача",Status.IN_PROGRESS, epicKeys.get(0)));          // обновляем подзадачу

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubtaskList(epicKeys.get(0)));
        System.out.println(manager.getSubtaskList(epicKeys.get(1)));

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicsList());

        System.out.println(manager.getTasks().get(taskKeys.get(1)).getStatus());
        System.out.println(manager.getEpics().get(epicKeys.get(0)).getStatus());



        manager.getTaskById(taskKeys.get(0)); // проверяем работоспособность истории
        System.out.println(manager.getHistoryManager().getHistory());

        manager.getEpicById(epicKeys.get(0));
        System.out.println(manager.getHistoryManager().getHistory());

    }
}
