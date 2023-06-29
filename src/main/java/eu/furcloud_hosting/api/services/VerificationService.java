package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.services.database.DataSyncService;
import eu.furcloud_hosting.api.services.database.DatabaseAccountService;
import eu.furcloud_hosting.api.services.database.DatabaseVerificationService;
import eu.furcloud_hosting.exceptions.DatabaseException;
import eu.furcloud_hosting.exceptions.InvalidVerificationCodeException;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;
import java.sql.SQLException;

public class VerificationService {

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[12];
        random.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }

    private void saveVerificationCode(String accountId, String verificationCode) throws DatabaseException {
        try {
            DatabaseVerificationService databaseVerificationService = new DatabaseVerificationService();
            databaseVerificationService.saveVerificationCode(accountId, verificationCode);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to save verification code");
        }

    }

    public String createVerificationCode(String accountId) throws DatabaseException {
        String verificationCode = generateVerificationCode();
        try {
            saveVerificationCode(accountId, verificationCode);
            //TODO: Send verification email
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to create Verification Code");
        }
        return verificationCode;
    }

    public void sendVerificationEmail(String email, String accountId, String verificationCode) {

    }

    public void verifyAccount(String code) throws DatabaseException, InvalidVerificationCodeException {
        DatabaseVerificationService databaseVerificationService = new DatabaseVerificationService();
        DatabaseAccountService databaseAccountService = new DatabaseAccountService();
        try {
            DataSyncService.beginTransactions(databaseAccountService, databaseVerificationService);
            String accountId = databaseVerificationService.getAccountFromVerification(code);
            databaseAccountService.verifyAccount(accountId);
            databaseVerificationService.deleteVerificationCode(code);
            DataSyncService.commitTransactions(databaseAccountService, databaseVerificationService);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
