package service;

import repository.AdminRepository;

/**
 * Service class for admin-related operations
 * Single Responsibility: Only handles admin business logic
 */
public class AdminService {
    
    private final AdminRepository adminRepository;
    
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    
    /**
     * Initialize admin system (create table and default admin)
     */
    public void initialize() {
        adminRepository.createTable();
        adminRepository.insertDefaultAdmin();
    }
    
    /**
     * Authenticate an admin by username and password
     * @param username the username
     * @param password the password
     * @return true if authenticated, false otherwise
     */
    public boolean authenticate(String username, String password) {
        return adminRepository.verifyLogin(username, password);
    }
}
