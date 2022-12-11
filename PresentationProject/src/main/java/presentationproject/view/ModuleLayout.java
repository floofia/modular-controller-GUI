package presentationproject.view;

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
import presentationproject.model.ConnectedModules;
import presentationproject.model.ConnectedModule;

public class ModuleLayout {
    private ConnectedModules connectedModules;
    private GridPane root;
    private static final String IMAGELOC = "C:\\Users\\7182094\\IdeaProjects\\PresentationProject\\src\\main\\resources\\";

    public ModuleLayout(GridPane root) {
        this.root = root;

        connectedModules = new ConnectedModules();

        root.setVgap(5);
        root.setHgap(5);
        root.setPadding(new Insets(10,10,10,10));
        buildView();
    }

    public void buildView() {
        cleanRoot();

        /* Add the modules / module images */
        this.addModules();

        /* Add the button(s) */
        this.addRefreshButton();
    }

    /**
     * Removes all children and row / column constraints from the root gridpane so that we can update what's in it
     */
    public void cleanRoot() {
        root.getChildren().clear();
        root.getRowConstraints().clear();
        root.getColumnConstraints().clear();
    }

    public void addModules() {
        for( int i = 0; i < 8; i ++ ) {
            Text label = new Text();
            ConnectedModule currMod = connectedModules.getModuleAtIdx(i);
            ImageView moduleImg = new ImageView(new Image(IMAGELOC + currMod.getImageFileName()));

            if (!currMod.getAddress().equals("x")) {
                label.setText(currMod.getAddress() + ": " + currMod.getName());
            } else {
                label.setText("Disconnected");
            }

            int col = i % 4;
            int row = (i / 4);
            VBox box = new VBox(5);
            box.setAlignment(Pos.CENTER);
            box.getChildren().addAll(moduleImg, label);
            root.add(box, col, row);
        }

        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.CENTER);
        col.setPercentWidth(25);
        root.getColumnConstraints().add(col);

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
                connectedModules.refreshModules();
                buildView();
            }
        });
        root.add(refreshBtn, 3, 2);

        RowConstraints btnRow = new RowConstraints();
        btnRow.setPercentHeight(10);
        root.getRowConstraints().add(btnRow);
    }
}
