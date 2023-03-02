package usersettingsgui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import usersettingsgui.Main;
import usersettingsgui.model.ConnectedModules;
import usersettingsgui.model.ConnectedModule;

public class ModuleLayout {
    private ConnectedModules connectedModules;
    private GridPane root;
    private Stage primaryStage;
    private Main parentWin;
    private static final String IMAGELOC = "C:\\Users\\7182094\\IdeaProjects\\UserSettingsGUI\\src\\main\\resources\\";

    public ModuleLayout(GridPane root, Stage primaryStage, Main parentWin) {
        this.primaryStage = primaryStage;
        this.root = root;
        this.parentWin = parentWin;
        connectedModules = new ConnectedModules();

        root.setVgap(5);
        root.setHgap(5);
        root.setPadding(new Insets(10,10,10,10));
    }

    public void buildView() {
        parentWin.setLoading(true);
        this.connectedModules.fetchModules();

        /* Add the modules / module images */
        this.addModules();

        /* Add the button(s) */
        this.addRefreshButton();
        parentWin.setLoading(false);
    }

    /**
     * Removes all children and row / column constraints from the root gridpane so that we can update what's in it
     */
    public void cleanRoot() {
        root.getChildren().removeAll();
        root.getRowConstraints().clear();
        root.getColumnConstraints().clear();
    }

    public void addModules() {
        for( int i = 0; i < 8; i ++ ) {
            Text label = new Text();
            ConnectedModule currMod = connectedModules.getModuleAtIdx(i);
            ImageView moduleImg = new ImageView(new Image(IMAGELOC + currMod.getImageFileName()));
            Button settingsBtn = new Button("Edit Settings");

            if (!currMod.getAddress().equals("x")) {
                label.setText(currMod.getAddress() + ": " + currMod.getName());
                ModuleLayout that = this;
                settingsBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        SettingsWindow settingsWin = new SettingsWindow(primaryStage, currMod, that);
                    }
                });
            } else {
                label.setText("Disconnected");
                settingsBtn.setDisable(true);
            }

            int col = i % 4;
            int row = (i / 4);
            VBox box = new VBox(5);
            box.setAlignment(Pos.CENTER);
            box.getChildren().addAll(moduleImg, label, settingsBtn);
            root.add(box, col, row);
        }

        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.CENTER);
        col.setPercentWidth(25);
        root.getColumnConstraints().addAll(col, col, col, col);

        RowConstraints row = new RowConstraints();
        row.setValignment(VPos.CENTER);
        row.setPercentHeight(45);
        /* Adding row twice because we want both module rows (the first 2 rows) to be the same*/
        root.getRowConstraints().addAll(row, row);
    }

    public void addRefreshButton() {
        Button refreshBtn = new Button("Refresh Devices");

        refreshBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buildView();
            }
        });
        root.add(refreshBtn, 3, 2);

        RowConstraints btnRow = new RowConstraints();
        btnRow.setPercentHeight(10);
        root.getRowConstraints().add(btnRow);
    }
}
