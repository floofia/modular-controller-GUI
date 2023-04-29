package usersettingsgui.model;

import java.util.Arrays;

public class ConnectedModule {
    protected String address;
    protected String name;
    protected String deviceType;
    protected Integer[] pins = new Integer[20];
    protected String imageFileName;

    public ConnectedModule(String address, String name, String deviceType, String pins) {
        this.address = address.strip();
        this.name = name.strip();
        this.deviceType = deviceType.strip();

        if(this.getAddressInt() > 127) {
            this.address = "73";
        }

        setPins(pins);
        setImageFileName();
    }

    private void setImageFileName() {
        switch(this.getDeviceType().strip().toLowerCase()) {
            case "audio" :
                this.imageFileName = "audio.png";
                break;
            case "d-pad":
                this.imageFileName = "dpad.png";
                break;
            case "l joystick":
                this.imageFileName = "leftjoystick.png";
                break;
            case "r joystick":
                this.imageFileName = "rightjoystick.png";
                break;
            case "l trigger":
                this.imageFileName = "lefttrigger.png";
                break;
            case "r trigger":
                this.imageFileName = "righttrigger.png";
                break;
            case "face buttons":
                this.imageFileName = "facebuttons.png";
                break;
            case "x":
                this.imageFileName = "disconnected.png";
                break;
            default:
                this.imageFileName = "unknown.png";
        }
    }

    private void setPins(String givenPins) {
        Arrays.fill(pins, -1);
        // there's a label before the pin numbers a la "Pins: 1, 2, 3"
        givenPins = givenPins.split(": ")[1];
        String[] pinsArr = givenPins.split(", ");
        Integer pinsCount = pinsArr.length;
        for(int i = 0; i < pinsCount; i++) {
            try {
                Integer pin = Integer.parseInt(pinsArr[i]);
                pins[i] = pin;
            } catch(Exception e) {
            }
        }
    }

    public String getAddress() {
        return this.address;
    }

    public int getAddressInt() {
        try {
            return Integer.parseInt(this.address);
        } catch(Exception e) {
            return -1;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }

    public Number[] getPins() {
        return this.pins;
    }

    public String getFullPinsString() {
        String ret = "Pins: ";
        for(int i = 0; i < pins.length; i++) {
            // once we start getting -1 we're past the initialized pins
            if(pins[i] == -1) {
                break;
            }
            if(i > 0) {
                ret += ", ";
            }
            ret += pins[i];
        }

        ret+= ";";
        return ret;
    }
}
