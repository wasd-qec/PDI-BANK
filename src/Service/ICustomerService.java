package Service;

import Object.Customer;

/**
 * Interface for customer-related business operations
 * Dependency Inversion: High-level modules depend on abstractions
 */
public interface ICustomerService {
    Customer createCustomerAccount(String id, String accNo, String name, String password, 
                                   int phone, String address, double balance, String birthDate);
    Customer getCustomerByAccNo(String accNo);
    boolean isAccountActive(String accNo);
    void updatePassword(Customer customer, String newPassword);
    void updateProfile(Customer customer);
    void updateBasicInfo(Customer customer);
    void activateAccount(String accNo);
    void deactivateAccount(String accNo);
    void deleteCustomer(String customerId);
}
