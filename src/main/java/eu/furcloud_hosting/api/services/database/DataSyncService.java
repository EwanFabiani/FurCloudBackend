package eu.furcloud_hosting.api.services.database;

import java.sql.SQLException;

public class DataSyncService {

    public static void beginTransactions(DatabaseService... services) throws SQLException {
        for (DatabaseService service : services) {
            try {
                service.databaseManager.beginTransaction();
            } catch (SQLException e) {
                throw new SQLException("Failed to begin transactions");
            }
        }
    }

    public static void commitTransactions(DatabaseService... services) throws SQLException {
        for (DatabaseService service : services) {
            try {
                service.databaseManager.commitTransaction();
            } catch (SQLException e) {
                throw new SQLException("Failed to begin transactions");
            }
        }
    }

    public static boolean isNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

}
