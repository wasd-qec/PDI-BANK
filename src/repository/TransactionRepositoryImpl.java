package repository;

import config.DatabaseConfig;
import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of TransactionRepository
 * Single Responsibility: Only handles Transaction database operations
 */
public class TransactionRepositoryImpl implements TransactionRepository {
    
    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO burger (TransactionId, ReceiverId, SenderId, Amount, Type, Timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transaction.getTransactionId());
            pstmt.setString(2, transaction.getReceiverId());
            pstmt.setString(3, transaction.getSenderId());
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
    
    @Override
    public List<Transaction> findByCustomerId(String customerId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE SenderId = ? OR ReceiverId = ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customer transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    @Override
    public List<Transaction> searchByIdOrUserId(String searchTerm) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE TransactionId LIKE ? OR SenderId LIKE ? OR ReceiverId LIKE ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searching transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    @Override
    public Transaction findById(String transactionId) {
        String sql = "SELECT * FROM burger WHERE TransactionId = ?";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error finding transaction: " + e.getMessage());
        }
        return null;
    }
    
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getString("TransactionId"),
            rs.getString("ReceiverId"),
            rs.getString("SenderId"),
            rs.getDouble("Amount"),
            rs.getString("Timestamp"),
            rs.getString("Type")
        );
    }
}
