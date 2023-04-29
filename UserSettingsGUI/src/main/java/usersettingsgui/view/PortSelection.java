package usersettingsgui.view;
import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import usersettingsgui.Main;
import java.util.ArrayList;
import java.util.List;

public class PortSelection {
    private ComboBox portSelBox = new ComboBox();
    private SerialPort[] sPorts;
    private List<String> portNames = new ArrayList<>();

    public PortSelection(GridPane root, Main parentWin) {
        refreshPorts();

        final Button submitBtn = new Button("Select Port");
        final Button refreshPortsBtn = new Button("Refresh Ports");

        submitBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int idx = portNames.indexOf(portSelBox.getValue());
                String sysPortName = sPorts[idx].getSystemPortName();
                parentWin.setModuleLayout(sysPortName);
            }
        });

        refreshPortsBtn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                refreshPorts();
            }
        });

        root.add(portSelBox,1,1);
        root.add(submitBtn,2,1);
        root.add(refreshPortsBtn, 2, 2);

        root.setVgap(5);
        root.setHgap(5);
        root.setPadding(new Insets(10,10,10,10));
    }

    private void refreshPorts() {
        sPorts = SerialPort.getCommPorts();
        int portsCount = sPorts.length;

        portNames.clear();
        for(int i = 0; i < portsCount; i++) {
            portNames.add(sPorts[i].getDescriptivePortName());
        }

        portSelBox.getItems().clear();
        portSelBox.getItems().addAll(FXCollections.observableList(portNames));
    }
}
