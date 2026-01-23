package Repository;

import Object.Customer;
import java.util.List;
import java.util.Optional;

/**
 * Interface Segregation Principle: Split into focused interfaces
 * Repository pattern for Customer data access
 */
public interface ICustomerRepository {
    // CRUD operations
    void save(Customer customer);
    void delete(String customerId);
    Optional<Customer> findByAccNo(String accNo);
    Optional<Customer> findById(String id);
    List<Customer> findAll();
    
    // Existence checks
    boolean existsById(String id);
    boolean existsByAccNo(String accNo);
}
