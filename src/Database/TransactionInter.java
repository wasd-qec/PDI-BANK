package Database;
import java.util.List;

import Object.Customer;
import Object.Transaction;

public interface TransactionInter{
    void saveTransaction(Transaction transaction);
    List<Transaction> ShowAllTransaction();
    boolean exists(String transactionId);
    List<Transaction> GetTransactionByCustomer(Customer customer);
    
}