package usersettingsgui;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import usersettingsgui.view.ModuleLayout;
import usersettingsgui.view.PortSelection;

public class Main extends Application {
    private Scene scene;
    private Text loadingText = new Text("Loading Modules. . .");
    private GridPane root;
    private Stage primaryStage;
    private String port;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new GridPane();
        root.add(loadingText, 0,0);
        loadingText.setVisible(false);

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
        // the combo box will always be index 1
        this.root.getChildren().remove(1);
        // after removing combo box submit button will also be index 1
        this.root.getChildren().remove(1);
        ModuleLayout moduleLayout = new ModuleLayout(root, primaryStage, this, portName);
        setLoading(true);
        moduleLayout.buildView();
        setLoading(false);
    }

    // this does nothing even though everything I found online suggests it should do something
    public void setLoading(boolean isLoading) {
        loadingText.setVisible(isLoading);
        if(isLoading) {
            scene.setCursor(Cursor.WAIT);
        } else {
            scene.setCursor(Cursor.DEFAULT);
        }
        primaryStage.show();
    }
}