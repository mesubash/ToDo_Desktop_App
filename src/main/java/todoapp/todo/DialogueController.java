package todoapp.todo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.*;
import javafx.util.Duration;
import java.io.*;

public class DialogueController {
    @FXML
    private Label errorLabel;
    @FXML
    private Label successLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button copyButton;
    @FXML
    public Button closeButton;

    //info dialogue
    @FXML
    private Label upperLabel;
    @FXML
    private Label lowerLabel;

    private Stage dialogStage;
    private String password;
    public void setPassword(String password){
        this.password=password;
    }

    public void showSuccessDialogue(Stage primaryStage, String message, int x, int y,double duratiom) throws IOException {
        showDialogue(primaryStage, "dialoguebox/Success.fxml", message,"", x, y,duratiom);
    }

    public void showErrorDialogue(Stage primaryStage, String message, int x, int y,double duration) throws IOException {
        showDialogue(primaryStage, "dialoguebox/Error.fxml", message,"", x, y,duration);
    }

    public void showCopyDialogue(Stage primaryStage,int x, int y,double duration) throws IOException{
        showDialogue(primaryStage,"dialoguebox/dialogueWithCopyOption.fxml","","",x,y,duration);
    }
    public void showInfoDialogue(Stage primaryStage,String firstLabel,String secondLabel,int x, int y,double duration) throws IOException{
        showDialogue(primaryStage,"dialoguebox/info.fxml",firstLabel,secondLabel,x,y,duration);
    }

    private void showDialogue(Stage primaryStage, String fxmlPath, String message,String anotherMessage, int x, int y, double duration) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        Parent root = loader.load();

        DialogueController controller = loader.getController();
        if (fxmlPath.equals("dialoguebox/Error.fxml")) {
            controller.setErrorLabel(message);
        } else if (fxmlPath.equals("dialoguebox/Success.fxml")) {
            controller.setSuccessLabel(message);
        }
        else if(fxmlPath.equals("dialoguebox/info.fxml")){
            controller.setLabels(message,anotherMessage);

        }else {
            controller.setPasswordField(password);
        }

        // Use the dialogStage from the controller
        controller.dialogStage = new Stage();
        controller.dialogStage.initStyle(StageStyle.UNDECORATED);
        controller.dialogStage.initModality(Modality.APPLICATION_MODAL);
        controller.dialogStage.initOwner(primaryStage);

        // Set the loaded FXML as the scene
        Scene scene = new Scene(root);
        controller.dialogStage.setScene(scene);

        // Set the position
        controller.dialogStage.setX(primaryStage.getX() + x);
        controller.dialogStage.setY(primaryStage.getY() + y);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration), event -> controller.dialogStage.close())
        );
        timeline.play();

        // Show the dialog
        controller.dialogStage.showAndWait();
    }
    public void setErrorLabel(String message) {
        errorLabel.setText(message);
    }
    public void setSuccessLabel(String message){
        successLabel.setText(message);
    }
    private void setPasswordField(String password){
        passwordLabel.setText(password);
    }
    //info dialogue
    private void setLabels(String upper,String lower){
        upperLabel.setText(upper);
        lowerLabel.setText(lower);
    }

    @FXML
    private void onCloseButtonCliceked(){
        dialogStage.close();
    }
    @FXML

    private void onCopyClicked() throws IOException {

            // Copy the text to the system clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(passwordLabel.getText());
            clipboard.setContent(content);
            copyButton.setStyle("-fx-background-color: green;-fx-text-fill: white");
            copyButton.setText("copied");
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1.3), event -> dialogStage.close())
            );
            timeline.play();

    }

}
