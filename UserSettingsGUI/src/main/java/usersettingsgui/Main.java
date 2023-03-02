package usersettingsgui;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import usersettingsgui.view.ModuleLayout;

public class Main extends Application {
    private Scene scene;
    private Text loadingText = new Text("Loading Modules. . .");

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        int wd = (int) Screen.getPrimary().getBounds().getWidth();
        int ht = (int) Screen.getPrimary().getBounds().getHeight();
        wd *= 0.75;
        ht *= 0.75;
        this.scene = new Scene(root, wd, ht);

        primaryStage.setTitle("Modules Connected");
        primaryStage.setScene(scene);

        ModuleLayout moduleLayout = new ModuleLayout(root, primaryStage, this);
        primaryStage.show();
        setLoading(true);
        moduleLayout.buildView();
        setLoading(false);
    }

    // this does nothing even though everything I found online suggests it should do something
    public void setLoading(boolean isLoading) {
        if(isLoading) {
            scene.setCursor(Cursor.WAIT);
        } else {
            scene.setCursor(Cursor.DEFAULT);
        }
    }
}