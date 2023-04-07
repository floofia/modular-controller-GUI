package usersettingsgui.model;

import usersettingsgui.controller.SerialCommunication;

public class ConnectedModules {
    private static final String MOD_INFO_SEPARATOR = ";";
    private int moduleCount = 0;
    private SerialCommunication serialComm;
    private ConnectedModule[] connectedModules = new ConnectedModule[8];

    public ConnectedModules(SerialCommunication serialComm) {
        this.serialComm = serialComm;
    }

    private void clearModules() {
        for( int i = 0; i < 8; i++ ) {
            connectedModules[i] = new ConnectedModule("x", "x", "x", "Pins: 100");
        }
        moduleCount = 0;
    }

    public void fetchModules() {
        clearModules();
        String input = serialComm.readInput();
        String[] inputArr = input.split("\n");
        int numLines = inputArr.length;
        int i = 0;
        String currLine = inputArr[i];
        int moduleIdx = 0;

        // ignore anything that comes before the first START
        while(!currLine.strip().equals("START")) {
            i++;
            if(i >= inputArr.length) {
                System.out.print("ERROR: Input array too short");
                System.out.println(inputArr.length);
                break;
            }
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
            String[] modInfo = currLine.split(MOD_INFO_SEPARATOR);

            // in this case the module info isn't being passed in right, should have an error
            // 6 because there can be a `; ` at the end of the module info string
            if(modInfo.length != 4 && modInfo.length != 5) {
                System.out.print("ERROR modinfo wrong\nLENGTH: ");
                System.out.println(modInfo.length);
                for(int k = 0; k < modInfo.length; k++) {
                    System.out.print("At index ");
                    System.out.print(k);
                    System.out.println(modInfo[k]);
                }
                continue;
            }

            // in this case we missed an END or the modules aren't being passed in right, should have an error
            if(moduleIdx >= 8) {
                break;
            }

            connectedModules[moduleIdx] = new ConnectedModule(modInfo[0], modInfo[1], modInfo[2], modInfo[3]);
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
