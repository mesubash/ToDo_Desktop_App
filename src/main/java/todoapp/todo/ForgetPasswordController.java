package todoapp.todo;

import javafx.fxml.FXML;

public class ForgetPasswordController {
    static MainApp mapp;
    public static void setMainApp(MainApp mainApp) {
        mapp = mainApp;

    }
    @FXML
    private void gobackHandle(){
        mapp.showLoginScene();
    }

}
