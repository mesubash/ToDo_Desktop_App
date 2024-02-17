package todoapp.todo;

import javafx.scene.Node;

import java.time.LocalDateTime;
import java.util.List;

import static javafx.scene.layout.GridPane.setConstraints;

public class Task extends Node {

    private String description;
    private int task_id;
    private LocalDateTime tasktime;
    public void setTasktime(LocalDateTime tasktime){
        this.tasktime=tasktime;
    }
    public LocalDateTime getTasktime(){
        return  tasktime;
    }


    private Boolean important;
    private Boolean completed;

    public static List<Task> getAllTasks() {

        return null;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public String getDescription() {
        return description;

    }

    public void setImportant(boolean completed) {
        this.important=completed;

    }
    public void setCompleted(boolean completed) {
        this.completed=completed;
    }
    public boolean isCompleted(){
        return completed;
    }
    public boolean isImportant(){
        return important;
    }
    public void setTask_id(int id){
        this.task_id=id;
    }

    public int getTask_id(){
        return task_id;
    }
}
