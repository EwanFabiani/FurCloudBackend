package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.database.DatabaseManager;
import eu.furcloud_hosting.exceptions.DatabaseException;

import java.sql.SQLException;

public abstract class DatabaseService {

    protected DatabaseManager databaseManager;

    public DatabaseService() throws DatabaseException {
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database");
        }
    }

}
