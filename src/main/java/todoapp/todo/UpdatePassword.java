package todoapp.todo;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UpdatePassword {
    public static boolean updatePassword(String password,String email) {
        try{
            System.out.println(email);
            System.out.println(password);
            Connection cn=DatabaseConnection.getConnection();
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

        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }
    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(4));
    }
}
