package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VMDatabaseService extends DatabaseService {

    public VMDatabaseService() throws DatabaseException {
    }

    public String getServerOwner(String VMId) throws DatabaseException {
        String query = "SELECT account_id FROM machines WHERE vmid = ?";
        try(ResultSet rs = databaseManager.executeQuery(query, VMId)) {
            if (rs.next()) {
                return rs.getString("account_id");
            } else {
                throw new IllegalArgumentException("No account with session id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get account id from session id");
        }
    }

}
