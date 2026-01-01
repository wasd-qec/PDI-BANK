package repository;

/**
 * Interface for Admin data access operations
 * Dependency Inversion: High-level modules depend on this abstraction
 */
public interface AdminRepository {
    boolean verifyLogin(String username, String password);
    void createTable();
    void insertDefaultAdmin();
}
