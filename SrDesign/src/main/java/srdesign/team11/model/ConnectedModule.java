package srdesign.team11.model;

public class ConnectedModule {
    protected String label;
    protected int connectionSpot;
    protected boolean isConnected = false;

    public ConnectedModule(String label, int connectionSpot) {
        this.label = label;
        this.connectionSpot = connectionSpot;
        if(!label.equals("")) {
            isConnected = true;
        }
    }

    public String getLabel() {
        return this.label;
    }

    public int getConnectionSpot() {
        return this.connectionSpot;
    }
}
