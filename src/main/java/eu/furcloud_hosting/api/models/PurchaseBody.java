package eu.furcloud_hosting.api.models;

public class PurchaseBody {

    //1-12
    private int cores;
    //in MB, 1024-65536
    private int ram;
    //in GB, 10-300, in 10GB increments
    private int storage;
    private String os;
    private String hostname;
    private String sessionID;

    public int getCores() {
        return cores;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public String getOs() {
        return os;
    }

    public String getHostname() {
        return hostname;
    }

    public String getSessionID() {
        return sessionID;
    }
}
