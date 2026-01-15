package Search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
