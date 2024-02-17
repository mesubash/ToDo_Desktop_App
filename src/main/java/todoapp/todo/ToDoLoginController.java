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

import java.io.InputStream;
import java.sql.ResultSet;

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
    }

    @FXML
    private void handleLoginButtonAction() {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String npassword = visiblePasswordField.getText();

        try {
            String[] validationResults = ClientSideValidator.validate(usernameField, passwordField, visiblePasswordField);

            if (validationResults[0].equalsIgnoreCase("true")) {
                ResultSet resultSet = LoginValidator.validateLogin(username, password, npassword);

                if (resultSet.next()) {
                    User u = new User();
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
            }

           if(validationResults[0]!=null){
               if (validationResults[0].equalsIgnoreCase("unameEmpty")) {
                   usererr.setText("* username cannot be empty");
                   usererr.setVisible(true);
               }
           }
           if(validationResults[1]!=null){
               if (validationResults[1].equalsIgnoreCase("passwordEmpty")) {
                   passworderr.setText("* password cannot be empty");
                   passworderr.setVisible(true);
               }
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

