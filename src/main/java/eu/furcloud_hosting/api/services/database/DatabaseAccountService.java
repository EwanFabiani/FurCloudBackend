package eu.furcloud_hosting.controllers.database;

import eu.furcloud_hosting.exceptions.DatabaseException;

import java.sql.SQLException;

public class DatabaseAccountService extends DatabaseService {

    public DatabaseAccountService() throws DatabaseException {}

    public void createAccount(String username, String email) throws DatabaseException {
        try {
        
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create account");
        }
    }

}
