package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;
    String status;

    public Epic(String title) {
        super(title);
        this.subtasks = new HashMap<>();
        status = setStatus();
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public String setStatus () {
        ArrayList<String> statuses = new ArrayList<>();
        for (Integer key : getSubtasks().keySet()){ // достаем каждую подзадачу
            statuses.add(getSubtasks().get(key).getStatus()); // складываем ее в список
        }
        if (statuses.isEmpty()){
            return "NEW";
        }
        String whatStatus = "DONE";
        for (String status: statuses){
            if(status.equals("NEW")){
                whatStatus = "NEW";
            } else if (status.equals("IN_PROGRESS")) {
                return "IN_PROGRESS";
            }
        }
        return whatStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks) && Objects.equals(status, epic.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks, status);
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "subtasks=" + subtasks +
                ", status='" + setStatus () + '\'' +
                '}';
    }
}
