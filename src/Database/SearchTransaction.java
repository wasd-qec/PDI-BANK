package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.DatabaseConfig;
import Object.Transaction;
import Object.TransactionSearchCriteria;

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
    public List<Transaction> filterByReceiver(String receiverId) {
        String sql = "SELECT * FROM burger WHERE ReceiverID = ? ORDER BY Timestamp DESC";
        return executeQuery(sql, receiverId);
    }

    public List<Transaction> filter(TransactionSearchCriteria criteria) {
        List<Transaction> transactions = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM burger WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (criteria.getTransactionId() != null && !criteria.getTransactionId().isEmpty()) {
            sql.append(" AND TransactionID = ?");
            params.add(criteria.getTransactionId());
        }

        if (criteria.getSenderId() != null && !criteria.getSenderId().isEmpty()) {
            sql.append(" AND SenderID = ?");
            params.add(criteria.getSenderId());
        }
        if (criteria.getReceiverId() != null && !criteria.getReceiverId().isEmpty()) {
            sql.append(" AND ReceiverID = ?");
            params.add(criteria.getReceiverId());
        }
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            sql.append(" AND Type = ?");
            params.add(criteria.getType());
        }
        if (criteria.getMinAmount() != null) {
            sql.append(" AND Amount >= ?");
            params.add(criteria.getMinAmount());
        }
        if (criteria.getMaxAmount() != null) {
            sql.append(" AND Amount <= ?");
            params.add(criteria.getMaxAmount());
        }
        if (criteria.getDateFrom() != null && !criteria.getDateFrom().isEmpty()) {
            sql.append(" AND Timestamp >= ?");
            params.add(criteria.getDateFrom());
        }
        if (criteria.getDateTo() != null && !criteria.getDateTo().isEmpty()) {
            sql.append(" AND Timestamp <= ?");
            params.add(criteria.getDateTo());
        }

        sql.append(" ORDER BY Timestamp DESC");

        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            return executeAndMapResults(pstmt);
        } catch (SQLException e) {
            System.out.println("Error filtering transactions: " + e.getMessage());
        }
        return transactions;
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
