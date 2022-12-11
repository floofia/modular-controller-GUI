package presentationproject.model;

public class ConnectedModule {
    // each named after input name from Controller Settings GUI Communication Example document
    protected String address;
    protected String name;
    protected String deviceType;
    protected String modType;
    protected String imageFileName;

    public ConnectedModule(String address, String name, String deviceType, String modType) {
        this.address = address.strip();
        this.name = name.strip();
        this.deviceType = deviceType.strip();
        this.modType = modType.strip();

        setImageFileName();
    }

    private void setImageFileName() {
        switch(this.getModType().strip().toLowerCase()) {
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

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getModType() {
        return this.modType;
    }

    public String getImageFileName() { return this.imageFileName; }
}
