package API;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String url;
    private String API_TOKEN;
    private HttpRequest request;
    private HttpResponse.BodyHandler<String> handler;
    private HttpClient client;

    public KVTaskClient(String url) throws InterruptedException, IOException {
        this.url = url;
        handler = HttpResponse.BodyHandlers.ofString();
        client = HttpClient.newHttpClient();
        request =  HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        this.API_TOKEN = response.body();
    }

    public void put(String key, String json) throws InterruptedException, IOException {
        request =  HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request, handler);
    }

    public String load(String key) throws InterruptedException, IOException {
         request =  HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }



}
