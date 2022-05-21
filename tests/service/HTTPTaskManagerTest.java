package service;

import API.HttpTaskServer;
import API.KVServer;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import model.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class HTTPTaskManagerTest {

    HTTPTaskManager manager;
    HttpTaskServer server;
    KVServer kvServer;
    String url;
    ObjectsForTestsServer objects;

    public HTTPTaskManagerTest() throws IOException, InterruptedException {
        url = "http://localhost:8080";
        objects = new ObjectsForTestsServer();
    }

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault(kvServer.getServerURL());
        server = new HttpTaskServer(manager);
        server.createServer();

    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
        server.stop();

    }

    @Test
    public void getTasksListTest() throws IOException, InterruptedException {
        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/getTasksList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client.send(request1, handler);
        String body1 = response1.body();
        String stringBody = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody, "Список задач пуст");

        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/getTasksList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Records> newTasks = server.getGson().fromJson(body, type);
        Assertions.assertEquals(newTasks, server.getManager().getTasksList());
    }

    @Test
    public void clearTasksList() throws IOException, InterruptedException {
        fillServer();
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/clearTasksList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(requestDelete, handler);
        Assertions.assertTrue(server.getManager().getTasks().isEmpty());
    }

    @Test
    public void GetTaskByIdTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/GetTask?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Task task = server.getGson().fromJson(body, Task.class);
        Assertions.assertEquals(server.getManager().getTasks().get(1), task);
    }

    @Test
    public void getNewTaskTest() throws IOException, InterruptedException {
        fillServer();
        Task task = server.getManager().getTasks().get(1);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(task)))
                .uri(URI.create(url + "/Tasks/PostNewTask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Время выполнения задачи пересекается со временем Задача 1");

        task = objects.thirdTask();
        task.setId(6);
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(task)))
                .uri(URI.create(url + "/Tasks/PostNewTask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request1, handler);
        Assertions.assertEquals(task, server.getManager().getTasks().get(6));
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        fillServer();
        Task task = objects.firstTask();
        task.setStatus(Status.DONE);
        task.setId(2);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(task)))
                .uri(URI.create(url + "/Tasks/UpdateTask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Время выполнения задачи пересекается со временем Задача 1");

        task = objects.firstTask();
        task.setStatus(Status.DONE);
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(task)))
                .uri(URI.create(url + "/Tasks/UpdateTask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request1, handler);
        Assertions.assertEquals(server.getManager().getTasks().get(1), task);
    }

    @Test
    public void removeTaskTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveTask?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
        Assertions.assertFalse(server.getManager().getTasks().containsKey(1));

        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveTask?id=777"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request1, handler);
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Такой задачи не найдено");
    }

    @Test
    public void getSubtaskListTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/GetSubtaskList?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Type type = new TypeToken<ArrayList<Subtask>>(){}.getType();
        ArrayList<Records> newSubtasks = server.getGson().fromJson(body, type);
        Assertions.assertEquals(newSubtasks, server.getManager().getSubtaskList(3));

        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/GetSubtaskList?id=777"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        String body1 = response1.body();
        String stringBody = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody, "Эпик не найден");
    }

    @Test
    public void clearSubtasksTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/ClearSubtasks?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(requestDelete, handler);
        Assertions.assertTrue(server.getManager().getEpics().get(3).getSubtasks().isEmpty());

        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/ClearSubtasks?id=777"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        String body1 = response1.body();
        String stringBody = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody, "Эпик не найден");
    }

    @Test
    public void postNewSubtaskTest() throws IOException, InterruptedException {
        fillServer();
        Subtask subtask = server.getManager().getEpics().get(3).getSubtasks().get(4);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(subtask)))
                .uri(URI.create(url + "/Tasks/PostNewSubtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Время выполнения задачи пересекается со временем Подзадача 1");

        subtask = objects.thirdSubtask();
        subtask.setId(6);
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(subtask)))
                .uri(URI.create(url + "/Tasks/PostNewSubtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request1, handler);
        Assertions.assertEquals(subtask, server.getManager().getEpics().get(3).getSubtasks().get(6));

        ZonedDateTime startTimeThirdTask = ZonedDateTime.of(  // Стартовое время третьей подзадачи
                LocalDateTime.of(2022,4,1,15,0,0,0),
                ZoneId.of("Europe/Moscow"));
        Subtask subtask1 = new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 777,
                startTimeThirdTask, Duration.ofMinutes(60));
        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(subtask1)))
                .uri(URI.create(url + "/Tasks/PostNewSubtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response1 = client.send(request2, handler);
        String body1 = response1.body();
        String stringBody1 = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody1, "Объект не найден");
    }

    @Test
    public void updateSubtaskTest() throws IOException, InterruptedException {
        fillServer();
        Subtask subtask = server.getManager().getEpics().get(3).getSubtasks().get(4);
        subtask.setId(5);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(subtask)))
                .uri(URI.create(url + "/Tasks/UpdateSubtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Время выполнения задачи пересекается со временем Подзадача 1");

        subtask.setId(4);
        subtask.setStatus(Status.DONE);
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(subtask)))
                .uri(URI.create(url + "/Tasks/UpdateSubtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request1, handler);
        Assertions.assertEquals(subtask, server.getManager().getEpics().get(3).getSubtasks().get(4));

        ZonedDateTime startTimeThirdTask = ZonedDateTime.of(  // Стартовое время третьей подзадачи
                LocalDateTime.of(2022,4,1,15,0,0,0),
                ZoneId.of("Europe/Moscow"));
        Subtask subtask1 = new Subtask("Подзадача 3", Status.NEW, "Третья подзадача", 777,
                startTimeThirdTask, Duration.ofMinutes(60));
        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(subtask1)))
                .uri(URI.create(url + "/Tasks/UpdateSubtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response1 = client.send(request2, handler);
        String body1 = response1.body();
        String stringBody1 = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody1, "Эпик не найден");
    }

    @Test
    public void removeSubtaskByIdTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveSubtaskById?epicId=3&subtaskId=4"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertFalse(server.getManager().getEpics().get(3).getSubtasks().containsKey(4));
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Задача удалена");

        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveSubtaskById?epicId=777&subtaskId=4"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        Assertions.assertFalse(server.getManager().getEpics().get(3).getSubtasks().containsKey(4));
        String body1 = response1.body();
        String stringBody1 = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody1, "Объект не найден");

        HttpRequest request2 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveSubtaskById?epicId=3&subtaskId=777"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        Assertions.assertFalse(server.getManager().getEpics().get(3).getSubtasks().containsKey(4));
        String body2 = response2.body();
        String stringBody2 = server.getGson().fromJson(body2, String.class);
        Assertions.assertEquals(stringBody2, "Объект не найден");
    }

    @Test
    public void getEpicsListTest() throws IOException, InterruptedException {
        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/GetEpicsList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client.send(request1, handler);
        String body1 = response1.body();
        String stringBody = server.getGson().fromJson(body1, String.class);
        Assertions.assertEquals(stringBody, "Список задач пуст");

        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/GetEpicsList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Type type = new TypeToken<ArrayList<Epic>>(){}.getType();
        ArrayList<Records> newTasks = server.getGson().fromJson(body, type);
        Assertions.assertEquals(newTasks, server.getManager().getEpicsList());
    }

    @Test
    public void clearEpicsListTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/ClearEpicsList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(requestDelete, handler);
        Assertions.assertTrue(server.getManager().getEpics().isEmpty());
    }

    @Test
    public void getEpicTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/GetEpic?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Epic epic = server.getGson().fromJson(body, Epic.class);
        Assertions.assertEquals(server.getManager().getEpics().get(3), epic);
    }

    @Test
    public void postNewEpicTest() throws IOException, InterruptedException {
        fillServer();
        Epic epic = new Epic("Новый эпик", "Описание");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGsonForNewEpc().toJson(epic)))
                .uri(URI.create(url + "/Tasks/PostNewEpic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        String stringBody = server.getGsonForNewEpc().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Задача успешно добавлена");

    }

    @Test
    public void UpdateEpicTest() throws IOException, InterruptedException {
        fillServer();
        Epic epic = server.getManager().getEpics().get(3);
        epic.getSubtasks().get(4).setStatus(Status.DONE);
        epic.setStatus(server.getManager().calculateStatus(3));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(epic)))
                .uri(URI.create(url + "/Tasks/updateEpic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(epic, server.getManager().getEpics().get(3));

        epic.setId(777);
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(epic)))
                .uri(URI.create(url + "/Tasks/updateEpic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        String body = response1.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Такой эпик не создавался");
    }

    @Test
    public void removeEpicTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveEpic?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
        Assertions.assertFalse(server.getManager().getEpics().containsKey(3));

        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveEpic?id=777"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request1, handler);
        String body = response.body();
        String stringBody = server.getGson().fromJson(body, String.class);
        Assertions.assertEquals(stringBody, "Такой эпик не создавался");
    }

    /*@Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        fillServer();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/getPrioritizedTasks"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();

        Type type = new TypeToken<TreeSet<Records>>(){}.getType();
        TreeSet<Records> prioritizedTasks = server.getGson().fromJson(body, type);
        Assertions.assertEquals(prioritizedTasks, server.getManager().getPrioritizedTasks());
    }

    @Test
    public void getHistoryManagerTest() throws IOException, InterruptedException {
        fillServer();
        server.getManager().getTaskById(1);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/getHistoryManager"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Type type = new TypeToken<ArrayList<Records>>(){}.getType();
        ArrayList<Records> history = server.getGson().fromJson(body, type);
        Assertions.assertEquals(history, server.getManager().getHistoryManager().getHistory());
    }*/


    private void fillServer() {
        server.getManager().getNewTask(objects.firstTask());
        server.getManager().getNewTask(objects.secondTask());
        server.getManager().getNewEpic(new Epic("Эпик 1", "Первый эпик"));
        server.getManager().getNewSubtask(objects.firstSubtask());
        server.getManager().getNewSubtask(objects.secondSubtask());
        //server.getManager().getNewSubtask(objects.thirdSubtask());
        server.getManager().getEpics().get(3).setStatus(server.getManager().calculateStatus(3));
        server.getManager().getEpics().get(3).setStartTime();
        server.getManager().getEpics().get(3).setDuration();
        server.getManager().getEpics().get(3).setEndTime();
        for(Task task : server.getManager().getTasks().values())
        server.getManager().getPrioritizedTasks().add(task);
        for(Subtask subtask : server.getManager().getEpics().get(3).getSubtasks().values()){
            server.getManager().getPrioritizedTasks().add(subtask);
        }
        server.getManager().setId(5);
    }
}
