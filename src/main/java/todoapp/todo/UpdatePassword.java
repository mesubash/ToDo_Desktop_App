package todoapp.todo;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import static todoapp.todo.MailSender.CodeStore.getUserIdByEmail;

public class UpdatePassword {
    public static boolean updatePassword(String password,String email) {
        try{
            System.out.println(email);
            System.out.println(password);
            Connection cn=DatabaseConnection.getConnection();
            String query = "UPDATE users SET password = ? WHERE u_id = ?";
            PreparedStatement preparedStatement=cn.prepareStatement(query);
            int u_id=getUserIdByEmail(email);
            preparedStatement.setString(1,hashPassword(password));
            preparedStatement.setInt(2,u_id);
            int i=preparedStatement.executeUpdate();
                if(i>0){
                    return true ;
                }
                else {
                    return false;
                }

        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }
    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(4));
    }
}
