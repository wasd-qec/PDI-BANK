package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password encryption utility using SHA-256 with salt
 * Single Responsibility: Only handles password hashing and verification
 */
public class PasswordEncryption {
    
    private static final int SALT_LENGTH = 16;
    
    /**
     * Encrypts a password using SHA-256 with a random salt
     * @param password the plain text password
     * @return encrypted password in format "salt:hash"
     */
    public String encryptPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            String hashedPassword = hashPassword(password, salt);
            return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Alias for encryptPassword for backward compatibility
     */
    public String hashPassword(String password) {
        return encryptPassword(password);
    }
    
    /**
     * Verifies if a plain password matches an encrypted password
     * @param plainPassword the plain text password to verify
     * @param encryptedPassword the stored encrypted password
     * @return true if passwords match, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String encryptedPassword) {
        try {
            String[] parts = encryptedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];
            String hashedInput = hashPassword(plainPassword, salt);
            
            return hashedInput.equals(storedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
