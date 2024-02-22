package todoapp.todo;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.scene.paint.Color.*;



public class ToDoLoginController {

    @FXML
    private ImageView iconField;
    @FXML
    private ToggleButton passwordShow;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField visiblePasswordField;


    @FXML
    private PasswordField passwordField;

    private MainApp mainApp;
    @FXML
    private Label usererr;
    @FXML
    private  Label passworderr;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void resetError(){
        passworderr.setVisible(false);
        usererr.setVisible(false);
        passwordField.setStyle("-fx-border-color:#7d2ae8");
        usernameField.setStyle("-fx-border-color:#7d2ae8");
        visiblePasswordField.setStyle("-fx-border-color: #7d2ae8");

    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String npassword = visiblePasswordField.getText();

        try {
            String[] validationResults = ClientSideValidator.validate(usernameField, passwordField, visiblePasswordField);

            if (validationResults[0] != null && validationResults[0].equalsIgnoreCase("true")) {
                // Use try-with-resources to automatically close the connection
                try (Connection connection = DatabaseConnection.getConnection()) {
                    if (connection != null) {
                        ResultSet resultSet = LoginValidator.validateLogin(connection, username, password, npassword);

                        if (resultSet.next()) {
                            User u = new User();
                            u.setUsername(resultSet.getString("username"));
                            u.setU_id(resultSet.getInt("u_id"));
                            mainApp.showMainScene();
                            showSuccess();
                        } else {
                            // Clear the password field on unsuccessful login
                            resetError();
                            showError("! Invalid credentials",450);
                        }
                    } else {
                        // Database connection error, show error dialog
                        showError("Validation error! Not connected to database.",260);
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    // Handle other exceptions if needed
                }
            } else {
                // Handle empty fields based on the validation results
                if ("unameEmpty".equalsIgnoreCase(validationResults[0])) {
                    usererr.setText("* username cannot be empty");
                    usererr.setVisible(true);
                }

                if ("passwordEmpty".equalsIgnoreCase(validationResults[1])) {
                    passworderr.setText("* password cannot be empty");
                    passworderr.setVisible(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleForgetPassword(){
        mainApp.showForgetPasswordScene();

    }
    @FXML
    private void gobackHandle(){
        mainApp.showLoginScene();
    }
    @FXML
    private void handleRegister(){
        mainApp.showRegisterScene();
    }



    private void showSuccess(){
        Stage primaryStage = mainApp.getPrimaryStage();

        showCustomSuccessInfoAlert("Login success",primaryStage);
    }


    private void showError(String message,int x_axis) {
        Stage primaryStage = mainApp.getPrimaryStage();

        showCustomErrorInfoAlert(message,primaryStage,x_axis);


    }
    private void showCustomErrorInfoAlert(String content, Stage primaryStage, int x) {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);

        // Customize the appearance
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        Label contentLabel = new Label(content);
//        contentLabel.setStyle("-fx-font-size: 14px;");
        contentLabel.setStyle("-fx-background-color: red;-fx-padding: 3px,3px,3px,3px;-fx-text-fill: white;");
        contentLabel.setFont(new Font("times new roman",18));

        dialog.setX(primaryStage.getX()+x);
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




    @FXML
    private void setPasswordShow() {
//        System.out.println(passwordShow.isSelected());
        passwordShow.setSelected(passwordShow.isSelected());
        updateToggleButtonIcon();
    }

    private void updateToggleButtonIcon() {
        Image icon;
        if (passwordShow.isSelected()) {

            // Use the hide icon
            icon = new Image(getClass().getResource("/todoapp/todo/eye-off.png").toExternalForm());
            if (icon != null ) {

                    passwordField.setVisible(false);
                    visiblePasswordField.setVisible(true);
                    visiblePasswordField.setText(passwordField.getText());
                    iconField.setImage(icon);



            } else {
                System.out.println("Image not found: /todoapp/todo/eye.png");

            }


        } else {
            // Use the show icon
            icon = new Image(getClass().getResource("/todoapp/todo/eye.png").toExternalForm());
            if (icon != null) {
                passwordField.setVisible(true);
                passwordField.setText(visiblePasswordField.getText());
                visiblePasswordField.setVisible(false);

                iconField.setImage(icon);

            } else {
                System.out.println("Image not found: /todoapp/todo/eye.png");
            }


        }


    }


}

