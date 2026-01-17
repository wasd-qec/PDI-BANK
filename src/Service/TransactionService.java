package service;

import object.Customer;
import object.Transaction;
import database.CustomerInter;
import database.TransactionInter;
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
        Transaction transaction = createTransaction(
            receiver.getID(),
            sender.getID(),
            amount,
            "TRANSFER"
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
    } while (transactionDB.exists(id)); 
    return id;
}   
}
