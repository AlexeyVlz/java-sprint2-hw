package API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exceptions.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
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
        this.gson = new GsonBuilder().serializeNulls().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter()).
                registerTypeAdapter(Duration.class, new DurationAdapter()).create();

    }

    public void createServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/Tasks/getTasksList", new GetTasksListHandler());
        httpServer.createContext("/Tasks/clearTasksList", new ClearTasksListHandler());
        httpServer.createContext("/Tasks/GetTask", new GetTaskByIdHandler());
        httpServer.createContext("/Tasks/PostNewTask", new PostNewTaskHandler());
        httpServer.createContext("/Tasks/UpdateTask", new UpdateTaskHandler());
        httpServer.createContext("/Tasks/RemoveTask", new RemoveTaskHandler());

        httpServer.createContext("/Tasks/GetSubtaskList", new GetSubtaskListHandler());
        httpServer.createContext("/Tasks/ClearSubtasks", new ClearSubtasksHandler());
        httpServer.createContext("/Tasks/GetSubtaskById", new GetSubtaskByIdHandler());
        httpServer.createContext("/Tasks/PostNewSubtask", new PostNewSubtaskHandler());
        httpServer.createContext("/Tasks/UpdateSubtask", new UpdateSubtaskHandler());
        httpServer.createContext("/Tasks/RemoveSubtaskById", new RemoveSubtaskByIdHandler());


        httpServer.createContext("/Tasks/GetEpicsList", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if(!manager.getEpics().isEmpty()){
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson(manager.getEpicsList()).getBytes());
                    }
                } else {
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson("Список задач пуст").getBytes());
                    }
                }
            }
        });

        httpServer.createContext("/Tasks/ClearEpicsList", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                manager.clearEpicsList();
                if(manager.getEpics().isEmpty()) {
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson("Список эпиков очищен").getBytes());
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson("Что-то пошло не так. Не удалось очистить список задач").getBytes());
                    }
                }
            }
        });

        httpServer.createContext("/Tasks/GetEpic", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                int id = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
                try {
                    Epic epic = manager.getEpicById(id);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson(epic).getBytes());
                    }
                } catch (NullPointerException exception) {
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson(exception.getMessage()).getBytes());
                    }
                }
            }
        });

        httpServer.createContext("/Tasks/PostNewEpic", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);
                    manager.getNewEpic(epic);
                    if(manager.getEpics().containsKey(epic.getId())) {
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson("Задача успешно добавлена").getBytes());
                        }
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson("Что-то пошло не так. Добавить новый эпик не удалось").getBytes());
                        }
                    }
            }
        });


        httpServer.start();
        System.out.println("Сервер запущен");
    }

    class GetTasksListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if(!manager.getTasks().isEmpty()){
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(manager.getTasksList()).getBytes());
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Список задач пуст").getBytes());
                }
            }
        }
    }

    class ClearTasksListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            manager.clearTasksList();
            if(manager.getTasks().isEmpty()) {
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Список задач очищен").getBytes());
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Что-то пошло не так. Не удалось очистить список задач").getBytes());
                }
            }
        }
    }

    class GetTaskByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String id = exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
            try {
                Task task = manager.getTaskById(Integer.parseInt(id));
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(task).getBytes());
                }
            } catch (NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }

        }
    }

    class PostNewTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            try {
                manager.getNewTask(task);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача успешно добавлена").getBytes());
                }
            } catch (ManagerSaveException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class UpdateTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            try{
                manager.updateTask(task);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача успешно обновлена").getBytes());
                }
            } catch (ManagerSaveException | NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class RemoveTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int id = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
            try{
                manager.removeTask(id);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача удалена").getBytes());
                }
            } catch (NullPointerException exception){
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class GetSubtaskByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int EpicId = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("&")[0]
                    .split("=")[1]);
            int SubtaskId = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1]
                    .split("&")[1].split("=")[1]);
            try {
                Subtask subtask = manager.getSubtaskById(EpicId, SubtaskId);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(subtask).getBytes());
                }
            } catch (NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class GetSubtaskListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int epicId = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
            try{
                if(!manager.getEpics().get(epicId).getSubtasks().isEmpty()){
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson(manager.getSubtaskList(epicId)).getBytes());
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson("Список подзадач пуст").getBytes());
                    }
                }
            } catch (NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Эпик не найден").getBytes());
                }
            }
        }
    }

    class ClearSubtasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int epicId = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
            try{
                manager.clearSubtasks (epicId);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Список подзадач очищен").getBytes());
                }
            } catch (NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class PostNewSubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            try {
                manager.getNewSubtask(subtask);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача успешно добавлена").getBytes());
                }
            } catch (ManagerSaveException | NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class UpdateSubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            try{
                manager.updateSubtask(subtask);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача успешно обновлена").getBytes());
                }
            } catch (ManagerSaveException | NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }

    class RemoveSubtaskByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int EpicId = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1].split("&")[0]
                    .split("=")[1]);
            int SubtaskId = Integer.parseInt(exchange.getRequestURI().toString().split("\\?")[1]
                    .split("&")[1].split("=")[1]);
            try {
                manager.removeSubtaskById(EpicId, SubtaskId);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson("Задача удалена").getBytes());
                }
            } catch (NullPointerException exception) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(gson.toJson(exception.getMessage()).getBytes());
                }
            }
        }
    }







    class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
        DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd--MM--yyyy, HH:mm:ss,SSS");
        DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy, MM, dd, HH, mm, ss, SSS");

        @Override
        public void write(final JsonWriter jsonWriter, final ZonedDateTime DateTime) throws IOException {
            jsonWriter.value(DateTime.format(formatterWriter));
        }

        @Override
        public ZonedDateTime read(final JsonReader jsonReader) throws IOException {
            return ZonedDateTime.of(LocalDateTime.parse(jsonReader.nextString(), formatterWriter),
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
