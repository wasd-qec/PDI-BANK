package Search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Config.DatabaseConfig;
import Object.Transaction;

public class SearchTransaction {
    
    public Transaction findById(String transactionId) {
        String sql = "SELECT * FROM burger WHERE TransactionID = ?";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error finding transaction: " + e.getMessage());
        }
        return null;
    }

    public List<Transaction> filterByAmountRange(double minAmount, double maxAmount) {
        String sql = "SELECT * FROM burger WHERE Amount BETWEEN ? AND ? ORDER BY Timestamp DESC";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, minAmount);
            stmt.setDouble(2, maxAmount);
            return executeAndMapResults(stmt);
        } catch (SQLException e) {
            System.out.println("Error filtering transactions by amount: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Transaction> filterBySender(String senderId) {
        String sql = "SELECT * FROM burger WHERE SenderID = ? ORDER BY Timestamp DESC";
        return executeQuery(sql, senderId);
    }

    private List<Transaction> executeQuery(String sql, String param) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, param);
            return executeAndMapResults(pstmt);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return transactions;
    }

    private List<Transaction> executeAndMapResults(PreparedStatement stmt) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        return transactions;
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
