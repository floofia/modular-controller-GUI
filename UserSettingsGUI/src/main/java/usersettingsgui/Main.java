package usersettingsgui;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import usersettingsgui.view.ModuleLayout;
import usersettingsgui.view.PortSelection;

public class Main extends Application {
    private Scene scene;
    private GridPane root;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new GridPane();

        int wd = (int) Screen.getPrimary().getBounds().getWidth();
        int ht = (int) Screen.getPrimary().getBounds().getHeight();
        wd *= 0.75;
        ht *= 0.75;
        this.scene = new Scene(root, wd, ht);

        primaryStage.setTitle("Modules Connected");
        primaryStage.setScene(scene);

        PortSelection portSelection = new PortSelection(root, this);
        primaryStage.show();
    }

    public void setModuleLayout(String portName) {
        this.root.getChildren().clear();
        ModuleLayout moduleLayout = new ModuleLayout(root, primaryStage, this, portName);
        moduleLayout.buildView();
    }
}