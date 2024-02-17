package todoapp.todo;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ClientSideValidator {
    public static String validate(TextField usernameField, PasswordField passwordField,TextField npassword) {
        if (usernameField == null || usernameField.getText().isEmpty()) {
            return "unameEmpty";

        }
        if ((passwordField == null || passwordField.getText().isEmpty())
                && (npassword == null || npassword.getText().isEmpty())
        ){
            return "passwordEmpty";

        }

        return "true";

    }


}
