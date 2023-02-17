package usersettingsgui.model;

public class ConnectedModule {
    protected String address;
    protected String name;
    protected String deviceType;
    protected Number digitalAddr;
    protected Number analogAddr;
    protected String imageFileName;

    public ConnectedModule(String address, String name, String deviceType, String digitalAddr, String analogAddr) {
        this.address = address.strip();
        this.name = name.strip();
        this.deviceType = deviceType.strip();

        setAddresses(digitalAddr, analogAddr);
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
            case "joystick":
                this.imageFileName = "joystick.png";
                break;
            case "x":
                this.imageFileName = "disconnected.png";
                break;
            default:
                this.imageFileName = "unknown.png";
        }
    }

    private void setAddresses(String digitalAddr, String analogAddr) {
        var tempArr = digitalAddr.split(" ");
            try {
                this.digitalAddr = Integer.parseInt(tempArr[1]);
            } catch(Exception e) {
                e.printStackTrace();
            }

        tempArr = digitalAddr.split(" ");
            try {
                this.analogAddr = Integer.parseInt(tempArr[1]);
            } catch(Exception e) {
                e.printStackTrace();
            }
    }

    public String getAddress() {
        return this.address;
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

    public Number getDigitalAddr() {
        return this.digitalAddr;
    }

    public Number getAnalogAddr() {
        return this.analogAddr;
    }
}
