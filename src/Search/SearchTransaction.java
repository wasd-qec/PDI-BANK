package Search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public List<Transaction> filterByAmountRange(double minAmount, double maxAmount) throws SQLException {
        String sql = "SELECT * FROM burger WHERE Amount BETWEEN ? AND ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, minAmount);
            stmt.setDouble(2, maxAmount);
            return executeAndMapResults(stmt);
        }
    }
    public List<Transaction> filterBySender(String senderId) throws SQLException {
        String sql = "SELECT * FROM burger WHERE SenderId = ?";
        return executeQuery(sql, senderId);
    }
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getString("TransactionID"),
            rs.getString("ReceiverID"),
            rs.getString("SenderID"),
            rs.getDouble("Amount"),
            rs.getString("Timestamp"),
            rs.getString("Type")
        );
    }
}
