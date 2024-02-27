package todoapp.todo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    Label selectedfieldshow;
    @FXML
    Label completedTaskButton;
    @FXML
    Label importantTaskButton;

    @FXML
    TextArea taskField;

    @FXML
    Button logoutButton;

    @FXML
    Label topusernameField;
    @FXML
    private ScrollPane tasksScrollPane;

    @FXML
    private VBox tasksContainer;
    @FXML
    private Label taskButton;

    public void initialize() {
        topusernameField.setText("Hey, " + User.getUsername());
        loadTasks();  // Load existing tasks when the controller is initialized
    }
    @FXML
    private void setSelectedfieldshowAll(){
        selectedfieldshow.setText(taskButton.getText());
        loadTasks();
    }

    @FXML
    private void setSelectedfieldshowC() {
        selectedfieldshow.setText(completedTaskButton.getText());
        loadTasks();  // Load completed tasks
    }

    @FXML
    private void setSelectedfieldshowI() {
        selectedfieldshow.setText(importantTaskButton.getText());
        loadTasks();  // Load important tasks
    }

    @FXML
    private void addTask() throws Exception {
        String taskAdded = taskField.getText();
        Boolean imp = false;
        Boolean completed = false;
        int inserted;
        if (taskAdded != null  && !taskAdded.trim().isEmpty()) {
            inserted = TaskHandler.insert(taskAdded, imp, completed);
            taskField.clear();
            if (inserted > 0) {
                loadTasks();
                DialogueController controller=new DialogueController();
                controller.showSuccessDialogue(mainApp.getPrimaryStage(),"New Task Added",1095,95,2.1);

            } else {
                DialogueController controller = new DialogueController();
                controller.showErrorDialogue(mainApp.getPrimaryStage(), "Error Adding Task", 1095, 95, 2.1);

            }
        }
    }

    // Method to load tasks into the UI
    public void loadTasks() {
        tasksContainer.getChildren().clear();  // Clear existing tasks
        List<Task> tasks = TaskHandler.getTasks(selectedfieldshow.getText());  // Get tasks based on the selected field

        // Add tasks to the UI
        for (Task task : tasks) {
            addTaskContainer(task.getDescription(), task);  // Pass the Task to addTaskContainer
        }
    }

    private void addTaskContainer(String taskText, Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task/TaskContainer.fxml"));
            HBox taskContainer = loader.load();

            TaskContainerController taskContainerController = loader.getController();
            taskContainerController.setTask(task);  // Set the Task property

            // Set the MainController reference
            taskContainerController.setMainController(this);

            if (tasksContainer != null) {
                // safely access getChildren() or other methods on tasksContainer
                tasksContainer.getChildren().add(taskContainer);  // Add the loaded TaskContainer to the main VBox
            } else {
                // Handle the case where tasksContainer is null (log an error, throw an exception, etc.)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private MainApp mainApp;
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public Stage getStage(){
        return mainApp.getPrimaryStage();
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show the alert and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {

                    try {
                        mainApp.showLoginScene();

                        Platform.runLater(() -> {
                            DialogueController controller=new DialogueController();
                            try {
                                controller.showSuccessDialogue(mainApp.getPrimaryStage(),"Logged Out!",420,36,2);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        });

    }

    private boolean shiftPressed;

    @FXML
    private void textFieldKeyPressedHandle(KeyEvent event) {
        if (event.getCode() == KeyCode.SHIFT) {
            shiftPressed = true;
        }
    }

    @FXML
    private void textFieldKeyReleasedHandle(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            if (shiftPressed) {
                // Shift + Enter pressed, go to the next line
                taskField.appendText("\n");
            } else {
                // Only Enter pressed, handle the event (replace with your logic)
                addTask();
            }

            // Reset shiftPressed after handling Enter key
            shiftPressed = false;
        }
    }
}
