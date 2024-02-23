package todoapp.todo;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;



import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
//        selectedfieldshow.setStyle("-fx-background-color:  #708090;-fx-text-fill: white;");

        loadTasks();  // Load existing tasks when the controller is initialized
    }
    @FXML
    private void setSelectedfieldshowAll(){
        selectedfieldshow.setText(taskButton.getText());
//        taskButton.setStyle("-fx-background-color:  #708090;-fx-text-fill: white;");
        loadTasks();
    }

    @FXML
    private void setSelectedfieldshowC() {
        selectedfieldshow.setText(completedTaskButton.getText());
//        completedTaskButton.setStyle("-fx-background-color:  green;-fx-text-fill: white;");

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

                showCustomInfoAlert("New task added!",mainApp.getPrimaryStage(),1000,43,2);

            } else
                System.out.println("Error insertion");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskContainer.fxml"));
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
    private ToDoLoginController loginController;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public Stage getStage(){
        return mainApp.getPrimaryStage();
    }

    public void setLoginController(ToDoLoginController loginController) {
        this.loginController = loginController;
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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
                        Parent root = loader.load();

                        // Set MainApp reference in ToDoLoginController
                        ToDoLoginController loginController = loader.getController();
                        loginController.setMainApp(mainApp);

                        Scene scene = new Scene(root);

                        Stage primaryStage = mainApp.getPrimaryStage();
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("ToDo App - Login");
                        primaryStage.show();

                        Platform.runLater(() -> {
                            showCustomInfoAlert("Logged out successfully!",primaryStage,420,36,3.5);
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        });

    }



    private void showCustomInfoAlert(String content, Stage primaryStage,double x,double y,double duration) {
        Stage dialog = new Stage();

        dialog.initOwner(primaryStage);

        // Customize the appearance
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        Label contentLabel = new Label(content);
        contentLabel.setStyle("-fx-text-fill: white; -fx-background-color: green;-fx-padding: 3px,3px,3px,3px;");
        contentLabel.setFont(new Font("Times new roman",16));
        dialog.setX(primaryStage.getX()+x);
        dialog.setY(primaryStage.getY()+y);


        dialog.setScene(new Scene(new VBox(contentLabel), Color.TRANSPARENT));

        // Use Timeline for a delayed closing effect
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration), event -> {
//                    System.out.println("Timeline finished, closing dialog.");
                    dialog.close();
                })
        );
        timeline.play();


        dialog.initOwner(primaryStage);

        dialog.showAndWait();
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
