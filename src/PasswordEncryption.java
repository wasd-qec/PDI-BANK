import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/*
 * ===== AVAILABLE FUNCTIONS =====
 * 1. encryptPassword(String password) - Encrypts a password using SHA-256 with random salt
 * 2. verifyPassword(String plainPassword, String encryptedPassword) - Verifies if plain password matches encrypted password
 * 3. hashPassword(String password, byte[] salt) - Private helper method to hash password with given salt
 * ================================
 */
public class PasswordEncryption {
    
    private static final int SALT_LENGTH = 16;
    
    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        
        // Hash the password with the salt
        String hashedPassword = hashPassword(password, salt);
        
        // Return salt:hash format
        return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;
    }
    
    public boolean verifyPassword(String plainPassword, String encryptedPassword) {
        try {
            // Split the encrypted password into salt and hash
            String[] parts = encryptedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];
            
            // Hash the input password with the same salt
            String hashedInput = hashPassword(plainPassword, salt);
            
            // Compare the hashes
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
    
    public static void main(String[] args) {
        try {
            PasswordEncryption pe = new PasswordEncryption();
            
            String[] passwords = {"1280", "1250", "1190", "1150"};
            String[] names = {"Heng", "Both", "Caro", "Rith"};
            
            System.out.println("Copy these lines to Password.csv:");
            System.out.println("-----------------------------------");
            for (int i = 0; i < names.length; i++) {
                String hash = pe.encryptPassword(passwords[i]);
                System.out.println(names[i] + "," + hash);
            }
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}