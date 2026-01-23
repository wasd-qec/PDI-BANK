package Service;

import Security.PasswordEncryption;

/**
 * Adapter pattern: Wraps existing PasswordEncryption to implement interface
 * Open/Closed: Original class unchanged, behavior extended via adapter
 */
public class PasswordEncryptionAdapter implements IPasswordEncryption {
    
    private final PasswordEncryption passwordEncryption;
    
    public PasswordEncryptionAdapter() {
        this.passwordEncryption = new PasswordEncryption();
    }
    
    public PasswordEncryptionAdapter(PasswordEncryption passwordEncryption) {
        this.passwordEncryption = passwordEncryption;
    }
    
    @Override
    public String encryptPassword(String password) throws Exception {
        return passwordEncryption.encryptPassword(password);
    }
    
    @Override
    public boolean verifyPassword(String plainPassword, String encryptedPassword) {
        return passwordEncryption.verifyPassword(plainPassword, encryptedPassword);
    }
}
