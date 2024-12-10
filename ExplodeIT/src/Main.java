import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Button fetchDataButton = new Button("Fetch Data");

        fetchDataButton.setOnAction(event -> {
            try (Connection connection = DbConfig.connect()) {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM item");

                    while (resultSet.next()) {
                        System.out.println("Data: " + resultSet.getString("ItemID"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(fetchDataButton);
        Scene scene = new Scene(root, 300, 200);

        stage.setScene(scene);
        stage.setTitle("JavaFX and MySQL Demo");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
