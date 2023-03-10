package usersettingsgui.view;

import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import usersettingsgui.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortSelection {
    public PortSelection(GridPane root, Main parentWin) {
        SerialPort[] sPorts = SerialPort.getCommPorts();
        int portsCount = sPorts.length;
        List<String> portNames = new ArrayList<>();


        for(int i = 0; i < portsCount; i++) {
            portNames.add(sPorts[i].getDescriptivePortName());
        }
        final ComboBox portSelBox = new ComboBox(FXCollections.observableList(portNames));
        final Button submitBtn = new Button("Select Port");

        submitBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int idx = portNames.indexOf(portSelBox.getValue());
                String sysPortName = sPorts[idx].getSystemPortName();
                parentWin.setModuleLayout(sysPortName);
            }
        });

        root.add(portSelBox,1,1);
        root.add(submitBtn,2,1);
    }
}
