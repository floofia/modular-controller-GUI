package usersettingsgui.model;

import javafx.stage.Stage;
import usersettingsgui.controller.SerialCommunication;
import usersettingsgui.view.ErrorWindow;

public class ConnectedModules {
    private static final String MOD_INFO_SEPARATOR = ";";
    private int moduleCount = 0;
    private SerialCommunication serialComm;
    private ConnectedModule[] connectedModules = new ConnectedModule[8];
    private Stage stageForErrWin;

    public ConnectedModules(SerialCommunication serialComm, Stage stageForErrWin) {
        this.serialComm = serialComm;
        this.stageForErrWin = stageForErrWin;
    }

    public void clearModules() {
        for( int i = 0; i < 8; i++ ) {
            connectedModules[i] = new ConnectedModule("x", "x", "x", "Pins: 100");
        }
        moduleCount = 0;
    }

    /**
     * @return boolean - whether or not successfully read
     */
    public boolean fetchModules() {
        //only clear the modules if they need to be
        if(!connectedModules[0].getName().equals("x"))
            clearModules();
        String input = serialComm.readInput(stageForErrWin);
        String[] inputArr = input.split("\n");
        int numLines = inputArr.length;
        int i = 0;
        String currLine = inputArr[i];
        int moduleIdx = 0;

        // ignore anything that comes before the first START
        while(!currLine.strip().equals("START")) {
            i++;
            // this means we reached the end without finding the "START" keyword
            if(i >= inputArr.length) {
                new ErrorWindow(stageForErrWin, "There was an error reading in modules, please try again.\nError: END without START");
                return false;
            }
            currLine = inputArr[i];
        }

        int startIdx = i;
        for( ; i < numLines; i++) {
            currLine = inputArr[i];

            if(currLine.equals("END")) {
                break;
            }

            if(currLine.strip().equals("START")) {
                // this means we saw the "START" keyword twice before seeing the "END" keyword
                if(i > startIdx) {
                    new ErrorWindow(stageForErrWin, "There was an error reading in modules (start seen twice), please try again.\nError: START without END");
                    return false;
                }
                //otherwise this is just the first "START" and we wanna skip trying to process
                continue;
            }
            String[] modInfo = currLine.split(MOD_INFO_SEPARATOR);

            // this means a module was passed in with the wrong amount of info
            if(modInfo.length != 4 && modInfo.length != 5) {
                new ErrorWindow(stageForErrWin, "There was an error reading in modules (wrong amount of info), please try again.\nError: MODINFO Length Wrong");
                return false;
            }

            // this means we missed an END or the modules aren't being passed in right
            if(moduleIdx >= 8) {
                new ErrorWindow(stageForErrWin, "There was an error reading in modules (missed an end), please try again.\nError: Too Many Modules");
                return false;
            }

            connectedModules[moduleIdx] = new ConnectedModule(modInfo[0], modInfo[1], modInfo[2], modInfo[3]);
            moduleIdx++;
            moduleCount++;
        }
        return true;
    }

    public ConnectedModule getModuleAtIdx(int i) {
        return connectedModules[i];
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public String getAllAddresses() {
        String addresses = "";
        for(int i = 0; i < getModuleCount(); i++) {
            if(i > 0)
                addresses += ",";
            addresses += connectedModules[i].getAddress();
        }
        return addresses;
    }
}
