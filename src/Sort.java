import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sort {
     private static final String DB_URL = "jdbc:sqlite:d:/School/PROJECT/TestingDB/mydatabase.db";
     public void readDataByBalance(double minBalance) {
        String sql = "SELECT * FROM users WHERE balance >= ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, minBalance);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String Id = rs.getString("id");
                String Name = rs.getString("name");
                double Balance = rs.getDouble("balance");
                String AccNo = rs.getString("accNo");
                
                System.out.printf("  • ID: %-8s | Name: %-15s | Balance: $%-10.2f | Account: %s\n", 
                                  Id, Name, Balance, AccNo);
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }
}
