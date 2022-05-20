package service;

import API.HttpTaskServer;
import API.KVServer;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Records;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
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
    ObjectsForTests objects;

    public HTTPTaskManagerTest() throws IOException, InterruptedException {
        url = "http://localhost:8080";
        objects = new ObjectsForTests();
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
    public void GetTaskByIdTest() throws IOException, InterruptedException {
        server.getManager().getNewTask(objects.firstTask());
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
    public void getTasksListTest() throws IOException, InterruptedException {
        server.getManager().getNewTask(objects.firstTask());
        server.getManager().getNewTask(objects.secondTask());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/Tasks/getTasksList"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        ArrayList<Task> tasks = server.getGson().fromJson(body, ArrayList.class);

    }


}
