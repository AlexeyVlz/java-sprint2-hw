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
import java.util.ArrayList;
import java.util.List;

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
        fillServer();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
        server.stop();

    }

    @Test
    public void getTasksListTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/getTasksList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Records> newTasks = server.getGson().fromJson(body, type);
        Assertions.assertEquals(newTasks, server.getManager().getTasksList());

    }

    @Test
    public void clearTasksList() throws IOException, InterruptedException {
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
        Task task = server.getManager().getTasks().get(1);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(task)))
                .uri(URI.create(url + "/Tasks/PostNewTask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
        Assertions.assertEquals(task, server.getManager().getTasks().get(1));
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        Task task = objects.firstTask();
        task.setStatus(Status.DONE);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(server.getGson().toJson(task)))
                .uri(URI.create(url + "/Tasks/UpdateTask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
        Assertions.assertEquals(server.getManager().getTasks().get(1), task);
    }

    @Test
    public void removeTaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/Tasks/RemoveTask?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
        Assertions.assertFalse(server.getManager().getTasks().containsKey(1));
    }

    /*@Test
    public void getSubtaskListTest*/


    private void fillServer() {
        server.getManager().getNewTask(objects.firstTask());
        server.getManager().getNewTask(objects.secondTask());
        server.getManager().getNewEpic(new Epic("Эпик 1", "Первый эпик"));
        Subtask subtask1 = objects.firstSubtask();
        Subtask subtask2 = objects.secondSubtask();
        Subtask subtask3 = objects.thirdSubtask();
        server.getManager().getEpics().get(3).getSubtasks().put(subtask1.getId(), subtask1);
        server.getManager().getEpics().get(3).getSubtasks().put(subtask2.getId(), subtask2);
        server.getManager().getEpics().get(3).getSubtasks().put(subtask3.getId(), subtask3);
        server.getManager().getEpics().get(3).setStatus(server.getManager().calculateStatus(3));
        server.getManager().getEpics().get(3).setStartTime();
        server.getManager().getEpics().get(3).setDuration();
        server.getManager().getEpics().get(3).setEndTime();
    }
}
