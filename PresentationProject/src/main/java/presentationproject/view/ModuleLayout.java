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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import presentationproject.controller.RefreshBtnActionFilter;
import presentationproject.controller.SerialInput;
import presentationproject.model.ConnectedModule;

import java.util.Locale;

public class ModuleLayout {
    private SerialInput serialInput;
    private GridPane root;

    public ModuleLayout(GridPane root, SerialInput serialInput) {
        this.serialInput = serialInput;
        this.root = root;
        root.setVgap(5);
        root.setHgap(5);
        root.setPadding(new Insets(10,10,10,10));
        root.setGridLinesVisible(true);
        buildView();
    }

    public void buildView() {
        cleanRoot();

        /* Add the modules / module images */
        this.addModules();

        /* Add the button(s) */
        this.addRefreshButton();
    }

    public void cleanRoot() {
        // make sure there's nothing in the gridpane before we add to it
        root.getChildren().clear();
        root.getRowConstraints().clear();
        root.getColumnConstraints().clear();
    }

    public void addModules() {
        ConnectedModule[] connectedModules = serialInput.getConnectedModules();
        for( int i = 0; i < 8; i ++ ) {
            Text label = new Text();
            ImageView moduleImg;
            ConnectedModule temp = connectedModules[i];

            if (!temp.getAddress().equals("x")) {
                String text = temp.getAddress() + ": " + temp.getName();
                label.setText(text);

                switch(temp.getModType().strip().toLowerCase()) {
                    case "audio" :
                        moduleImg = new ImageView(new Image("C:\\Users\\7182094\\IdeaProjects\\PresentationProject\\src\\main\\resources\\audio.png"));
                        break;
                    case "d-pad":
                        moduleImg = new ImageView(new Image("C:\\Users\\7182094\\IdeaProjects\\PresentationProject\\src\\main\\resources\\dpad.png"));
                        break;
                    case "joystick":
                        moduleImg = new ImageView(new Image("C:\\Users\\7182094\\IdeaProjects\\PresentationProject\\src\\main\\resources\\joystick.png"));
                        break;
                    default:
                        moduleImg = new ImageView(new Image("C:\\Users\\7182094\\IdeaProjects\\PresentationProject\\src\\main\\resources\\unknown.png"));
                }
            } else {
                label.setText("Disconnected");
                moduleImg = new ImageView(new Image("C:\\Users\\7182094\\IdeaProjects\\PresentationProject\\src\\main\\resources\\disconnected.png"));
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
                serialInput.readInput();
                buildView();
            }
        });
        root.add(refreshBtn, 3, 2);

        RowConstraints btnRow = new RowConstraints();
        btnRow.setPercentHeight(10);
        root.getRowConstraints().add(btnRow);
    }
}
