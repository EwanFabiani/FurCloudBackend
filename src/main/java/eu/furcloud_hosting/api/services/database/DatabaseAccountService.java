package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.api.services.IDService;
import eu.furcloud_hosting.exceptions.AccountNotFoundException;
import eu.furcloud_hosting.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAccountService extends DatabaseService {

    public DatabaseAccountService() throws DatabaseException {}

    public String createAccount(String username, String email) throws DatabaseException {
        try {
            String UUID = IDService.generateID();
            String query = "INSERT INTO accounts (account_id, username, email) VALUES (?, ?, ?)";
            databaseManager.executeUpdate(query, UUID, username, email);
            return UUID;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create account");
        }
    }

    public void verifyAccount(String accountId) throws DatabaseException {
        String query = "UPDATE accounts SET verified = 1 WHERE account_id = ?";
        try {
            databaseManager.executeUpdate(query, accountId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to verify account");
        }
    }

    public String getAccountFromEmail(String email) throws DatabaseException {
        String query = "SELECT account_id FROM accounts WHERE email = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, email)) {
            if (rs.next()) {
                return rs.getString("account_id");
            }else {
                throw new DatabaseException("No account with email");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get account from email");
        }
    }

    public String getAccountFromUsername(String username) throws DatabaseException {
        String query = "SELECT account_id FROM accounts WHERE username = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, username)) {
            if (rs.next()) {
                return rs.getString("account_id");
            }else {
                throw new DatabaseException("No account with username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get accountId from username");
        }
    }

    public String getAccountFromIdentifier(String identifier) throws AccountNotFoundException, DatabaseException {
        String query = "SELECT account_id FROM accounts WHERE username = ? OR email = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, identifier, identifier)) {
            if (rs.next()) {
                return rs.getString("account_id");
            }else {
                throw new AccountNotFoundException("No account with username or email");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get accountId from identifier");
        }
    }

}
