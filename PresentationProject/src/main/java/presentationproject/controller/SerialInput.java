package presentationproject.controller;
import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;

public class SerialInput {
    private static final String COMM_PORT = "COM7";
    private static final int COMM_RATE = 9600;
    public SerialInput() { }

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
                        atEnd = inputArr[numLines - 1].equals("END");
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
}
