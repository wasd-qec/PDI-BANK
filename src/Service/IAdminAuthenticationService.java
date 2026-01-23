package Service;

/**
 * Interface for admin authentication
 */
public interface IAdminAuthenticationService {
    boolean authenticate(String username, String password);
}