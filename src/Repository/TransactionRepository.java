package Repository;

import Object.Customer;
import Object.Transaction;
import Config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Single Responsibility: Handles ONLY Transaction data access
 */
public class TransactionRepository implements ITransactionRepository {
    
    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO burger(TransactionID, ReceiverID, SenderID, Amount, Type, Timestamp) " +
                    "VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getTransactionID());
            pstmt.setString(2, transaction.getReceiverID());
            pstmt.setString(3, transaction.getSenderID());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setString(5, transaction.getType());
            pstmt.setString(6, transaction.getTimestamp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
    
    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger ORDER BY Timestamp DESC";
        try (Connection conn = getConnection();
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
    
    @Override
    public List<Transaction> findByCustomer(Customer customer) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE ReceiverID = ? OR SenderID = ? ORDER BY Timestamp DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getID());
            pstmt.setString(2, customer.getID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transactions for customer: " + e.getMessage());
        }
        return transactions;
    }
    
    @Override
    public Optional<Transaction> findById(String transactionId) {
        String sql = "SELECT * FROM burger WHERE TransactionID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding transaction: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public boolean exists(String transactionId) {
        String sql = "SELECT COUNT(*) FROM burger WHERE TransactionID = ?";
        try (Connection conn = getConnection();
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
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.getDbUrl());
    }
    
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getString("TransactionID"),
            rs.getString("ReceiverID"),
            rs.getString("SenderID"),
            rs.getDouble("Amount"),
            rs.getString("Type"),
            rs.getString("Timestamp")
        );
    }
}
