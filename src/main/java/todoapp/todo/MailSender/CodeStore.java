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
            String sql = "INSERT INTO validation_keys (email, validation_code) VALUES (?, ?) ON DUPLICATE KEY UPDATE validation_code = ?";

            if (connection != null) {

                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, userEmail);
                preparedStatement.setString(2, code);
                preparedStatement.setString(3, code);
                preparedStatement.executeUpdate();

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



}