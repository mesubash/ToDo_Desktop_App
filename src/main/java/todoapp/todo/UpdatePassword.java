package todoapp.todo;

import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class UpdatePassword {

    public static boolean updatePassword(String password, String email, Stage primaryStage) throws IOException {
        DialogueController controller = new DialogueController();

        try{
            System.out.println(email);
            System.out.println(password);
            Connection cn=DatabaseConnection.getConnection();
            if(cn!=null){
                String query = "UPDATE users SET password = ? WHERE email = ?";
                PreparedStatement preparedStatement=cn.prepareStatement(query);

                preparedStatement.setString(1,hashPassword(password));
                preparedStatement.setString(2,email);
                int i=preparedStatement.executeUpdate();
                if(i>0){
                    return true ;
                }
                else {
                    return false;
                }

            }else{
                controller.showErrorDialogue(primaryStage,"Database connection error!",320,59,1);
            }
        }catch (Exception e){
            controller.showErrorDialogue(primaryStage,e.getMessage(),320,59,3);
            return false;
        }
        return false;
    }
    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(4));
    }
}
