package todoapp.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    public static int insert(String task, Boolean imp, Boolean completed) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement insertion = connection.prepareStatement("INSERT INTO tasks (task_description, important, completed, u_id) VALUES (?, ?, ?, ?)");
        insertion.setString(1, task);
        insertion.setBoolean(2, imp);
        insertion.setBoolean(3, completed);
        insertion.setInt(4, User.getId());
        int i = insertion.executeUpdate();
        insertion.close();
        connection.close();
        return i;
    }

    public static List<Task> getTasks(String selectedField) {
        List<Task> tasks = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query;

            // Define query based on the selected field
            if ("Completed Tasks".equals(selectedField)) {
                query = "SELECT * FROM tasks WHERE completed = true AND u_id = ?";
            } else if ("Important Tasks".equals(selectedField)) {
                query = "SELECT * FROM tasks WHERE important = true AND u_id = ?";
            } else {
                query = "SELECT * FROM tasks WHERE u_id = ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, User.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setTask_id(resultSet.getInt("task_id"));
                task.setDescription(resultSet.getString("task_description"));
                task.setImportant(resultSet.getBoolean("important"));
                task.setCompleted(resultSet.getBoolean("completed"));

                tasks.add(task);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static int updateImportant(int taskId, boolean important) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE tasks SET important = ? WHERE task_id = ?");
            updateStatement.setBoolean(1, important);
            updateStatement.setInt(2, taskId);

            int updatedRows = updateStatement.executeUpdate();

            updateStatement.close();
            connection.close();

            return updatedRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int updateCompleted(int taskId, boolean completed) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE tasks SET completed = ? WHERE task_id = ?");
            updateStatement.setBoolean(1, completed);
            updateStatement.setInt(2, taskId);

            int updatedRows = updateStatement.executeUpdate();

            updateStatement.close();
            connection.close();

            return updatedRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static int updateTaskDescription(int taskId, String newDescription) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE tasks SET task_description = ? WHERE task_id = ?");
            updateStatement.setString(1, newDescription);
            updateStatement.setInt(2, taskId);

            int updatedRows = updateStatement.executeUpdate();

            updateStatement.close();
            connection.close();

            return updatedRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int deleteTask(int taskId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM tasks WHERE task_id = ?");
            deleteStatement.setInt(1, taskId);

            int deletedRows = deleteStatement.executeUpdate();

            deleteStatement.close();
            connection.close();

            return deletedRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}



