package todoapp.todo;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.ResultSet;

import static javafx.scene.paint.Color.*;

public class ToDoLoginController {

    @FXML
    public HBox rootHBox;

    @FXML
    private TextField usernameField;



    @FXML
    private PasswordField passwordField;

    private MainApp mainApp;
    @FXML
    private Label topusernameField;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            ResultSet resultSet=LoginValidator.validateLogin(username, password);

            if (resultSet.next()) {
                User u = new User();  // Instantiate the User object
                u.setUsername(resultSet.getString("username"));
                u.setU_id(resultSet.getInt("u_id"));
                mainApp.showMainScene();
                showSuccess();




            } else {


                // Clear the password field on unsuccessful login
                usernameField.setStyle("-fx-border-color: red");
                passwordField.setStyle("-fx-border-color: red");
                usernameField.clear();
                passwordField.clear();
                showError("! Invalid credentials");

            }
        } catch (Exception e) {
            // Log the exception or show an error message to the user
            e.printStackTrace();
        }
    }
    private void showSuccess(){
        Stage primaryStage = mainApp.getPrimaryStage();

        showCustomSuccessInfoAlert("Login success",primaryStage);
    }


    private void showError(String message) {
        Stage primaryStage = mainApp.getPrimaryStage();

        showCustomErrorInfoAlert(message,primaryStage);


    }
    private void showCustomErrorInfoAlert(String content, Stage primaryStage) {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);

        // Customize the appearance
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        Label contentLabel = new Label(content);
//        contentLabel.setStyle("-fx-font-size: 14px;");
        contentLabel.setStyle("-fx-background-color: red;-fx-padding: 3px,3px,3px,3px;-fx-text-fill: white;");
        contentLabel.setFont(new Font("times new roman",18));

        dialog.setX(primaryStage.getX()+450);
        dialog.setY(primaryStage.getY()+36);

        dialog.setScene(new Scene(new VBox(contentLabel), TRANSPARENT));

        // Use Timeline for a delayed closing effect
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3.5), event -> {
//                    System.out.println("Timeline finished, closing dialog.");
                    dialog.close();
                })
        );
        timeline.play();


        dialog.initOwner(primaryStage);

        dialog.showAndWait();
    }
    private void showCustomSuccessInfoAlert(String content, Stage primaryStage) {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);

        // Customize the appearance
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        Label contentLabel = new Label(content);
//        contentLabel.setStyle("-fx-font-size: 14px;");
        contentLabel.setStyle("-fx-background-color: green;-fx-padding: 3px,3px,3px,3px;-fx-text-fill: white;");
        contentLabel.setFont(new Font("times new roman",18));

        dialog.setX(primaryStage.getX()+1020);
        dialog.setY(primaryStage.getY()+43);

        dialog.setScene(new Scene(new VBox(contentLabel), TRANSPARENT));

        // Use Timeline for a delayed closing effect
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
//                    System.out.println("Timeline finished, closing dialog.");
                    dialog.close();
                })
        );
        timeline.play();


        dialog.initOwner(primaryStage);

        dialog.showAndWait();
    }


}
