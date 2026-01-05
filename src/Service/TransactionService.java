package Service;

import Object.Customer;
import Object.Transaction;
import Database.CustomerInter;
import Database.TransactionInter;
import java.time.LocalDateTime;
import java.util.UUID;
import java.time.format.DateTimeFormatter;



public class TransactionService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private CustomerInter customerDB;
    private TransactionInter transactionDB;
    
    public TransactionService(CustomerInter customerDB, TransactionInter transactionDB) {
        this.customerDB = customerDB;
        this.transactionDB = transactionDB;
    }
    
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
        Transaction transaction = new Transaction(
            generateTransactionId(),
            receiver.getID(),
            sender.getID(),
            amount,
            "TRANSFER",
            LocalDateTime.now().format(FORMATTER)
            
        );
        
        transactionDB.saveTransaction(transaction);
        customerDB.updateBalance(sender);
        customerDB.updateBalance(receiver);
        
        return transaction;
    }


    public Transaction DepositService(Customer customer, double amount) {
        if (!customer.deposit(amount)) {
            return null;
        }
        Transaction transaction = createTransaction(customer.getAccNo(), customer.getAccNo(), amount, "DEPOSIT");
        customerDB.updateBalance(customer);
        transactionDB.saveTransaction(transaction);
        return transaction;
    }


    public Transaction WithdrawService(Customer customer, double amount) {
        if (!customer.withdraw(amount)) {
            return null;
        }
        Transaction transaction = createTransaction(customer.getAccNo(), customer.getAccNo(), amount, "WITHDRAW");
        customerDB.updateBalance(customer);
        transactionDB.saveTransaction(transaction);
        return transaction;
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
    String id;
    do {
        id = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    } while (transactionDB.exists(id)); 
    return id;
}   
}
