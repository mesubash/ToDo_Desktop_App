module todoapp.todo {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;


    opens todoapp.todo to javafx.fxml;
    exports todoapp.todo;
}