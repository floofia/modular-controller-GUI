package usersettingsgui.controller;
import com.fazecast.jSerialComm.SerialPort;
import usersettingsgui.model.ConnectedModule;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerialCommunication {
    private static final String COMM_PORT = "COM7";
    private static final int COMM_RATE = 115200;
    public SerialCommunication() { }

    public String readInput() {
        SerialPort comPort = SerialPort.getCommPort(COMM_PORT);
        comPort.setBaudRate(COMM_RATE);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        String input = "";
        int numLines = 0;
        boolean atEnd = false;
        boolean haveSeenStart = false;

        try {
            String[] inputArr;
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
                        atEnd = inputArr[numLines - 1].equals("END DATA") || inputArr[numLines - 1].equals("END");
                    }
                }

                if(atEnd) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
        return input;
    }

    public void writeModuleSettings(ConnectedModule modToEdit, String newAddr, String newName) {
        // what we have to send to let device know we want to enter module edit mode
        String indicatorString = "-1\n";
        char[] indicatorCharArr = new char[3];
        char[] currAddrCharArr = new char[100];
        char[] settingsCharArr = new char[300];
        String currAddr = modToEdit.getAddress();

        if(newAddr == "") {
            newAddr = currAddr;
        }

        if(newName == "") {
            newName = modToEdit.getName();
        }
        currAddr += "\n";

        String settings = newAddr + ";" + newName + ";" + modToEdit.getDeviceType() + ";" +
                modToEdit.getDigitalAddr() + ";" + modToEdit.getAnalogAddr() + "\n";

        indicatorString.getChars(0,2, indicatorCharArr, 0);
        currAddr.getChars(0, currAddr.length(), currAddrCharArr, 0);
        settings.getChars(0, settings.length(), settingsCharArr, 0);
        System.out.println(settingsCharArr);

        SerialPort comPort = SerialPort.getCommPort(COMM_PORT);
        comPort.setBaudRate(COMM_RATE);
        comPort.openPort();
        OutputStream out = comPort.getOutputStream();
        InputStream in = comPort.getInputStream();

        try {
            System.out.println(indicatorString);
            comPort.writeBytes(indicatorString.getBytes(StandardCharsets.UTF_8), indicatorString.length());

            String c = in.readAllBytes().toString();
            System.out.println(c);

            System.out.println(currAddr);
            comPort.writeBytes(currAddr.getBytes(StandardCharsets.UTF_8), currAddr.length());
            Thread.sleep(10 * 1000);
            System.out.println(settings);
            comPort.writeBytes(settings.getBytes(StandardCharsets.UTF_8), settings.length());
//
//            System.out.println(settings.getBytes(StandardCharsets.UTF_8));
//            for(int i = 0; i < indicatorCharArr.length; i++) {
//                out.write(indicatorCharArr[i]);
//            }
//            for(int i = 0; i < currAddrCharArr.length; i++) {
//                if(currAddrCharArr[i] == 0) {
//                    break;
//                }
//                out.write(currAddrCharArr[i]);
//            }
//            for(int i = 0; i < settingsCharArr.length; i++) {
//                if(settingsCharArr[i] == 0) {
//                    break;
//                }
//                out.write(settingsCharArr[i]);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        comPort.closePort();
    }
}
