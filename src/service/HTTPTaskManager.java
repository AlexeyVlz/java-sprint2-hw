package service;

import API.KVTaskClient;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager{

    String url;
    KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url, Path path) throws IOException, InterruptedException {
        super(path);
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save()  {
        StringBuilder value = new StringBuilder();
        value.append("id,type,name,status,description,startTime,duration,epic");
        for(Task task : tasks.values()) {
            value.append("//").append(task.toString());
        }
        for(Epic epic : epics.values()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy;MM;dd;HH;mm;ss");
            String stringStartTime;
            if(epic.getStartTime() != null){
                stringStartTime = formatter.format(epic.getStartTime());
            } else {
                stringStartTime = "null";
            }
            String epicString = epic.getId() + "," + Types.EPIC + "," + epic.getTitle() + "," + epic.getStatus()
                    + "," + epic.getSpecification() + "," + stringStartTime + ","
                    + (int) epic.getDuration().toMinutes();
            value.append("//").append(epicString);
        }
        for(Epic epic : epics.values()){
            for(Subtask subtask : epic.getSubtasks().values()){
                value.append("//").append(subtask.toString());
            }
        }
        try {
            kvTaskClient.put("manager", value.toString());
        } catch (InterruptedException | IOException exception){
            System.out.println("Не удалось сохранить данные на сервер");
        }
    }


}
