import controllers.Managers;
import controllers.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.println("Пришло время практики!");

        TaskManager manager = Managers.getDefault();


        manager.getNewTask(new Task("Задача1", Status.NEW, "Первая")); // создаем задачи
        manager.getNewTask(new Task("Задача2", Status.NEW, "Вторая"));

        ArrayList<Integer> taskKeys = new ArrayList<>(manager.getTasks().keySet());// вытаскиваем ключи задач

        manager.getNewEpic(new Epic("Эпик 1")); // создаем эпики
        manager.getNewEpic(new Epic("Эпик 2"));

        ArrayList<Integer> epicKeys = new ArrayList<>(manager.getEpics().keySet()); // вытаскиваем ключи эпиков

        manager.getNewSubtask(new Subtask("Подзадача 1", Status.NEW, "Первая подзадача",
                epicKeys.get(0)));                                                               // создаем подзадачи
        manager.getNewSubtask(new Subtask("Подзадача 2", Status.NEW, "Вторая подзадача",
                epicKeys.get(0)));
        manager.getNewSubtask(new Subtask("Подзадача 3", Status.NEW, "Третья подзадача",
                epicKeys.get(0)));

        ArrayList<Integer> subtaskKeysFirstEpic = new ArrayList<>(manager.getEpics().get(epicKeys.get(0)).getSubtasks().
                keySet()); // вытащил ключи подзадач 1 эпика для аргументов



        manager.getSubtaskById(epicKeys.get(0), subtaskKeysFirstEpic.get(0));
        manager.getTaskById(taskKeys.get(0)); // проверяем работоспособность истории
        manager.getEpicById(epicKeys.get(1));
        manager.getTaskById(taskKeys.get(1));
        manager.getTaskById(taskKeys.get(0));
        manager.getEpicById(epicKeys.get(1));
        manager.getTaskById(taskKeys.get(0)); // проверяем работоспособность истории

        System.out.println(manager.getHistoryManager().getHistory());

        manager.getEpicById(epicKeys.get(0));
        System.out.println(manager.getHistoryManager().getHistory());

        manager.removeTask(taskKeys.get(0));
        System.out.println(manager.getHistoryManager().getHistory());

        manager.clearTasksList();
        System.out.println(manager.getHistoryManager().getHistory());






    }
}
