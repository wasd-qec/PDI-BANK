package repository;

import model.Transaction;
import java.util.List;

/**
 * Interface for Transaction data access operations
 * Dependency Inversion: High-level modules depend on this abstraction
 */
public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAll();
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> searchByIdOrUserId(String searchTerm);
    Transaction findById(String transactionId);
}
