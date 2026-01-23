package Repository;

import Object.Customer;
import Object.Transaction;
import java.util.List;
import java.util.Optional;

/**
 * Repository pattern for Transaction data access
 */
public interface ITransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAll();
    List<Transaction> findByCustomer(Customer customer);
    Optional<Transaction> findById(String transactionId);
    boolean exists(String transactionId);
}
