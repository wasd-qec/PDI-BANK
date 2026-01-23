package Service;

import Object.Customer;
import Object.Transaction;

/**
 * Interface for transaction-related business operations
 * Dependency Inversion: High-level modules depend on abstractions
 */
public interface ITransactionService {
    Transaction processTransfer(Customer sender, Customer receiver, double amount);
    Transaction processDeposit(Customer customer, double amount);
    Transaction processWithdrawal(Customer customer, double amount);
}
