package Service;

import Object.Customer;
import Repository.ICustomerRepository;
import Repository.ICustomerStatusRepository;
import Repository.ICustomerUpdateRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Single Responsibility: Handles customer business logic
 * Dependency Inversion: Depends on abstractions (interfaces), not concrete classes
 * Open/Closed: Can extend behavior without modifying this class
 */
public class CustomerServiceImpl implements ICustomerService {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final ICustomerRepository customerRepository;
    private final ICustomerStatusRepository statusRepository;
    private final ICustomerUpdateRepository updateRepository;
    private final IPasswordEncryption passwordEncryption;
    
    // Dependency Injection via constructor
    public CustomerServiceImpl(ICustomerRepository customerRepository,
                               ICustomerStatusRepository statusRepository,
                               ICustomerUpdateRepository updateRepository,
                               IPasswordEncryption passwordEncryption) {
        this.customerRepository = customerRepository;
        this.statusRepository = statusRepository;
        this.updateRepository = updateRepository;
        this.passwordEncryption = passwordEncryption;
    }
    
    @Override
    public Customer createCustomerAccount(String id, String accNo, String name, String password,
                                          int phone, String address, double balance, String birthDate) {
        try {
            String hashedPassword = passwordEncryption.encryptPassword(password);
            String createDate = LocalDateTime.now().format(FORMATTER);
            
            Customer customer = new Customer(
                name,
                hashedPassword,
                id,
                accNo,
                balance,
                phone,
                address,
                birthDate,
                createDate,
                true  // Active by default
            );
            
            customerRepository.save(customer);
            return customer;
        } catch (Exception e) {
            System.out.println("Error creating customer account: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Customer getCustomerByAccNo(String accNo) {
        return customerRepository.findByAccNo(accNo).orElse(null);
    }
    
    @Override
    public boolean isAccountActive(String accNo) {
        return statusRepository.isActive(accNo);
    }
    
    @Override
    public void updatePassword(Customer customer, String newPassword) {
        try {
            String hashedPassword = passwordEncryption.encryptPassword(newPassword);
            updateRepository.updatePassword(customer.getAccNo(), hashedPassword);
        } catch (Exception e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }
    
    @Override
    public void updateProfile(Customer customer) {
        updateRepository.updateFullProfile(customer);
    }
    
    @Override
    public void updateBasicInfo(Customer customer) {
        updateRepository.updateBasicInfo(customer);
    }
    
    @Override
    public void activateAccount(String accNo) {
        statusRepository.activate(accNo);
    }
    
    @Override
    public void deactivateAccount(String accNo) {
        statusRepository.deactivate(accNo);
    }
    
    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.delete(customerId);
    }
}
