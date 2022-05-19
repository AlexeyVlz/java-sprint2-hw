package service;

import API.KVTaskClient;
import model.Epic;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager{

    String url;
    KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url, Path path) throws IOException, InterruptedException {
        super(path);
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
    }

    /*@Override
    public void save() throws IOException, InterruptedException {
        for(Task task : tasks.values()){
            kvTaskClient.put(Integer.toString(task.getId()), task.toString());
        }
        for (Epic epic : epics.values()){

        }
    }*/
}
