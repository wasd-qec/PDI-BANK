package Service;

/**
 * Interface for password encryption
 * Dependency Inversion: Services depend on abstraction, not concrete implementation
 */
public interface IPasswordEncryption {
    String encryptPassword(String password) throws Exception;
    boolean verifyPassword(String plainPassword, String encryptedPassword);
}
