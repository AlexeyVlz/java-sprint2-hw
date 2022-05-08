package test;

import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;


public class InMemoryTaskManagerTests extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager taskManager;

    public InMemoryTaskManagerTests(){
        super(new InMemoryTaskManager());
    }


    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    @Override
    public void shouldGiveTasksList() {
        super.shouldGiveTasksList();

    }

    @Test
    @Override
    public void tasksShouldBeEmpty() {
        super.tasksShouldBeEmpty();
    }

    @Test
    @Override
    public void shouldGetTaskById() {
        super.shouldGetTaskById();
    }

    @Test
    @Override
    public void shouldCreateNewTask() {
        super.shouldCreateNewTask();
    }

    @Test
    @Override
    public void shouldUpdateTask() {
        super.shouldUpdateTask();
    }

    @Test
    @Override
    public void shouldRemoveTask() {
        super.shouldRemoveTask();
    }

    @Test
    @Override
    public void shouldGetSubtaskListByEpicId() {
        super.shouldGetSubtaskListByEpicId();
    }

    @Test
    @Override
    public void shouldClearSubtasks() {
        super.shouldClearSubtasks();
    }
    @Test
    @Override
    public void shouldGetSubtaskById() {
        super.shouldGetSubtaskById();
    }

    @Test
    @Override
    public void shouldGetNewSubtask() {
        super.shouldGetNewSubtask();
    }

    @Test
    @Override
    public void shouldUpdateSubtask() {
        super.shouldUpdateSubtask();
    }

    @Test
    @Override
    public void shouldRemoveSubtaskById() {
        super.shouldRemoveSubtaskById();
    }

    @Test
    @Override
    public void shouldGetEpicsList() {
        super.shouldGetEpicsList();
    }

    @Test
    @Override
    public void shouldClearEpicsList() {
        super.shouldClearEpicsList();
    }

    @Test
    @Override
    public void shouldGetEpicById() {
        super.shouldGetEpicById();
    }

    @Test
    @Override
    public void shouldGetNewEpic() {
        super.shouldGetNewEpic();
    }

    @Test
    @Override
    public void shouldUpdateEpic() {
        super.shouldUpdateEpic();
    }

    @Test
    @Override
    public void shouldRemoveEpic() {
        super.shouldRemoveEpic();
    }

    @Test
    public void shouldCalculateStatus() {
        super.shouldCalculateStatus();
    }
}
