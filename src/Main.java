import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.println("Пришло время практики!");

        Manager manager = new Manager();

        manager.getNewTask(new Task("Задача1", "Первая", "NEW")); // создаем задачи
        manager.getNewTask(new Task("Задача2", "Вторая", "NEW"));

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

        manager.getNewSubtask(new Subtask("Подзадача 1", "Первая подзадача", "NEW"),
                epicKeys.get(0));                                                            // создаем подзадачи
        manager.getNewSubtask(new Subtask("Подзадача 2", "Вторая подзадача", "NEW"),
                epicKeys.get(0));
        manager.getNewSubtask(new Subtask("Подзадача", "Единственная подзадача", "NEW"),
                epicKeys.get(1));

        ArrayList<Integer> subtaskKeysFirstEpic = new ArrayList<>(); // вытащил ключи подзадач 1 эпика для аргументов
        for (Integer key : manager.getEpics().get(epicKeys.get(0)).getSubtasks().keySet()){
            subtaskKeysFirstEpic.add(key);
        }

        manager.updateTask(new Task("Подзадача 1", "Первая подзадача", "IN_PROGRESS"),
                taskKeys.get(0));                                                       // обновляем задачу
        manager.updateSubtask(epicKeys.get(0), subtaskKeysFirstEpic.get(0), new Subtask("Подзадача 1",
                "Первая подзадача","IN_PROGRESS"));                     // обновляем подзадачу

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubtaskList(epicKeys.get(0)));
        System.out.println(manager.getSubtaskList(epicKeys.get(1)));


        manager.removeTask(taskKeys.get(0));
        manager.removeEpic(epicKeys.get(1));

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicsList());
    }
}
