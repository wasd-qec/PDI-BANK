package Service;

import Object.Customer;
import Object.Transaction;
import Repository.ICustomerUpdateRepository;
import Repository.ITransactionRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Single Responsibility: Handles transaction business logic
 * Dependency Inversion: Depends on abstractions (interfaces), not concrete classes
 */
public class TransactionServiceImpl implements ITransactionService {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final ICustomerUpdateRepository customerUpdateRepository;
    private final ITransactionRepository transactionRepository;
    
    // Dependency Injection via constructor
    public TransactionServiceImpl(ICustomerUpdateRepository customerUpdateRepository,
                                  ITransactionRepository transactionRepository) {
        this.customerUpdateRepository = customerUpdateRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    public Transaction processTransfer(Customer sender, Customer receiver, double amount) {
        if (sender.getID().equals(receiver.getID())) {
            System.out.println("Cannot transfer to the same account");
            return null;
        }
        
        if (!sender.withdraw(amount)) {
            System.out.println("Invalid transfer amount");
            return null;
        }
        
        if (!receiver.deposit(amount)) {
            System.out.println("Invalid transfer amount");
            return null;
        }
        
        Transaction transaction = createTransaction(
            receiver.getID(),
            sender.getID(),
            amount,
            "TRANSFER"
        );
        
        transactionRepository.save(transaction);
        customerUpdateRepository.updateBalance(sender);
        customerUpdateRepository.updateBalance(receiver);
        
        return transaction;
    }
    
    @Override
    public Transaction processDeposit(Customer customer, double amount) {
        if (!customer.deposit(amount)) {
            return null;
        }
        
        Transaction transaction = createTransaction(
            customer.getAccNo(),
            customer.getAccNo(),
            amount,
            "DEPOSIT"
        );
        
        customerUpdateRepository.updateBalance(customer);
        transactionRepository.save(transaction);
        
        return transaction;
    }
    
    @Override
    public Transaction processWithdrawal(Customer customer, double amount) {
        if (!customer.withdraw(amount)) {
            return null;
        }
        
        Transaction transaction = createTransaction(
            customer.getAccNo(),
            customer.getAccNo(),
            amount,
            "WITHDRAW"
        );
        
        customerUpdateRepository.updateBalance(customer);
        transactionRepository.save(transaction);
        
        return transaction;
    }
    
    private Transaction createTransaction(String receiverId, String senderId, double amount, String type) {
        return new Transaction(
            generateTransactionId(type),
            receiverId,
            senderId,
            amount,
            type,
            LocalDateTime.now().format(FORMATTER)
        );
    }
    
    private String generateTransactionId(String type) {
        String prefix;
        switch (type) {
            case "DEPOSIT":
                prefix = "TXN-D";
                break;
            case "WITHDRAW":
                prefix = "TXN-W";
                break;
            case "TRANSFER":
                prefix = "TXN-T";
                break;
            default:
                prefix = "TXN-";
        }
        
        String id;
        do {
            id = prefix + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (transactionRepository.exists(id));
        
        return id;
    }
}
