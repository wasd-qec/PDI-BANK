package service;

import model.Customer;
import model.Transaction;
import repository.CustomerRepository;
import repository.TransactionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Service class for handling banking transactions
 * Single Responsibility: Only handles transaction business logic
 * Dependency Inversion: Depends on repository interfaces, not implementations
 */
public class TransactionService {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }
    
    /**
     * Process a withdrawal transaction
     * @param customer the customer withdrawing money
     * @param amount the amount to withdraw
     * @return the Transaction object if successful, null otherwise
     */
    public Transaction processWithdrawal(Customer customer, double amount) {
        if (!customer.withdraw(amount)) {
            return null;
        }
        
        Transaction transaction = createTransaction(
            customer.getId(), customer.getId(), amount, "WITHDRAWAL"
        );
        
        transactionRepository.save(transaction);
        customerRepository.updateBalance(customer);
        
        return transaction;
    }
    
    /**
     * Process a deposit transaction
     * @param customer the customer depositing money
     * @param amount the amount to deposit
     * @return the Transaction object if successful, null otherwise
     */
    public Transaction processDeposit(Customer customer, double amount) {
        if (!customer.deposit(amount)) {
            return null;
        }
        
        Transaction transaction = createTransaction(
            customer.getId(), customer.getId(), amount, "DEPOSIT"
        );
        
        transactionRepository.save(transaction);
        customerRepository.updateBalance(customer);
        
        return transaction;
    }
    
    /**
     * Transfer money from one customer to another
     * @param sender the customer sending money
     * @param receiver the customer receiving money
     * @param amount the amount to transfer
     * @return the Transaction object if successful, null otherwise
     */
    public Transaction processTransfer(Customer sender, Customer receiver, double amount) {
        if (sender.getId().equals(receiver.getId())) {
            System.out.println("Cannot transfer to the same account");
            return null;
        }
        
        if (!sender.withdraw(amount)) {
            return null;
        }
        
        if (!receiver.deposit(amount)) {
            // Rollback
            sender.deposit(amount);
            System.out.println("Transfer failed: unable to deposit to receiver");
            return null;
        }
        
        Transaction transaction = createTransaction(
            receiver.getId(), sender.getId(), amount, "TRANSFER"
        );
        
        transactionRepository.save(transaction);
        customerRepository.updateBalance(sender);
        customerRepository.updateBalance(receiver);
        
        return transaction;
    }
    
    /**
     * Get all transaction history
     * @return list of all transactions
     */
    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }
    
    /**
     * Get transaction history for a specific customer
     * @param customerId the customer ID
     * @return list of transactions involving this customer
     */
    public List<Transaction> getCustomerTransactions(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }
    
    /**
     * Search transactions by ID or user ID
     * @param searchTerm the search term
     * @return list of matching transactions
     */
    public List<Transaction> searchTransactions(String searchTerm) {
        return transactionRepository.searchByIdOrUserId(searchTerm);
    }
    
    private Transaction createTransaction(String receiverId, String senderId, double amount, String type) {
        return new Transaction(
            generateTransactionId(),
            receiverId,
            senderId,
            amount,
            LocalDateTime.now().format(FORMATTER),
            type
        );
    }
    
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
