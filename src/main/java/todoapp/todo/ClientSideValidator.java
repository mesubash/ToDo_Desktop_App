package todoapp.todo;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ClientSideValidator {
    public static String[] validate(TextField usernameField, PasswordField passwordField, TextField npassword) {
        String result[] = new String[2];

        if (usernameField == null || usernameField.getText().isEmpty()) {
            result[0] = "unameEmpty";
        }

        if (passwordField == null || passwordField.getText().isEmpty()) {
            result[1] = "passwordEmpty";
        }

        if (result[0] != null || result[1] != null) {
            // Either username or password is empty
            return result;
        }

        // Both username and password are not empty
        result[0] = "true";
        return result;
    }


}
