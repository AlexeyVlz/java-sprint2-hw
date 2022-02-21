public class Main {
    public static void main(String[] args) {

        System.out.println("Пришло время практики!");

        Manager manager = new Manager();

        manager.getNewTask(new Task("Задача1", "Первая", "NEW"));
        manager.getNewTask(new Task("Задача2", "Вторая", "NEW"));

        manager.getNewEpic(new Epic("Эпик 1"));
        manager.getNewSubtask(new Subtask("Подзадача 1", "Первая подзадача", "NEW"), 1);
        manager.getNewSubtask(new Subtask("Подзадача 2", "Вторая подзадача", "NEW"), 1);

        manager.getNewEpic(new Epic("Эпик 2"));
        manager.getNewSubtask(new Subtask("Подзадача", "Единственная подзадача", "NEW"), 2);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubtaskList(1));
        System.out.println(manager.getSubtaskList(2));

        manager.updateTask(new Task("Подзадача 1", "Первая подзадача", "IN_PROGRESS"), 1);
        manager.updateSubtask(1, 1, new Subtask("Подзадача 1", "Первая подзадача",
                "IN_PROGRESS"));

        manager.removeTask(1);
        manager.removeEpic(2);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicsList());
    }
}
