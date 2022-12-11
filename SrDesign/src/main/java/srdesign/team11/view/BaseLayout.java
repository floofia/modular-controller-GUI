package srdesign.team11.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import srdesign.team11.controller.ModuleBtnActionFilter;
import srdesign.team11.model.ConnectedModule;

//import java.io.FileInputStream;

public class BaseLayout {
    public BaseLayout(BorderPane root) {
        ConnectedModule mod1 = new ConnectedModule("Joystick", 1);
        ConnectedModule mod2 = new ConnectedModule("", 2);
        ConnectedModule mod3 = new ConnectedModule("4 Buttons", 3);
        ConnectedModule mod4 = new ConnectedModule("", 4);
        /* build buttons for modules */
        Button mod1Btn = buildModule(mod1);
        Button mod2Btn = buildModule(mod2);
        Button mod3Btn = buildModule(mod3);
        Button mod4Btn = buildModule(mod4);

        mod1Btn.setMaxWidth(Double.MAX_VALUE);
        mod2Btn.setMaxWidth(Double.MAX_VALUE);
        mod3Btn.setMaxWidth(Double.MAX_VALUE);
        mod4Btn.setMaxWidth(Double.MAX_VALUE);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        VBox.setVgrow(leftSpacer, Priority.ALWAYS);
        VBox.setVgrow(rightSpacer, Priority.ALWAYS);

        /* group buttons so they'll be "stacked" in 2 groups */
        VBox leftModGroup = new VBox(5, mod1Btn, leftSpacer, mod2Btn);
        VBox rightModGroup = new VBox(5, mod3Btn, rightSpacer, mod4Btn);

        leftModGroup.setSpacing(10);
        rightModGroup.setSpacing(10);

        /* Controller depicted in center */
        ImageView controllerImg = buildCtrl();

        root.setPadding(new Insets(20, 20, 20, 20));
        root.setLeft(leftModGroup);
        root.setCenter(controllerImg);
        root.setRight(rightModGroup);
    }

    /*
    * TODO:
    *  add event listener / function to buttons
    */
    public Button buildModule(ConnectedModule modInfo) {
        String btnText = "Module " + modInfo.getConnectionSpot() + ": ";
        String label = modInfo.getLabel();
        boolean disable = false;

        /* if there isn't a label we want to disable this button and put something
        *  meaningful on the button */
        if(label.isBlank()) {
            label = "Disconnected";
            disable = true;
        }

        btnText += label;

        Button mod = new Button(btnText);
        mod.setDisable(disable);
        mod.addEventFilter(ActionEvent.ACTION, new ModuleBtnActionFilter());

        return mod;
    }

    /* might just want to do a simple image to begin with
    * TODO:
    *  - look into SVG to see how complicated it would be to get img
    *    made into SVG now so we don't have to later
    *  -  */
    public ImageView buildCtrl() {
        ImageView controllerImg = new ImageView(new Image("C:\\Users\\7182094\\IdeaProjects\\SrDesign\\src\\main\\resources\\controllerimg.png"));
        controllerImg.setPreserveRatio(true);
        controllerImg.setFitWidth(450);
        return controllerImg;
    }
}
