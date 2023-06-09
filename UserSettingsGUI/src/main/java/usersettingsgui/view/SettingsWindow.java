package usersettingsgui.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import usersettingsgui.controller.SerialCommunication;
import usersettingsgui.model.ConnectedModule;

public class SettingsWindow {
    private final Stage parentStage;
    private final GridPane root;
    private Stage settingsWin;

    private TextField nameField;
    private ComboBox devTypeBox;
    private Label addrLabel = new Label();
    private ComboBox addrField;
    private Label addrNote = new Label();
    private TextField customAddrField;
    private Button saveBtn;
    private Button resetBtn;
    private Button defaultsBtn;

    private ConnectedModule modToEdit;
    private SerialCommunication serialComm;

    private Text[] pinLabelNodes = new Text[16];
    private ComboBox[] pinComboBoxNodes = new ComboBox[16];
    private String newDevType;
    private Integer firstAddrDigit;
    private int pinRowsCount = 0;
    private String allModAddresses;

    private ObservableList<String> devTypeList = FXCollections.observableArrayList("Face Buttons", "Left Trigger", "Right Trigger", "Left Joystick", "Right Joystick", "D-Pad", "Feedback Device", "Custom");
    private String[] devTypeValues = {"Face Buttons", "L Trigger", "R Trigger", "L Joystick", "R Joystick", "D-Pad", "Feedback", "Custom"};
    private String[] joystickPinOptions = {"adc", "adc", "digital"};
    private String[] joystickPinLabels = {"X-Axis Pin #", "Y-Axis Pin #", "Select Pin #"};
    private Number[] joystickPinDefaults = {18, 19, 12};
    private String[] fourButtonPinOptions = {"digital", "digital", "digital", "digital"};
    private String[] fourButtonPinLabels = {"Top Button Pin #", "Bottom Button Pin #", "Left Button Pin #", "Right Button Pin #"};
    private Number[] fourButtonPinDefaults = {13, 18, 14, 19};
    private String[] triggerPinOptions = {"digital", "digital"};
    private String[] triggerPinLabels = {"Top Button Pin #", "Bottom Button Pin #"};
    private Number[] triggerPinDefaults = {18, 14};
    private String[] customPinLabels = {"Parameter 0", "Parameter 1","Parameter 2","Parameter 3","Parameter 4","Parameter 5","Parameter 6","Parameter 7","Parameter 8","Parameter 9","Parameter 10","Parameter 11","Parameter 12","Parameter 13","Parameter 14"};
    private String[] customPinOptions = {"digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital", "digital"};
    private Number[] customPinDefaults = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private Number[] adcCapablePins = {0, 1, 2, 3, 6, 7, 18, 19, 20};
    private Number[] pwmCapablePins = {0, 1, 9, 12, 13};
    private Number[] digitalCapablePins = {0, 1, 2, 3, 20, 5, 6, 7, 8, 9, 12, 13, 14, 18, 19};

    public SettingsWindow(Stage parentStage, ConnectedModule modToEdit, SerialCommunication serialComm, String allModAddresses) {
        this.parentStage = parentStage;
        this.root = new GridPane();
        this.serialComm = serialComm;
        this.modToEdit = modToEdit;
        this.allModAddresses = allModAddresses;

        setModuleThenShow();
    }

    public void setModuleThenShow() {
        ScrollPane sp = new ScrollPane();
        sp.setContent(root);
        Scene settingsScene = new Scene(sp);

        // New window (Stage)
        settingsWin = new Stage();
        settingsWin.setTitle("Edit settings for " + modToEdit.getName());
        settingsWin.setScene(settingsScene);

        // Specifies the modality for new window.
        settingsWin.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        settingsWin.initOwner(this.parentStage);

        // Add the children
        // Note: the save button is added inside the addPins function because it's location depends on how many boxes
        //       are used for Pins
        this.addForm();

        this.root.setAlignment(Pos.CENTER);
        this.root.setPadding(new Insets(20,20,20,20));
        this.root.setHgap(10);
        this.root.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHalignment(HPos.RIGHT);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHgrow(Priority.ALWAYS);
        col4.setHalignment(HPos.LEFT);
        this.root.getColumnConstraints().addAll(col1, col2, col3, col4);

        settingsWin.setX(this.parentStage.getX() + (this.parentStage.getWidth() / 4));
        settingsWin.setY(this.parentStage.getY());
        settingsWin.show();
    }

    private void addForm() {
        Label formLabel = new Label(modToEdit.getName() + " Settings");
        formLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        this.root.add(formLabel, 0, 0, 4, 1);
        GridPane.setHalignment(formLabel, HPos.CENTER);

        // Module Name
        addNameBox();

        // Module Type
        addDevTypeBox();

        // Module Address
        if(firstAddrDigit > 0) {
            addStandardAddrForm(firstAddrDigit);
        } else {
            addCustomAddrForm();
        }

        addPins();
    }

    private void addNameBox() {
        Label nameLabel = new Label("Name: ");
        this.nameField = new TextField();
        nameField.setText(modToEdit.getName());
        this.root.add(nameLabel, 0, 1, 1, 1);
        this.root.add(nameField, 1, 1, 3, 1);
    }

    private void addDevTypeBox() {
        Label devTypeLabel = new Label("Module Type: ");
        this.devTypeBox = new ComboBox(devTypeList);
        newDevType = modToEdit.getDeviceType();
        firstAddrDigit = determineAddrRange(true);
        root.add(addrLabel, 0, 3, 1, 1);
        root.add(addrNote, 0, 4, 4, 1);
        SettingsWindow that = this;

        devTypeBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldVal, Object newVal) {
                that.devTypeChanged(newVal);
            }
        });
        this.root.add(devTypeLabel, 0, 2, 1, 1);
        this.root.add(devTypeBox, 1, 2, 3, 1);
    }

    private void addStandardAddrForm(Integer firstAddrDigit) {
        addrLabel.setText("Address: " + firstAddrDigit);
        addrNote.setText("Note: The address will be a two digit number but the first digit will be " + firstAddrDigit);
        addrField = new ComboBox();
        addrField.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        int addrOnesDigit = modToEdit.getAddressInt() % 10;
        addrField.setValue(addrOnesDigit);

        root.add(addrField, 1, 3, 3, 1);
    }

    private void addCustomAddrForm() {
        addrLabel.setText("Address: ");
        addrNote.setText("Note: The address must be an integer between 70 and 127 (inclusive)");
        customAddrField = new TextField();
        customAddrField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        customAddrField.setText(modToEdit.getAddress());
        root.add(customAddrField, 1, 3, 3, 1);
    }

    private void addPins() {
        Number[] pinValues;
        String[] pinOptions;
        String devType;
        boolean includeNegOne = false;

        String savedDevType = modToEdit.getDeviceType().strip().toLowerCase();
        Integer devTypeIdx = devTypeList.indexOf(devTypeBox.getValue());
        String selectedDevType = devTypeValues[devTypeIdx].strip().toLowerCase();
        boolean typeChanged;
        if(savedDevType.equals(selectedDevType)) {
            typeChanged = false;
            devType = savedDevType;
        } else {
            typeChanged = true;
            devType = selectedDevType;
        }
        String[] labels;

        switch (devType) {
            case "face buttons":
            case "d-pad":
                labels = fourButtonPinLabels;
                pinOptions = fourButtonPinOptions;
                if(typeChanged)
                    pinValues = fourButtonPinDefaults;
                else
                    pinValues = modToEdit.getPins();
                break;
            case "l trigger" :
            case "r trigger" :
                labels = triggerPinLabels;
                pinOptions = triggerPinOptions;
                if(typeChanged)
                    pinValues = triggerPinDefaults;
                else
                    pinValues = modToEdit.getPins();
                break;
            case "l joystick":
            case "r joystick":
                labels = joystickPinLabels;
                pinOptions = joystickPinOptions;
                if(typeChanged)
                    pinValues = joystickPinDefaults;
                else
                    pinValues = modToEdit.getPins();
                break;
            default:
                includeNegOne = true;
                labels = customPinLabels;
                pinOptions = customPinOptions;
                if(typeChanged)
                    pinValues = customPinDefaults;
                else
                    pinValues = modToEdit.getPins();
        }

        for(int i = 0; i < pinLabelNodes.length; i++) {
            if(pinLabelNodes[i] == null)
                break;
            removeNode(pinLabelNodes[i]);
            removeNode(pinComboBoxNodes[i]);
            pinLabelNodes[i] = null;
            pinComboBoxNodes[i] = null;
        }
        pinRowsCount = 0;
        removeButtons();

        for(int i = 0; i < labels.length; i++) {
            Text pinLabel = new Text(labels[i] + ": ");
            ComboBox pinComboBox = new ComboBox();

            switch(pinOptions[i]) {
                case "adc":
                    pinComboBox.getItems().addAll(adcCapablePins);
                    break;
                case "pwm":
                    pinComboBox.getItems().addAll(pwmCapablePins);
                    break;
                default:
                    if(includeNegOne)
                        pinComboBox.getItems().add(-1);
                    pinComboBox.getItems().addAll(digitalCapablePins);
            }

            pinComboBox.setValue(pinValues[i]);
            Integer col = 0;
            Integer row = 5 + i;
            if(i > 7) {
                col = 2;
                row -= 8;
            }
            root.add(pinLabel, col, row, 1, 1);
            root.add(pinComboBox, col + 1, row, 1, 1);
            pinLabelNodes[i] = pinLabel;
            pinComboBoxNodes[i] = pinComboBox;
            // since sometimes we'll go into a second column instead of more rows we just want the highest row number we
            // use
            if(row > pinRowsCount)
                pinRowsCount = row;
        }
        addButtons();
    }

    private String getSelectedPins() {
        String pinsString = "Pins: ";
        for(Integer i = 0; i < pinComboBoxNodes.length; i++) {
            if(pinComboBoxNodes[i] == null)
                break;

            if(i > 0)
                pinsString += ", ";
            pinsString += pinComboBoxNodes[i].getValue().toString();
        }

        return pinsString;
    }

    private Integer determineAddrRange(boolean setVal) {
        switch (newDevType.strip().toLowerCase()) {
            case "face buttons":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(0));};
                return 1;
            case "l trigger" :
            case "left trigger":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(1));};
                return 2;
            case "r trigger" :
            case "right trigger":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(2));};
                return 3;
            case "l joystick":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(3));};
                return 4;
            case "r joystick":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(4));};
                return 5;
            case "d-pad":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(5));};
                return 6;
            case "audio":
            case "lights":
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(6));};
                return -1;
            default:
                if(setVal) {
                    devTypeBox.setValue(devTypeList.get(7));};
                return -1;
        }
    }

    private void addButtons() {
        SettingsWindow that = this;

        defaultsBtn = new Button("Set to Default Pins");
        defaultsBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                that.defaultsBtnPressed();
            }
        });

        resetBtn = new Button("Undo Changes");
        resetBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                that.resetBtnPressed();
            }
        });

        saveBtn = new Button("Save Changes");
        saveBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                that.saveBtnPressed();
            }
        });

        int row = pinRowsCount + 1;
        this.root.add(defaultsBtn, 1, row);
        this.root.add(resetBtn, 2, row);
        this.root.add(saveBtn, 3, row);
    }

    private void removeButtons() {
        removeNode(resetBtn);
        removeNode(saveBtn);
        removeNode(defaultsBtn);
    }

    private void defaultsBtnPressed() {
        Number[] pinValues;
        Integer devTypeIdx = devTypeList.indexOf(devTypeBox.getValue());
        String devType = devTypeValues[devTypeIdx].strip().toLowerCase();

        switch (devType) {
            case "face buttons":
            case "d-pad":
                pinValues = fourButtonPinDefaults;
                break;
            case "l trigger" :
            case "r trigger" :
                pinValues = triggerPinDefaults;
                break;
            case "l joystick":
            case "r joystick":
                pinValues = joystickPinDefaults;
                break;
            default:
                pinValues = customPinDefaults;
        }

        for(int i = 0; i < pinValues.length; i++) {
            pinComboBoxNodes[i].setValue(pinValues[i]);
        }
    }

    private void resetBtnPressed() {
        //address field and pins get set when device type changes
        nameField.setText(modToEdit.getName());
        newDevType = modToEdit.getDeviceType();
        firstAddrDigit = determineAddrRange(true);
    }

    private void saveBtnPressed() {
        disable();
        String newAddr = "";
        if(firstAddrDigit > 0) {
            newAddr = firstAddrDigit + addrField.getValue().toString();
        } else {
            newAddr = customAddrField.getText();
        }
        int newAddrInt = Integer.parseInt(newAddr);

        String newName = nameField.getText();

        String errList = "";
        // name can't contain semi-colons because we use them as delimiters
        if(newName.contains(";")) {
            errList += "Name cannot contain semi-colons\n";
        }

        // address needs to stay in valid range so we don't ruin the hardware
        int lowestValidAddr = getAddrRangeLower();
        int highestValidAddr = getAddrRangeUpper();
        if(newAddrInt < lowestValidAddr || newAddrInt > highestValidAddr){
            errList += "Address must be between " + getAddrRangeString() + "\n";
        }

        // we don't want duplicate addresses but we want to allow the device to keep the same address
        if(newAddrInt != modToEdit.getAddressInt() && allModAddresses.contains(newAddr)) {
                errList += "Address is already in use\n";
        }

        if(errList != "") {
            openErrWin(errList);
            enable();
            return;
        }

        serialComm.writeModuleSettings(modToEdit, newAddr, newName, newDevType, getSelectedPins());
        settingsWin.close();
        enable();
    }

    private int getAddrRangeLower() {
        if(firstAddrDigit > 0) {
            return firstAddrDigit * 10;
        } else {
            return 70;
        }
    }

    private int getAddrRangeUpper() {
        if(firstAddrDigit > 0) {
            return firstAddrDigit * 10 + 9;
        } else {
            return 127;
        }
    }

    private String getAddrRangeString() {
        if(firstAddrDigit > 0) {
            return firstAddrDigit + "0 - " + firstAddrDigit + "9";
        } else {
            return "70 - 127";
        }
    }

    private void openErrWin(String errList) {
        new ErrorWindow(settingsWin, "There are errors with your input:\n" + errList);
    }

    private void disable() {
        this.root.setCursor(Cursor.WAIT);
        this.root.setDisable(true);
    }

    private void enable() {
        this.root.setCursor(Cursor.DEFAULT);
        this.root.setDisable(false);
    }

    private void removeNode(Node nodeToRemove) {
        /* you might think "couldn't you just remove it by index? or by the node itself?" and based on
        documentation, yes, that should work, but you'd still be wrong (T_T;) */
        int idx = root.getChildren().indexOf(nodeToRemove);
        // if index is negative the node doesn't exist
        if(idx > -1)
            root.getChildren().remove(root.getChildren().get(idx));
    }

    private void devTypeChanged(Object newVal) {
        newDevType = devTypeValues[devTypeList.indexOf(newVal)];
        Integer newFirstAddrDigit = determineAddrRange(false);

        if(newFirstAddrDigit < 0 && firstAddrDigit > 0) {
                /* this means we went from a defined type to a custom type so we need to change around the address part
                of the form to reflect that */
            removeNode(addrField);
            addCustomAddrForm();
        } else if(newFirstAddrDigit > 0 && firstAddrDigit < 0) {
            /* this means we went from a custom type to a defined type*/
            removeNode(customAddrField);
            addStandardAddrForm(newFirstAddrDigit);
        } else if(newFirstAddrDigit > 0 && firstAddrDigit > 0) {
            // in this case we just need to change the first digit of the address
            addrLabel.setText("Address: " + newFirstAddrDigit);
            addrNote.setText("Note: The address will be a two digit number but the first digit will be " + newFirstAddrDigit);

            // if we changed back to the original type set the address to the original address
            if(newDevType.equalsIgnoreCase(modToEdit.getDeviceType())) {
                addrField.setValue(modToEdit.getAddressInt() % 10);
            }
            // if we're staying within custom address range and going back to original device type set address to original too
        } else if(newFirstAddrDigit < 0 && firstAddrDigit < 0 && newDevType.equalsIgnoreCase(modToEdit.getDeviceType())) {
            addrField.setValue(modToEdit.getAddress());
        }

        firstAddrDigit = newFirstAddrDigit;
        addPins();
    }
}
