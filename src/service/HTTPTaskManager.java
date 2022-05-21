package service;

import API.KVTaskClient;
import model.Epic;
import model.Records;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager{


     private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(url);
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save(String key)  {
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
        value.append("///");
        String prefix = "";
        for(Records history : historyManager.getHistory()){
            value.append(prefix);
            prefix = ",";
            value.append(history.getId());
        }

        try {
            kvTaskClient.put(key, value.toString());
        } catch (InterruptedException | IOException exception){
            System.out.println("Не удалось сохранить данные на сервер");
        }
    }


    static public HTTPTaskManager loadFromServer(String url) throws IOException, InterruptedException {
        HTTPTaskManager manager = new HTTPTaskManager(url);
        String value = manager.kvTaskClient.load(url);
        String[] newTasks = value.split("///");
        String[] newTask = newTasks[0].split("//");
        for(int i = 1; i < newTask.length; i++){
            Records record = fromString(newTasks[i]);
            if (record instanceof Task) {
                Task task = (Task) record;
                manager.getTasks().put(task.getId(), task);
            } else if (record instanceof Epic) {
                Epic epic = (Epic) record;
                manager.getEpics().put(epic.getId(), epic);
            } else if (record instanceof Subtask) {
                Subtask subtask = (Subtask) record;
                manager.getEpics().get(subtask.getEpicId()).getSubtasks().put(subtask.getId(), subtask);
                manager.getEpics().get(subtask.getEpicId()).
                        setStatus(manager.calculateStatus(subtask.getEpicId()));
                manager.getEpics().get(subtask.getEpicId()).setStartTime();
                manager.getEpics().get(subtask.getEpicId()).setDuration();
                manager.getEpics().get(subtask.getEpicId()).setEndTime();
            }
        }
        List<Integer> history = manager.fromStringToList(newTasks[1]);
        for(Integer historyTaskId : history) {
            if(manager.tasks.containsKey(historyTaskId)) {
                manager.historyManager.add(manager.tasks.get(historyTaskId));
            } else if (manager.epics.containsKey(historyTaskId)) {
                manager.historyManager.add(manager.epics.get(historyTaskId));
            } else {
                for(Epic epic : manager.epics.values()) {
                    if(epic.getSubtasks().containsKey(historyTaskId)) {
                        manager.historyManager.add(epic.getSubtasks().get(historyTaskId));
                    }
                }
            }
        }
        int id = 0;
        for(Task task : manager.getTasks().values()){
            manager.prioritizedTasks.add(task);
            if(id < task.getId()){
                id = task.getId();
            }
        }
        for(Epic epic : manager.getEpics().values()){
            if(id < epic.getId()){
                id = epic.getId();
            }
            for(Subtask subtask : epic.getSubtasks().values()){
                manager.prioritizedTasks.add(subtask);
                if(id < subtask.getId()){
                    id = subtask.getId();
                }
            }
        }
        manager.setId(id);
        return manager;
    }

}
