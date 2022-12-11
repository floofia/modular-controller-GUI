package srdesign.team11.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import srdesign.team11.view.ModuleSettingsView;

/*
* TODO: potentially have different Action Filter for each type of module
*  issue w this would be complexity of adding module
*  however, each module type will have its own settings, so that's already tricky
*
* TODO: consider having basic settings coded in e.g., sensitivity is p universal
*/
public class ModuleBtnActionFilter implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent evt) {
        StackPane moduleSettingsLayout = new StackPane();


        Scene moduleSettingsScene = new Scene(moduleSettingsLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Real Settings Window");
        newWindow.setScene(moduleSettingsScene);

        new ModuleSettingsView(moduleSettingsLayout);

        newWindow.show();
    }
}