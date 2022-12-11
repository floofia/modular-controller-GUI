package presentationproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import presentationproject.controller.SerialInput;
import presentationproject.view.ModuleLayout;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Modules Connected");
        primaryStage.setScene(scene);

        SerialInput serialInput = new SerialInput();

        new ModuleLayout(root, serialInput);

        primaryStage.show();
    }
}