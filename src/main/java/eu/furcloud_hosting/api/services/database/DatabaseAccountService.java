package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.api.data.Account;
import eu.furcloud_hosting.api.data.AccountStatus;
import eu.furcloud_hosting.api.services.IDService;
import eu.furcloud_hosting.exceptions.AccountNotFoundException;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.RegisterException;
import org.springframework.http.HttpStatus;

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

    public String getAccountIdFromEmail(String email) throws DatabaseException {
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

    public String getAccountIdFromUsername(String username) throws DatabaseException {
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

    public String getAccountIdFromIdentifier(String identifier) throws AccountNotFoundException, DatabaseException {
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

    public AccountStatus getAccountStatus(String accountId) throws DatabaseException {
        String query = "SELECT status FROM accounts WHERE account_id = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, accountId)) {
            if (rs.next()) {
                return AccountStatus.valueOf(rs.getString("status"));
            }else {
                throw new DatabaseException("No account with accountId");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get account from accountId");
        }
    }

    public Account getAccountFromId(String accountId) throws DatabaseException {
        String query = "SELECT * FROM accounts WHERE account_id = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, accountId)) {
            if (rs.next()) {
                return new Account(
                    rs.getString("account_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    AccountStatus.valueOf(rs.getString("status")),
                    rs.getBoolean("admin"),
                    rs.getTimestamp("created_at")
                );
            }else {
                throw new DatabaseException("No account with accountId");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get account from accountId");
        }
    }

    public void doesAccountAlreadyExist(String username, String email) throws RegisterException {
        String query = "SELECT * FROM accounts WHERE username = ? OR email = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, username, email)) {
            if (rs.next()) {
                if (rs.getString("email").equals(email)) {
                    throw new RegisterException("Email is already taken!", HttpStatus.CONFLICT);
                } else {
                    throw new RegisterException("Username is already taken!", HttpStatus.CONFLICT);
                }
            }
        } catch (SQLException e) {
            throw new RegisterException("Failed to check if account already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //public void canAccountUsernameBeChanged(String accountId)

}
