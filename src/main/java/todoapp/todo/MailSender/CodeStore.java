package todoapp.todo.MailSender;

import todoapp.todo.DatabaseConnection;
import todoapp.todo.ForgetPasswordController;
import todoapp.todo.UpdatePassword;

import java.sql.*;

public class CodeStore {
    static String name;

    public static void storeCodeInDatabase(String userEmail, String code) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Your database connection logic (replace with your own database details)
            connection = DatabaseConnection.getConnection();

            // Fetch the u_id associated with the provided email
            int userId = getUserIdByEmail(userEmail);

            String sql = "INSERT INTO validation_keys (user_id, email, validation_code) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE validation_code = ?";
            if (connection != null) {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, userEmail);
                preparedStatement.setString(3, code);
                preparedStatement.setString(4, code);

                if (preparedStatement.executeUpdate() > 0) {
                    ForgetPasswordController.setStoredCode(code);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (you may need to handle exceptions here as well)
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getUserIdByEmail(String userEmail) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int userId = -1;

        try {
            // Your database connection logic (replace with your own database details)
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT u_id,name FROM users WHERE email = ?";
            if (connection != null) {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userEmail);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    userId = resultSet.getInt("u_id");
                    name = resultSet.getString("name");

                }
            }
        } finally {
            // Close resources (you may need to handle exceptions here as well)
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userId;
    }


}