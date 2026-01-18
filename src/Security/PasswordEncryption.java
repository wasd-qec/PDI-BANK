package Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryption {
    
    private static final int SALT_LENGTH = 16;
    
    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        
        String hashedPassword = hashPassword(password, salt);
        
        return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;
    }
    
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
            System.err.println("Error: SHA-256 algorithm not available");
            e.printStackTrace();
        }
    }
}
