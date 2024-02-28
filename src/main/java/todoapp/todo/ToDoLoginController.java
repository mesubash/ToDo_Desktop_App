package todoapp.todo;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


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

    public MainApp mainApp;
    @FXML
    private Label usererr;
    @FXML
    private  Label passworderr;

    @FXML
    private RadioButton rememberMeRadioButton;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        RegisterController.setMainApp(this.mainApp);
        ForgetPasswordController.setMainApp(this.mainApp);
    }
    @FXML
    private void resetError(){
        passworderr.setVisible(false);
        usererr.setVisible(false);
        passwordField.setStyle("-fx-border-color:#7d2ae8");
        usernameField.setStyle("-fx-border-color:#7d2ae8");
        visiblePasswordField.setStyle("-fx-border-color: #7d2ae8");

    }
    private void clearFields(){

        passwordField.clear();
        usernameField.clear();
        visiblePasswordField.clear();

    }

    @FXML
    private void handleLoginButtonAction() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String npassword = visiblePasswordField.getText();

        try {
            String[] validationResults = ClientSideValidator.validate(usernameField, passwordField, visiblePasswordField);

            if (validationResults[0] != null && validationResults[0].equalsIgnoreCase("true")) {
                // Use try-with-resources to automatically close the connection
                try (Connection connection = DatabaseConnection.getConnection()) {
                    if (connection != null) {
                        boolean valid = LoginValidator.validateLogin(connection, username, password, npassword);

                        if (valid) {

                            // Check if Remember Me is selected
                            mainApp.showMainScene();
                            showSuccess();
                        } else {
                            clearFields();
                            DialogueController controller = new DialogueController();
                            controller.showErrorDialogue(mainApp.getPrimaryStage(), "Invalid credential", 410, 33, 2);
                        }
                    } else {
                        // Database connection error, show error dialog
                        DialogueController controller = new DialogueController();
                        controller.showErrorDialogue(mainApp.getPrimaryStage(), "Database Connection error", 410, 33, 2);
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    // Database connection error, show error dialog
                    DialogueController controller = new DialogueController();
                    controller.showErrorDialogue(mainApp.getPrimaryStage(), "Database connection Error", 410, 33, 2);
                    e.printStackTrace();
                }
            } else {
                // Handle empty fields based on the validation results
                if ("unameEmpty".equalsIgnoreCase(validationResults[0])) {
                    usererr.setText("* field cannot be empty");
                    usererr.setVisible(true);
                }

                if ("passwordEmpty".equalsIgnoreCase(validationResults[1])) {
                    passworderr.setText("* password cannot be empty");
                    passworderr.setVisible(true);
                }
            }
        } catch (Exception e) {
            DialogueController controller = new DialogueController();
            controller.showErrorDialogue(mainApp.getPrimaryStage(), "Validation Error", 410, 33, 2);
        }
    }


    @FXML
    private void handleForgetPassword(){
        mainApp.showForgetPasswordScene();

    }

    @FXML
    private void handleRegister(){

        mainApp.showRegisterScene();

    }



    private void showSuccess() throws IOException {
        DialogueController controller = new DialogueController();
        controller.showSuccessDialogue(mainApp.getPrimaryStage(),"Login Successful",1100,93,1.3);

    }


    @FXML
    private void setPasswordShow() throws IOException {
//        System.out.println(passwordShow.isSelected());
        passwordShow.setSelected(passwordShow.isSelected());
        updateToggleButtonIcon();
    }

    private void updateToggleButtonIcon() throws IOException {
        Image icon;
        if (passwordShow.isSelected()) {

            // Use the hide icon
            icon = new Image(getClass().getResource("/todoapp/todo/login/icons/eye-off.png").toExternalForm());
            if (icon != null ) {

                    passwordField.setVisible(false);
                    visiblePasswordField.setVisible(true);
                    visiblePasswordField.setText(passwordField.getText());
                    iconField.setImage(icon);



            } else {
                DialogueController controller=new DialogueController();

                controller.showErrorDialogue(mainApp.getPrimaryStage(),"Image not found: eye-off.png",410,33,.5);

            }


        } else {
            // Use the show icon
            icon = new Image(getClass().getResource("/todoapp/todo/login/icons/eye.png").toExternalForm());
            if (icon != null) {
                passwordField.setVisible(true);
                passwordField.setText(visiblePasswordField.getText());
                visiblePasswordField.setVisible(false);
                iconField.setImage(icon);

            } else {
                DialogueController controller=new DialogueController();

                controller.showErrorDialogue(mainApp.getPrimaryStage(),"Image not found: eye.png",410,33,.5);
            }


        }


    }
    @FXML
    private void loginKeyHandle(KeyEvent event) throws IOException {
        if(event.getCode()== KeyCode.ENTER){
            handleLoginButtonAction();
        }
    }

}

