package usersettingsgui.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorWindow {
    StackPane root;
    Scene errorScene;
    String errorMsg;

    public ErrorWindow(Stage parentStage, String errorMsg) {
        this.errorMsg = errorMsg;
        root = new StackPane();
        errorScene = new Scene(root);

        // New window (Stage)
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");
        errorStage.setScene(errorScene);

        // Specifies the modality for new window.
        errorStage.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        errorStage.initOwner(parentStage);

        // Add the children
        this.buildWin();
        errorStage.show();
    }

    protected void buildWin() {
        root.setPadding(new Insets(20,20,20,20));
        Label mainText = new Label(errorMsg);

        root.getChildren().add(mainText);
    }
}
