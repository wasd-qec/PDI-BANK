package Factory;

import Repository.*;
import Service.*;

/**
 * Factory Pattern: Centralized creation of services with proper dependencies
 * Single Responsibility: Handles object creation and dependency wiring
 * Dependency Inversion: Returns interfaces, not concrete implementations
 */
public class ServiceFactory {
    
    // Singleton repositories (can be changed to use connection pooling)
    private static CustomerRepository customerRepository;
    private static TransactionRepository transactionRepository;
    private static ReportRepository reportRepository;
    private static AdminRepository adminRepository;
    private static IPasswordEncryption passwordEncryption;
    
    // Prevent instantiation
    private ServiceFactory() {}
    
    // ==================== Repository Getters ====================
    
    public static synchronized ICustomerRepository getCustomerRepository() {
        if (customerRepository == null) {
            customerRepository = new CustomerRepository();
        }
        return customerRepository;
    }
    
    public static synchronized ICustomerStatusRepository getCustomerStatusRepository() {
        if (customerRepository == null) {
            customerRepository = new CustomerRepository();
        }
        return customerRepository;
    }
    
    public static synchronized ICustomerUpdateRepository getCustomerUpdateRepository() {
        if (customerRepository == null) {
            customerRepository = new CustomerRepository();
        }
        return customerRepository;
    }
    
    public static synchronized CustomerRepository getCustomerRepositoryFull() {
        if (customerRepository == null) {
            customerRepository = new CustomerRepository();
        }
        return customerRepository;
    }
    
    public static synchronized ITransactionRepository getTransactionRepository() {
        if (transactionRepository == null) {
            transactionRepository = new TransactionRepository();
        }
        return transactionRepository;
    }
    
    public static synchronized IReportRepository getReportRepository() {
        if (reportRepository == null) {
            reportRepository = new ReportRepository();
        }
        return reportRepository;
    }

    public static synchronized IAdminRepository getAdminRepository() {
        if (adminRepository == null) {
            adminRepository = new AdminRepository();
        }
        return adminRepository;
    }
    
    // ==================== Service Getters ====================
    
    public static ICustomerService getCustomerService() {
        return new CustomerServiceImpl(
            getCustomerRepository(),
            getCustomerStatusRepository(),
            getCustomerUpdateRepository(),
            getPasswordEncryption()
        );
    }
    
    public static ITransactionService getTransactionService() {
        return new TransactionServiceImpl(
            getCustomerUpdateRepository(),
            getTransactionRepository()
        );
    }
    
    public static IAuthenticationService getAuthenticationService() {
        return new AuthenticationServiceImpl(
            getCustomerRepositoryFull(),
            getPasswordEncryption()
        );
    }

    public static IAdminAuthenticationService getAdminAuthenticationService() {
        return new AdminAuthenticationServiceImpl(
            getAdminRepository(),
            getPasswordEncryption()
        );
    }
    
    public static IPasswordEncryption getPasswordEncryption() {
        if (passwordEncryption == null) {
            passwordEncryption = new PasswordEncryptionAdapter();
        }
        return passwordEncryption;
    }
    
    // ==================== Legacy Support ====================
    
    /**
     * For backward compatibility with existing code
     * Returns the combined CustomerRepository for legacy access
     */
    public static CustomerRepository getLegacyCustomerRepository() {
        return getCustomerRepositoryFull();
    }
    
    /**
     * For backward compatibility with existing code
     */
    public static TransactionRepository getLegacyTransactionRepository() {
        if (transactionRepository == null) {
            transactionRepository = new TransactionRepository();
        }
        return transactionRepository;
    }
    
    /**
     * For backward compatibility with existing code
     */
    public static ReportRepository getLegacyReportRepository() {
        if (reportRepository == null) {
            reportRepository = new ReportRepository();
        }
        return reportRepository;
    }
}
