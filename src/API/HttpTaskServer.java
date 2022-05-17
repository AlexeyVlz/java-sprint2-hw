package API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Status;
import model.Task;
import service.FileBackedTasksManager;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {

    HttpServer httpServer;
    TaskManager manager;
    Gson gson;

    public HttpTaskServer() {
        this.manager = FileBackedTasksManager.loadFromFile(Paths.get("Api.txt"));
        /*this.gson = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).
                registerTypeAdapter(Duration.class, new DurationAdapter()).create();*/
        this.gson = new GsonBuilder().serializeNulls().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter()).
                registerTypeAdapter(Duration.class, new DurationAdapter()).create();

    }

    public void createServer() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        //httpServer.createContext("/Tasks/Task/postNewTask", new PostNewTaskHandler());
        httpServer.createContext("/Tasks/GetTask", new GetTaskByIdHandler());
        httpServer.createContext("/Tasks/GetEpic", new GetEpicByIdHandler());
        httpServer.createContext("/Tasks/PostNewTask", new PostNewTaskHandler());
        httpServer.start();
        System.out.println("Сервер запущен");

    }

    class PostNewTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            if(!manager.getTasks().containsKey(task.getId())) {
                manager.getNewTask(task);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача уже создана").getBytes());
                }
            }
        }
    }

    class GetTaskByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String id = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
            if(manager.getTasks().containsKey(Integer.parseInt(id))) {
                Task task = manager.getTaskById(Integer.parseInt(id));
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(task).getBytes());
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                }
            }
        }
    }

    class GetEpicByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int id = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
            if (manager.getEpics().containsKey(id)) {
                Epic epic = manager.getEpicById(id);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(epic).getBytes());
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                }
            }
        }
    }


    /*class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd--MM--yyyy, HH:mm:ss,SSS");
        DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy, MM, dd, HH, mm, ss, SSS");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(formatterWriter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }
    }*/

    class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
        DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd--MM--yyyy, HH:mm:ss,SSS");
        DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy, MM, dd, HH, mm, ss, SSS");

        @Override
        public void write(final JsonWriter jsonWriter, final ZonedDateTime DateTime) throws IOException {
            jsonWriter.value(DateTime.format(formatterWriter));
        }

        @Override
        public ZonedDateTime read(final JsonReader jsonReader) throws IOException {
            return ZonedDateTime.of(LocalDateTime.parse(jsonReader.nextString(), formatterReader),
                    ZoneId.systemDefault());

        }
    }

    class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(jsonReader.nextInt());
        }
    }

}
