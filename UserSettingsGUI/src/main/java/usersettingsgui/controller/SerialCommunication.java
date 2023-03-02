package usersettingsgui.controller;
import com.fazecast.jSerialComm.SerialPort;
import usersettingsgui.model.ConnectedModule;
import java.io.*;

public class SerialCommunication {
    private static final String COMM_PORT = "COM7";
    private static final int COMM_RATE = 115200;
    public SerialCommunication() { }

    public String readInput() {
        SerialPort comPort = SerialPort.getCommPort(COMM_PORT);
        comPort.setBaudRate(COMM_RATE);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        String input = "";

        try {
            InputStream inStream = comPort.getInputStream();
            InputStreamReader isr = new InputStreamReader(inStream);
            BufferedReader in = new BufferedReader(isr);

            String currLine;
            int numLines = 0;
            boolean haveSeenStart = false;
            while(true) {
                currLine = in.readLine();
                System.out.println(currLine);

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

                numLines = input.split("\n").length;
                if(numLines > 10) {
                    System.out.println("Error: more than 10 lines (8 modules+2 indicator lines) from device.");
                } else if(numLines > 18) {
                    System.out.println("Too much info from device. Stopping read.");
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
    public void writeModuleSettings(ConnectedModule modToEdit, String newAddr, String newName) {
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

        String settings = newAddr + ";" + newName + ";" + modToEdit.getDeviceType() + ";" +
                modToEdit.getDigitalAddr() + ";" + modToEdit.getAnalogAddr() + "\n";
        System.out.println(settings);

        SerialPort comPort = SerialPort.getCommPort(COMM_PORT);
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
            out.write(guiIndicatorString);
            out.flush();
            Thread.sleep(125);
            in.readLine();
            out.write(currAddr);
            out.flush();
            Thread.sleep(125);
            in.readLine();
            out.write(settings);
            out.flush();
            Thread.sleep(125);
            in.readLine();

            System.out.println("Done");

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
}
