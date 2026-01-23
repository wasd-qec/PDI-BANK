package Repository;

/**
 * Interface for admin data access
 * Interface Segregation: small focused interface
 */
public interface IAdminRepository {
    String getPasswordByUsername(String username);
}