package eu.furcloud_hosting.api.services;

import eu.furcloud_hosting.api.services.database.DatabasePasswordService;
import eu.furcloud_hosting.exceptions.LoginException;
import eu.furcloud_hosting.exceptions.PasswordHashingException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityService {

    private static final int SALT_LENGTH = 16;

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws PasswordHashingException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Hex.encodeHexString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("No such algorithm");
        }
    }

    public static boolean verifyCredentials(String accountId, String password) throws LoginException {
        try {
            DatabasePasswordService databasePasswordService = new DatabasePasswordService();
            byte[] salt = Hex.decodeHex(databasePasswordService.getSalt(accountId));
            String hashedPassword = databasePasswordService.getHashedPassword(accountId);
            return hashedPassword.equals(hashPassword(password, salt));
        } catch (Exception e) {
            throw new LoginException("Failed to verify credentials", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
