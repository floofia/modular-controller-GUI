package presentationproject.model;

public class ConnectedModule {
    // each named after input name from Controller Settings GUI Communication Example document
    protected String address;
    protected String name;
    protected String deviceType;
    protected String modType;

    public ConnectedModule(String address, String name, String deviceType, String modType) {
        this.address = address.strip();
        this.name = name.strip();
        this.deviceType = deviceType.strip();
        this.modType = modType.strip();
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
}
