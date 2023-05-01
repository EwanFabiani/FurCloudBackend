package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.api.services.IDService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import org.apache.commons.dbutils.DbUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAccountService extends DatabaseService {

    public DatabaseAccountService() throws DatabaseException {}

    public String createAccount(String username, String email) throws DatabaseException {
        try {
            String UUID = new IDService().generateID();
            String query = "INSERT INTO accounts (accountid, username, email) VALUES (?, ?, ?)";
            databaseManager.executeUpdate(query, UUID, username, email);
            return UUID;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create account");
        }
    }

    public boolean doesAccountExistUsername(String username) throws DatabaseException {
        String query = "SELECT * FROM accounts WHERE username = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, username)) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if account exists");
        }
        return false;
    }

    public boolean doesAccountExistEmail(String email) throws DatabaseException {
        String query = "SELECT * FROM accounts WHERE email = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, email)) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if account exists");
        }
        return false;
    }

    public void verifyAccount(String accountId) throws DatabaseException {
        String query = "UPDATE accounts SET verified = 1 WHERE accountid = ?";
        try {
            databaseManager.executeUpdate(query, accountId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to verify account");
        }
    }

    public String getAccountFromEmail(String email) throws DatabaseException {
        String query = "SELECT accountid FROM accounts WHERE email = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, email)) {
            if (rs.next()) {
                return rs.getString("accountid");
            }else {
                throw new DatabaseException("No account with email");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get account from email");
        }
    }

    public String getAccountFromUsername(String username) throws DatabaseException {
        String query = "SELECT accountid FROM accounts WHERE username = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, username)) {
            if (rs.next()) {
                return rs.getString("accountid");
            }else {
                throw new DatabaseException("No account with username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get accountId from username");
        }
    }

}
