package repository;

import model.Customer;
import java.util.List;

/**
 * Interface for Customer data access operations
 * Dependency Inversion: High-level modules depend on this abstraction
 */
public interface CustomerRepository {
    Customer findByAccNo(String accNo);
    Customer findById(String id);
    String getPasswordByAccNo(String accNo);
    List<Customer> findAll();
    List<Customer> searchByNameOrAccNo(String searchTerm);
    void save(Customer customer);
    void updateBalance(Customer customer);
    void delete(String customerId);
    void createTable();
}
