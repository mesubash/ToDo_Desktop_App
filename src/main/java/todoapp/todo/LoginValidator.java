package todoapp.todo;

import java.sql.*;

public class LoginValidator {

        public static ResultSet validateLogin(String username, String password) throws Exception {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        connection = DatabaseConnection.getConnection();
                        String sql = "SELECT * FROM users WHERE BINARY username=? AND password=?";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);

                        resultSet = preparedStatement.executeQuery();

                        return  resultSet;
                } catch (SQLException e) {
                        throw new Exception("Error validating login", e);


                }


        }
}
