package todoapp.todo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws SQLException {
        this.primaryStage = primaryStage;
//       y
            showLoginScene();
    }

    public void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
            Parent root = loader.load();

            ToDoLoginController loginController = loader.getController();
            loginController.setMainApp(this);

            Scene scene = new Scene(root);

            primaryStage.setResizable(false);


            primaryStage.setScene(scene);
            primaryStage.setTitle("ToDo App - Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void showMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("afterlogin/loggedin_view.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setMainApp(this);
//            mainController.setUserId(userId); // Pass the user ID to the controller
            Scene scene = new Scene(root);

            primaryStage.setMaximized(true);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ToDo App - Main");

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showForgetPasswordScene(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("forgetpassword/ForgetPassword.fxml"));
            Parent root = loader.load();

            ForgetPasswordController forgetPasswordController = loader.getController();
            forgetPasswordController.setMainApp(this);


            Scene scene = new Scene(root);
            primaryStage.setResizable(false);


            primaryStage.setScene(scene);
            primaryStage.setTitle("Forget Password");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showRegisterScene(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register/Register.fxml"));
            Parent root = loader.load();

            RegisterController registerController = loader.getController();
            registerController.setMainApp(this);
            registerController.initialize();

            Scene scene = new Scene(root);
            primaryStage.setResizable(false);


            primaryStage.setScene(scene);
            primaryStage.setTitle("Register User");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void showNextScene(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("forgetpassword/nextForgetView.fxml"));
            Parent root = loader.load();

            ForgetPasswordController controller = loader.getController();
            controller.setMainApp(this);
            controller.setEmail(email);
            controller.initializeNext();

            Scene scene = new Scene(root);

            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Forget Password");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
