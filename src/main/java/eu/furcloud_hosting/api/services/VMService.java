package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.data.OperatingSystem;
import eu.furcloud_hosting.api.models.PurchaseBody;
import eu.furcloud_hosting.api.services.database.DatabaseSessionService;
import eu.furcloud_hosting.api.services.database.VMDatabaseService;
import eu.furcloud_hosting.api.services.fire.FireInfoService;
import eu.furcloud_hosting.exceptions.DatabaseException;

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

    public boolean doesUserOwnServer(String accountId, String vmId) throws DatabaseException {
        VMDatabaseService vmDatabaseService = new VMDatabaseService();
        return vmDatabaseService.getServerOwner(vmId).equals(accountId);
    }

    public void startVM(String sessionId, String vmId) throws DatabaseException, IllegalArgumentException {
        try {
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            String accountId = databaseSessionService.validateSessionAndGetId(sessionId);
            if (!doesUserOwnServer(accountId, vmId)) {
                throw new IllegalArgumentException("You do not own this server!");
            }
            FireInfoService fireInfoService = new FireInfoService();
            fireInfoService.startVM(vmId);
        } catch (DatabaseException e) {
            throw e;
        }
    }

    public void stopVM(String sessionId, String vmId) throws DatabaseException, IllegalArgumentException {
        try {
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            String accountId = databaseSessionService.validateSessionAndGetId(sessionId);
            if (!doesUserOwnServer(accountId, vmId)) {
                throw new IllegalArgumentException("You do not own this server!");
            }
            FireInfoService fireInfoService = new FireInfoService();
            fireInfoService.stopVM(vmId);
        } catch (DatabaseException e) {
            throw e;
        }
    }

    public void restartVM(String sessionId, String vmId) throws DatabaseException, IllegalArgumentException {
        try {
            DatabaseSessionService databaseSessionService = new DatabaseSessionService();
            String accountId = databaseSessionService.validateSessionAndGetId(sessionId);
            if (!doesUserOwnServer(accountId, vmId)) {
                throw new IllegalArgumentException("You do not own this server!");
            }
            FireInfoService fireInfoService = new FireInfoService();
            fireInfoService.restartVM(vmId);
        } catch (DatabaseException e) {
            throw e;
        }
    }

}
