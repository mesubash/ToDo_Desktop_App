package todoapp.todo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
                    showSuccessDelete();
                } else {
                    showError("Failed to delete task: " + task.getDescription());
                }
            }
        });
    }
    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.show();
    }

    private void showSuccessDelete(){
        Stage primaryStage=mainController.getStage();

        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);

        // Customize the appearance
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        Label contentLabel = new Label("Successfully Deleted");
        contentLabel.setStyle("-fx-text-fill: white; -fx-background-color: green;-fx-padding: 3px,3px,3px,3px;");
        contentLabel.setFont(new Font("Times new roman",16));
        dialog.setX(primaryStage.getX()+1000);
        dialog.setY(primaryStage.getY()+43);


        dialog.setScene(new Scene(new Group(new VBox(contentLabel)), Color.TRANSPARENT));


        // Use Timeline for a delayed closing effect
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.5), event -> {
//                    System.out.println("Timeline finished, closing dialog.");
                    dialog.close();
                })
        );
        timeline.play();
        dialog.showAndWait();

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

