package Database;
import Object.Transaction;
import Config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import Object.Customer;


public class TransactionImplement implements TransactionInterface{
    
    public void saveTransaction(Transaction transaction) {
      String sql = "INSERT INTO burger(TransactionID, ReceiverID, SenderID, Amount, Type, Timestamp)" +
                    "VALUES(?,?,?,?,?,?)";
      try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getTransactionID());  
            pstmt.setString(2, transaction.getReceiverID());  
            pstmt.setString(3, transaction.getSenderID());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setString(5, transaction.getType());
            pstmt.setString(6, transaction.getTimestamp());
            pstmt.executeUpdate();
    }
    catch (Exception e) {
        System.out.println(e.getMessage());
}
}
    public List<Transaction> ShowAllTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger ORDER BY Timestamp DESC";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
        return transactions;
    }
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getString("TransactionId"),
            rs.getString("ReceiverId"),
            rs.getString("SenderId"),
            rs.getDouble("Amount"),
            rs.getString("Type"),
            rs.getString("Timestamp")
        );
    }
    public List<Transaction> GetTransactionByCustomer(Customer customer){
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE ReceiverID = ? OR SenderID = ? ORDER BY TimeStamp DESC";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getID());
            pstmt.setString(2, customer.getID());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
            transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
    }
    catch (SQLException e) {
        System.out.println("Error retrieving transactions for customer: " + e.getMessage());
    }
    return null;
    }
    public boolean exists(String transactionId) {
        String sql = "SELECT COUNT(*) FROM burger WHERE TransactionID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking transaction existence: " + e.getMessage());
        }
        return false;
    }

}