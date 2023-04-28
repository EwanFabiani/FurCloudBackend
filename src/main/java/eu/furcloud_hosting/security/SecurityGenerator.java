package eu.furcloud_hosting.security;

import eu.furcloud_hosting.exceptions.PasswordHashingException;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityGenerator {

    private static final int SALT_LENGTH = 16;

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public String hashPassword(String password, byte[] salt) throws PasswordHashingException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Hex.encodeHexString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("No such algorithm");
        }
    }

}
