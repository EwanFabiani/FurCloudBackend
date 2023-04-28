package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.PasswordHashingException;
import eu.furcloud_hosting.security.SecurityGenerator;
import org.apache.commons.codec.binary.Hex;

import java.sql.SQLException;

public class DatabasePasswordService extends DatabaseService {

    public DatabasePasswordService() throws DatabaseException {
    }

    public void saveCredentials(String accountId, String password) throws DatabaseException, PasswordHashingException {
        try {
            SecurityGenerator securityGenerator = new SecurityGenerator();
            byte[] salt = securityGenerator.generateSalt();
            String hashedPassword = securityGenerator.hashPassword(password, salt);
            String query = "INSERT INTO credentials (accountid, password, salt) VALUES (?, ?, ?)";
            databaseManager.executeUpdate(query, accountId, hashedPassword, Hex.encodeHexString(salt));
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save password");
        }
    }

}
