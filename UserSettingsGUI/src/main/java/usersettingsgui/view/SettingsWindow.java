package usersettingsgui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import usersettingsgui.controller.SerialCommunication;
import usersettingsgui.model.ConnectedModule;

public class SettingsWindow {
    private final Stage parentStage;
    private final GridPane root;
    private TextField nameField;
    private TextField addrField;

    public SettingsWindow(Stage parentStage, ConnectedModule modToEdit) {
        this.parentStage = parentStage;
        this.root = new GridPane();
        setModuleThenShow(modToEdit);
    }

    public void setModuleThenShow(ConnectedModule modToEdit) {
        Scene settingsScene = new Scene(root);

        // New window (Stage)
        Stage settingsWin = new Stage();
        settingsWin.setTitle("Edit settings for " + modToEdit.getName());
        settingsWin.setScene(settingsScene);

        // Specifies the modality for new window.
        settingsWin.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        settingsWin.initOwner(this.parentStage);

        // Add all the children
        this.addForm(modToEdit);
        this.addButtons(modToEdit);

        this.root.setAlignment(Pos.CENTER);
        this.root.setPadding(new Insets(20,20,20,20));
        this.root.setHgap(10);
        this.root.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints(200,200, Double.MAX_VALUE);
        col2.setHgrow(Priority.ALWAYS);
        this.root.getColumnConstraints().addAll(col1, col2);

        settingsWin.setX(this.parentStage.getX() + (this.parentStage.getWidth() / 4));
        settingsWin.setY(this.parentStage.getY());
        settingsWin.show();
    }

    private void addForm(ConnectedModule modToEdit) {
        Label formLabel = new Label(modToEdit.getName() + " Settings");
        formLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        this.root.add(formLabel, 0, 0, 2, 1);
        GridPane.setHalignment(formLabel, HPos.CENTER);

        // Module Address
        Label addrLabel = new Label("Address: ");
        this.addrField = new TextField();
        // need to add checking but we're not considering that part of the "basic" window
//        addrField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, ));
        this.root.add(addrLabel, 0, 1);
        this.root.add(addrField, 1, 1);

        // Module Name
        Label nameLabel = new Label("Name: ");
        this.nameField = new TextField();
        this.root.add(nameLabel, 0, 2);
        this.root.add(nameField, 1, 2);
    }

    private void addButtons(ConnectedModule modToEdit) {
        Button resetBtn = new Button("Reset");
        resetBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // set back to values saved on the module
            }
        });;

        Button saveBtn = new Button("Save Changes");
        SettingsWindow that = this;
        saveBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SerialCommunication serialComm = new SerialCommunication();
                serialComm.writeModuleSettings(modToEdit, that.addrField.getText(), that.nameField.getText());
            }
        });

        this.root.add(resetBtn, 1, 3);
        this.root.add(saveBtn, 2, 3);
    }
}
