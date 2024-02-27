package todoapp.todo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import todoapp.todo.DialogueController;
import todoapp.todo.MainController;
import todoapp.todo.Task;
import todoapp.todo.TaskHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TaskContainerController {
    @FXML
    private Label taskLabel;
    @FXML
    private Label tasktime;


    @FXML
    private CheckBox completedCheckbox;
    @FXML
    private CheckBox importantCheckbox;
    @FXML
    private Button deleteButton;
    @FXML
    HBox taskContainer;


    private MainController mainController;
    private Task task;





    public void setTask(Task task) {
        this.task = task;
        updateLabels();
    }

    private void updateLabels() {
        taskLabel.setText(task.getDescription());
        completedCheckbox.setSelected(task.isCompleted());
        importantCheckbox.setSelected(task.isImportant());

        LocalDateTime timestamp = task.getTasktime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // adjust the pattern as needed
        String formattedTimestamp = timestamp.format(formatter);

        tasktime.setText("created at: "+formattedTimestamp);


        if (task.isCompleted()) {
            // Set color to green for completed tasks
            taskContainer.setStyle("-fx-background-color: green;");

            taskLabel.setTextFill(Color.WHITE);

            importantCheckbox.setTextFill(Color.WHITE);
            completedCheckbox.setTextFill(Color.WHITE);
            tasktime.setTextFill(Color.WHITE);


        } else if (task.isImportant()) {
            // Set color to purple for important tasks
            taskContainer.setStyle("-fx-background-color: #7d2ae8;");

            taskLabel.setTextFill(Color.WHITE);
            importantCheckbox.setTextFill(Color.WHITE);
            completedCheckbox.setTextFill(Color.WHITE);
            tasktime.setTextFill(Color.WHITE);

        } else {
            // Reset the background color to the default for other tasks
            taskContainer.setStyle("-fx-background-color:white;");
            tasktime.setStyle("-fx-text-fill: #4A5F96");

            taskLabel.setTextFill(Color.BLACK);
            importantCheckbox.setTextFill(Color.BLACK);
            completedCheckbox.setTextFill(Color.BLACK);
        }

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void onMarkCompleted() {
        // Toggle the completion status
        task.setCompleted(!task.isCompleted());
        // Update the UI
        updateLabels();
        TaskHandler.updateCompleted(task.getTask_id(), task.isCompleted());
    }

    @FXML
    private void onMarkImportant() {
        // Toggle the importance status
        task.setImportant(!task.isImportant());
        // Update the UI
        updateLabels();
        TaskHandler.updateImportant(task.getTask_id(), task.isImportant());
    }

    @FXML
    private void onDeleteClicked() {
        int taskId = task.getTask_id();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Task");
        confirmationAlert.setContentText("Are you sure you want to delete the task:\n" + task.getDescription() + "?");

        // Set the style of the alert
        confirmationAlert.initStyle(StageStyle.UNDECORATED);

        // Customize the button types
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show and wait for user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                int rowsAffected = TaskHandler.deleteTask(taskId);

                if (rowsAffected > 0) {
                    mainController.loadTasks();
                    DialogueController controller=new DialogueController();
                    try {
                        controller.showSuccessDialogue(mainController.getStage(),"Task Deleted",1095,95,2.1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    DialogueController controller=new DialogueController();
                    try {
                        controller.showErrorDialogue(mainController.getStage(),"Can't Delete Task",1095,95,2.1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    @FXML
    private void onHoverEffect(){
        deleteButton.setStyle("-fx-background-color: red;");

    }
    @FXML
    private void onHoverExited(){
        deleteButton.setStyle("-fx-background-color: white;");
    }

    @FXML
    private void onLabelHover(){
        taskLabel.setStyle("-fx-font-size: 24px;");

    }

    @FXML
    private void onCompletedHover(){
        completedCheckbox.setStyle("-fx-scale-x: 1.2; -fx-scale-y: 1.2;");

    }
    @FXML
    private void onImportantHover(){
        importantCheckbox.setStyle("-fx-scale-x: 1.2; -fx-scale-y: 1.2;");
    }
    @FXML
    private void resetHover(){
        taskLabel.setStyle("");
        completedCheckbox.setStyle("");
        importantCheckbox.setStyle("");

    }
}

