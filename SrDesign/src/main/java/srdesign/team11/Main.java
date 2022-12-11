package srdesign.team11;

import javafx.scene.layout.BorderPane;
import srdesign.team11.view.BaseLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
* TODO:
*   add "Reset to Default"
*   add Update button
*   add windows for each module type
*   add specialized forms for settings
*/
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static final int MINWIDTH = 700;
    public static final int MINHEIGHT = 350;

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Scene scene = new Scene(root/*, MINWIDTH, MINHEIGHT*/);
//        primaryStage.setMinWidth(MINWIDTH);
//        primaryStage.setMinHeight(MINHEIGHT);
        primaryStage.setTitle("Controller Settings");
        primaryStage.setScene(scene);

        new BaseLayout(root);

        primaryStage.show();
    }
}
