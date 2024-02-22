package todoapp.todo;

import java.sql.*;

public class LoginValidator {

        public static ResultSet validateLogin(Connection connection,String username, String password, String npassword) throws Exception {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        String sql = "SELECT * FROM users WHERE BINARY username=? AND (password = ? OR password = ?)";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);
                        preparedStatement.setString(3,npassword);

                        resultSet = preparedStatement.executeQuery();

                        return  resultSet;
                } catch (SQLException e) {
                        throw new Exception("Error validating login", e);


                }


        }
}
