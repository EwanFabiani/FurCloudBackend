package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.PasswordHashingException;
import eu.furcloud_hosting.api.services.SecurityService;
import org.apache.commons.codec.binary.Hex;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabasePasswordService extends DatabaseService {

    public DatabasePasswordService() throws DatabaseException {
    }

    public void saveCredentials(String accountId, String password) throws DatabaseException, PasswordHashingException {
        try {
            byte[] salt = SecurityService.generateSalt();
            String hashedPassword = SecurityService.hashPassword(password, salt);
            String query = "INSERT INTO credentials (accountid, password, salt) VALUES (?, ?, ?)";
            databaseManager.executeUpdate(query, accountId, hashedPassword, Hex.encodeHexString(salt));
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save password");
        }
    }

    public String getSalt(String accountId) throws DatabaseException {
        String query = "SELECT salt FROM credentials WHERE accountid = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, accountId)) {
            if (rs.next()) {
                return rs.getString("salt");
            }else {
                throw new DatabaseException("No account with username");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get salt");
        }
    }

    public String getHashedPassword(String accountId) throws DatabaseException {
        String query = "SELECT password FROM credentials WHERE accountid = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, accountId)) {
            if (rs.next()) {
                return rs.getString("password");
            }else {
                throw new DatabaseException("No account with username");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get hashed password");
        }
    }

}
