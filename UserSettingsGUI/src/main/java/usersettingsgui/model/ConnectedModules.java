package usersettingsgui.model;

import usersettingsgui.controller.SerialCommunication;

public class ConnectedModules {
    private static final String MOD_INFO_SEPERATOR = "; ";
    private int moduleCount = 0;
    private static final SerialCommunication serialCommunication = new SerialCommunication();
    private ConnectedModule[] connectedModules = new ConnectedModule[8];

    public ConnectedModules() {
        refreshModules();
    }

    private void clearModules() {
        for( int i = 0; i < 8; i++ ) {
            connectedModules[i] = new ConnectedModule("x", "x", "x", "Digital: 100", "Analog: 100");
        }
        moduleCount = 0;
    }

    public void refreshModules() {
        clearModules();
        String input = serialCommunication.readInput();
        String[] inputArr = input.split("\n");
        int numLines = inputArr.length;
        int i = 0;
        String currLine = inputArr[i];
        int moduleIdx = 0;

        // ignore anything that comes before the first START
        while(!currLine.strip().equals("START")) {
            i++;
            currLine = inputArr[i];
        }

        /* technically this doesn't error handle as thoroughly as we'll want the real code to do it, but that was
           best for getting it done quickly for the presentation
           for this reason I have comments for where and what I'm pretty sure we'll need to watch for so I can address
           this in the true GUI*/
        for( ; i < numLines; i++) {
            currLine = inputArr[i];

            if(currLine.equals("END")) {
                break;
            }

            // in this case we missed an END so we should have an error of some sort
            if(currLine.strip().equals("START")) {
                continue;
            }
            String[] modInfo = currLine.split(MOD_INFO_SEPERATOR);

            // in this case the module info isn't being passed in right, should have an error
            // 6 because there can be a `; ` at the end of the module info string
            if(modInfo.length != 5 && modInfo.length != 6) {
                if(modInfo.length == 3) {
                    modInfo[3] = "Digital 100";
                    modInfo[4] = "Analog 100";
                }
                else {
                    continue;
                }
            }

            // in this case we missed an END or the modules aren't being passed in right, should have an error
            if(moduleIdx >= 8) {
                break;
            }

            connectedModules[moduleIdx] = new ConnectedModule(modInfo[0], modInfo[1], modInfo[2], modInfo[3], modInfo[4]);
            moduleIdx++;
        }
    }

    public ConnectedModule getModuleAtIdx(int i) {
        return connectedModules[i];
    }

    public int getModuleCount() {
        return moduleCount;
    }
}
