package todoapp.todo;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class LoginValidator {

        public static boolean validateLogin(Connection connection, String username, String enteredPassword, String npassword) throws Exception {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        String sql = "SELECT * FROM users WHERE BINARY username=?";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, username);

                        resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                                String hashedPasswordFromDatabase = resultSet.getString("password");
                                // Use BCrypt.checkpw to compare the entered password with the hashed password
                                boolean result=BCrypt.checkpw(enteredPassword, hashedPasswordFromDatabase);
                                if(result){
                                        User u = new User();
                                        u.setUsername(resultSet.getString("username"));
                                        u.setU_id(resultSet.getInt("u_id"));
                                        u.setName(resultSet.getString("name"));

                                }



                                return result;

                        } else {
                                // No user found with the given username
                                return false;
                        }
                } catch (SQLException e) {
                        throw new Exception("Error validating login", e);
                } finally {
                        // Make sure to close the ResultSet and PreparedStatement
                        if (resultSet != null) resultSet.close();
                        if (preparedStatement != null) preparedStatement.close();
                }
        }
}
