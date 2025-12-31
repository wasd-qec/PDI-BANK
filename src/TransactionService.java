import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class for handling banking transactions
 * Manages withdrawals, deposits, and transfers between customers
 * Persists all transactions and balance updates to SQLite database
 */
public class TransactionService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String DB_URL = "jdbc:sqlite:d:/School/PROJECT/TestingDB/mydatabase.db";
    private static final Read dbHelper = new Read();
    
    /**
     * Process a withdrawal transaction
     * @param customer the customer withdrawing money
     * @param amount the amount to withdraw
     * @return the Transaction object if successful, null otherwise
     */
    public static Transaction processWithdrawal(Customer customer, double amount) {
        if (customer.withdraw(amount)) {
            String transactionId = generateTransactionId();
            String timestamp = LocalDateTime.now().format(formatter);
            // Save to database
            dbHelper.InsertTransaction(transactionId, customer.getId(), customer.getId(), 
                                      amount, "WITHDRAWAL", timestamp);
            updateCustomerBalance(customer);
            
            Transaction transaction = new Transaction(
                transactionId,
                customer.getId(),
                customer.getId(),
                amount,
                timestamp,
                "WITHDRAWAL"
            );
            
            System.out.println("Withdrawal successful: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", customer.getBalance()));
            return transaction;
        }
        return null;
    }
    
    /**
     * Process a deposit transaction
     * @param customer the customer depositing money
     * @param amount the amount to deposit
     * @return the Transaction object if successful, null otherwise
     */
    public static Transaction processDeposit(Customer customer, double amount) {
        if (customer.deposit(amount)) {
            String transactionId = generateTransactionId();
            String timestamp = LocalDateTime.now().format(formatter);
            // Save to database
            dbHelper.InsertTransaction(transactionId, customer.getId(), customer.getId(), 
                                      amount, "DEPOSIT", timestamp);
            
            Transaction transaction = new Transaction(
                transactionId,
                customer.getId(),
                customer.getId(),
                amount,
                timestamp,
                "DEPOSIT"
            );
            
            System.out.println("Deposit successful: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", customer.getBalance()));
            return transaction;
        }
        return null;
    }
    
    /**
     * Transfer money from one customer to another
     * @param sender the customer sending money
     * @param receiver the customer receiving money
     * @param amount the amount to transfer
     * @return the Transaction object if successful, null otherwise
     */
    public static Transaction processTransfer(Customer sender, Customer receiver, double amount) {
        if (sender.getId().equals(receiver.getId())) {
            System.out.println("Cannot transfer to the same account");
            return null;
        }
        
        // Withdraw from sender
        if (sender.withdraw(amount)) {
            // Deposit to receiver
            if (receiver.deposit(amount)) {
                String transactionId = generateTransactionId();
                String timestamp = LocalDateTime.now().format(formatter);
                
                // Save to database
                dbHelper.InsertTransaction(transactionId, receiver.getId(), sender.getId(), 
                                          amount, "TRANSFER", timestamp);
                updateCustomerBalance(sender);
                updateCustomerBalance(receiver);
                
                Transaction transaction = new Transaction(
                    transactionId,
                    receiver.getId(),
                    sender.getId(),
                    amount,
                    timestamp,
                    "TRANSFER"
                );
                
                System.out.println("Transfer successful: $" + String.format("%.2f", amount));
                System.out.println(sender.getName() + " -> " + receiver.getName());
                System.out.println("Sender balance: $" + String.format("%.2f", sender.getBalance()));
                System.out.println("Receiver balance: $" + String.format("%.2f", receiver.getBalance()));
                return transaction;
            } else {
                // Rollback if deposit fails
                sender.deposit(amount);
                System.out.println("Transfer failed: unable to deposit to receiver");
                return null;
            }
        }
        return null;
    }
    
    /**
     * Generate a unique transaction ID
     * @return a unique transaction ID string
     */
    private static String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Get all transaction history from database
     * @return list of all transactions
     */
    public static List<Transaction> getTransactionHistory() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger ORDER BY Timestamp DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction t = new Transaction(
                    rs.getString("TransactionId"),
                    rs.getString("ReceiverId"),
                    rs.getString("SenderId"),
                    rs.getDouble("Amount"),
                    rs.getString("Timestamp"),
                    rs.getString("Type")
                );
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Get transaction history for a specific customer from database
     * @param customerId the customer ID
     * @return list of transactions involving this customer
     */
    public static List<Transaction> getCustomerTransactions(String customerId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE SenderId = ? OR ReceiverId = ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction t = new Transaction(
                    rs.getString("TransactionId"),
                    rs.getString("ReceiverId"),
                    rs.getString("SenderId"),
                    rs.getDouble("Amount"),
                    rs.getString("Timestamp"),
                    rs.getString("Type")
                );
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customer transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Update customer balance in the database
     * @param customer the customer whose balance needs to be updated
     */
    private static void updateCustomerBalance(Customer customer) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, customer.getBalance());
            pstmt.setString(2, customer.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error updating balance in database: " + e.getMessage());
        }
    }
}
