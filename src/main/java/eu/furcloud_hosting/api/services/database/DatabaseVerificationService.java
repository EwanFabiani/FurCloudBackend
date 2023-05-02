package eu.furcloud_hosting.api.services.database;

import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.InvalidVerificationCodeException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseVerificationService extends DatabaseService{

    public DatabaseVerificationService() throws DatabaseException {}

    public void saveVerificationCode(String accountId, String code) throws DatabaseException {
        try {
            String query = "INSERT INTO verification (account_id, code) VALUES (?, ?)";
            databaseManager.executeUpdate(query, accountId, code);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save verification code");
        }
    }

    public String getAccountFromVerification(String code) throws DatabaseException, InvalidVerificationCodeException {
        String query = "SELECT * FROM verification WHERE code = ?";
        try (ResultSet rs = databaseManager.executeQuery(query, code);) {
            if (rs.next()) {
                return rs.getString("account_id");
            }else {
                throw new InvalidVerificationCodeException();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check verification code");
        }
    }

    public void deleteVerificationCode(String code) {
        String query = "DELETE FROM verification WHERE code = ?";
        try {
            databaseManager.executeUpdate(query, code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
