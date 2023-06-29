package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.data.OperatingSystem;
import eu.furcloud_hosting.api.models.PurchaseBody;

public class VMService {

    public static boolean areArgumentsValid(PurchaseBody body) {
        int cores = body.getCores();
        int ram = body.getRam();
        int storage = body.getStorage();
        String os = body.getOs();
        String hostname = body.getHostname();
        if (cores < 1 || cores > 12) {
            return false;
        }
        if (ram < 1024 || ram > 65536 || ram % 1024 != 0) {
            return false;
        }
        if (storage < 10 || storage > 300 || storage % 10 != 0) {
            return false;
        }
        if (!OperatingSystem.isOperatingSystemValid(os)) {
            return false;
        }
        if (hostname == null || !hostname.matches("^[a-zA-Z0-9]{3,64}$")) {
            return false;
        }
        return true;
    }

    public void createServer(PurchaseBody body) {
        //TODO: Implement
    }

}
