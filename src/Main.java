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
        manager.getNewSubtask(new Subtask("Подзадача", Status.NEW, "Единственная подзадача",
                epicKeys.get(1)));

        ArrayList<Integer> subtaskKeysFirstEpic = new ArrayList<>(manager.getEpics().get(epicKeys.get(0)).getSubtasks().
                keySet()); // вытащил ключи подзадач 1 эпика для аргументов


        manager.updateTask(new Task("Задача 1", Status.IN_PROGRESS, "Первая"));  // обновляем задачу
        manager.updateSubtask(subtaskKeysFirstEpic.get(0), new Subtask("Подзадача 1", Status.IN_PROGRESS,
                "Первая подзадача", epicKeys.get(0)));          // обновляем подзадачу

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
