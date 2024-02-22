package todoapp.todo;

import java.sql.*;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_desktop_app", "root", "");

            return cn;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }


}
