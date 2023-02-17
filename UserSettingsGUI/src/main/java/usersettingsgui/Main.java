package usersettingsgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import usersettingsgui.view.ModuleLayout;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Modules Connected");
        primaryStage.setScene(scene);

        new ModuleLayout(root, primaryStage);

        primaryStage.show();
    }
}