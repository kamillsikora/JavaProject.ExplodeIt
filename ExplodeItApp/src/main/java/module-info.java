module org.example.explodeitapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.explodeitapp to javafx.fxml;
    exports com.example.app;
}