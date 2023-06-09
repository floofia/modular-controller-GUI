package usersettingsgui.controller;
import com.fazecast.jSerialComm.SerialPort;
import javafx.stage.Stage;
import usersettingsgui.model.ConnectedModule;
import usersettingsgui.view.ErrorWindow;

import java.io.*;

public class SerialCommunication {
    private String portName;
    private static final int COMM_RATE = 115200;
    public SerialCommunication() { }

    public String readInput(Stage stageForErrWin) {
        SerialPort comPort = SerialPort.getCommPort(this.portName);
        comPort.setBaudRate(COMM_RATE);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        String input = "";

        try {
            InputStream inStream = comPort.getInputStream();
            InputStreamReader isr = new InputStreamReader(inStream);
            BufferedReader in = new BufferedReader(isr);
            OutputStream outStream = comPort.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outStream);
            BufferedWriter out = new BufferedWriter(osw);
            // write 200 to indicate to device that we want module information
            out.write("200");
            out.flush();

            String currLine;
            int numLines = 0;
            boolean haveSeenStart = false;
            while(true) {
                currLine = in.readLine();

                if(currLine.contains("START")) {
                    haveSeenStart = true;
                }

                if(haveSeenStart) {
                    input += currLine += "\n";
                }

                if(currLine.contains("END")) {
                    in.close();
                    isr.close();
                    inStream.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        comPort.closePort();
        return input;
    }

    /**
     * NOTE: currently can only change name
     *
     * @param modToEdit - the module that is being edited
     * @param newAddr - user input for what to change address to, if set to empty string we'll keep the current value
     * @param newName - user input for what to change name to, if set to empty string we'll keep current value
     */
    public void writeModuleSettings(ConnectedModule modToEdit, String newAddr, String newName, String newDevType, String pinsString) {
        // what we have to send to let device know we want to enter module edit mode from the GUI
        String guiIndicatorString = "-1\n";
        String currAddr = modToEdit.getAddress();

        if(newAddr == "") {
            newAddr = currAddr;
        }
        currAddr += "\n";

        if(newName == "") {
            newName = modToEdit.getName();
        }

        String settings = newAddr + ";" + newName + ";" + newDevType + ";" +
                pinsString + "\n";

        SerialPort comPort = SerialPort.getCommPort(this.portName);
        comPort.setBaudRate(COMM_RATE);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 10000);
        comPort.openPort();
        OutputStream outStream = comPort.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(outStream);
        BufferedWriter out = new BufferedWriter(osw);
        InputStream inStream = comPort.getInputStream();
        InputStreamReader isr = new InputStreamReader(inStream);
        BufferedReader in = new BufferedReader(isr);

        try {
            // we have to read in whenever the device writes out for anything to work
            // please, I'm begging you, just take my word for it and leave these in.readLine()'s as they are
            // unless another Serial.print gets added to the gui interface controller code, then we'll need more
            out.write(guiIndicatorString);
            out.flush();
            Thread.sleep(125);
//            System.out.println(in.readLine());
            in.readLine();
            out.write(currAddr);
            out.flush();
            Thread.sleep(125);
//            System.out.println(in.readLine());
            in.readLine();
            out.write(settings);
            out.flush();
            Thread.sleep(125);
//            System.out.println(in.readLine());
            in.readLine();
//            System.out.println(in.readLine());
            in.readLine();

            out.close();
            in.close();
            osw.close();
            isr.close();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }

    public void setPort(String portName) {
        this.portName = portName;
    }
}
