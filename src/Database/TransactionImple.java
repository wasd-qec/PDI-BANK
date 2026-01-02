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


public class TransactionImple implements TransactionInter{
    
    public void saveTransaction(Transaction transaction) {
      String sql = "INSERT INTO burger(TransactionID, ReciverID, SenderID, Amount, Type, Timestamp)" +
                    "VALUES(?,?,?,?,?,?)";
      try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getTransactionID());  
            pstmt.setString(2, transaction.getReciverID());  
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
            rs.getString("Timestamp"),
            rs.getString("Type")
        );
    }

}