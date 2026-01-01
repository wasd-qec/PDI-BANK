package service;

import model.Customer;
import repository.CustomerRepository;
import security.PasswordEncryption;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Service class for customer-related operations
 * Single Responsibility: Only handles customer business logic
 */
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final PasswordEncryption passwordEncryption;
    
    public CustomerService(CustomerRepository customerRepository, PasswordEncryption passwordEncryption) {
        this.customerRepository = customerRepository;
        this.passwordEncryption = passwordEncryption;
    }
    
    /**
     * Authenticate a customer by account number and password
     * @param accNo the account number
     * @param password the plain text password
     * @return the Customer if authenticated, null otherwise
     */
    public Customer authenticate(String accNo, String password) {
        String storedPassword = customerRepository.getPasswordByAccNo(accNo);
        if (storedPassword != null && passwordEncryption.verifyPassword(password, storedPassword)) {
            return customerRepository.findByAccNo(accNo);
        }
        return null;
    }
    
    /**
     * Create a new customer account
     * @param name customer name
     * @param password plain text password
     * @param phone phone number
     * @param address address
     * @param birthDate birth date
     * @param initialBalance initial account balance
     * @return the created Customer
     */
    public Customer createAccount(String name, String password, long phone, 
                                   String address, String birthDate, double initialBalance) {
        String id = generateId("USR");
        String accNo = generateId("ACC");
        String createDate = LocalDate.now().toString();
        String hashedPassword = passwordEncryption.encryptPassword(password);
        
        Customer customer = new Customer(accNo, id, name, hashedPassword, initialBalance,
                                         phone, address, birthDate, createDate, true);
        customerRepository.save(customer);
        
        return customer;
    }
    
    /**
     * Get all customers
     * @return list of all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Search customers by name or account number
     * @param searchTerm the search term
     * @return list of matching customers
     */
    public List<Customer> searchCustomers(String searchTerm) {
        return customerRepository.searchByNameOrAccNo(searchTerm);
    }
    
    /**
     * Find a customer by account number
     * @param accNo the account number
     * @return the Customer if found, null otherwise
     */
    public Customer findByAccNo(String accNo) {
        return customerRepository.findByAccNo(accNo);
    }
    
    private String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
