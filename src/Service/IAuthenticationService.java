package Service;

/**
 * Interface for authentication operations
 * Single Responsibility: Separate authentication from customer management
 */
public interface IAuthenticationService {
    boolean authenticate(String accNo, String password);
    boolean validatePassword(String accNo, String password);
}
