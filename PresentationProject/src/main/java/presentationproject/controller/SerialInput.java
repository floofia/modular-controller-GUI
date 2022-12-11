package presentationproject.controller;
import com.fazecast.jSerialComm.SerialPort;
import presentationproject.model.ConnectedModule;
import java.io.InputStream;

public class SerialInput {
    private ConnectedModule[] connectedModules = {
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x"),
            new ConnectedModule("x", "x", "Passive", "x") };

    public SerialInput() {
        readInput();
    }

    public ConnectedModule[] getConnectedModules() {
        return connectedModules;
    }

    public void readInput() {
        //make sure we're starting with clean modules
        for(int i = 0; i < 8; i++) {
            connectedModules[i] = new ConnectedModule("x", "x", "Passive", "x");
        }
        SerialPort comPort = SerialPort.getCommPort("COM8");
        comPort.setBaudRate(115200);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        String[] inputArr;
        int numLines = 0;
        boolean atEnd = false;
        //this is to make sure we've seen a start
        boolean haveSeenStart = false;

        try {
            String input = "";
            // read until we see END which indicates end of information
            while (numLines < 3 || !atEnd) {
                char c = (char) in.read();
                input += c;
                inputArr = input.split("\n");
                numLines = inputArr.length;
                if (numLines > 2) {
                    for(int i = 0; i < numLines; i++) {
                        if(inputArr[i].contains("START")) {
                            haveSeenStart = true;
                        }
                    }
                    if(haveSeenStart) {
                        atEnd = inputArr[numLines - 1].equals("END");
                    }
                }
                if(atEnd) {
                    parseInput(inputArr, numLines);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }

    protected void parseInput(String[] inputArr, int numLines) {
        int i = 0;
        String currLine = inputArr[i];
        int moduleIdx = 0;
        // ignore anything that comes before START
        while(!currLine.strip().equals("START")) {
            i++;
            currLine = inputArr[i];
        };

        for( ; i < numLines; i++) {
            currLine = inputArr[i];
            if(currLine.equals("END")) {
                return;
            }
            if(currLine.strip().equals("START")) {
                continue;
            }
            String[] moduleInfo = currLine.split(", ");
            if(moduleInfo.length != 4) {
                continue;
            }
            if(moduleIdx >= 8) {
                break;
            }
            connectedModules[moduleIdx] = new ConnectedModule(moduleInfo[0], moduleInfo[1], moduleInfo[2], moduleInfo[3]);
            System.out.println(moduleInfo[3]);
            moduleIdx++;
        }
    }
}
