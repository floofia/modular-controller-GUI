package srdesign.team11.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class ModuleSettingsView {
    public ModuleSettingsView(StackPane baseView) {
        Label hello = new Label("Hello World! \nI'm a real settings window! \nNo need to look any further!");

        baseView.getChildren().add(hello);
    }
}
