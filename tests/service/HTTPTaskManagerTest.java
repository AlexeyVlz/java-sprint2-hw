package service;

import API.HttpTaskServer;
import API.KVServer;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class HTTPTaskManagerTest {

    HttpTaskServer server;

    public HTTPTaskManagerTest() throws IOException, InterruptedException {
        server = new HttpTaskServer(new HTTPTaskManager("http://localhost:8078"));
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        KVServer kvServer = new KVServer();
    }

    @Test
    public void GetTaskById() {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient client = HttpClient.newHttpClient();
        String url;
    }
}
