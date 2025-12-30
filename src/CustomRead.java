import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomRead {
    private static final String DB_URL = "jdbc:sqlite:d:/School/PROJECT/TestingDB/mydatabase.db";
     public String getPasswordByAccNo(String AccNo) {
        String sql = "SELECT password FROM users WHERE accNo = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, AccNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String Password = rs.getString("password");
                System.out.println("\nPassword for Account " + AccNo + ": " + Password);
                return Password;
            } else {
                System.out.println("No user found with Account Number: " + AccNo);
                return null;
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
            return null;
        }
    }

    public Customer InitializedCus(String AccNo) {
        String sql = "SELECT * FROM users WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, AccNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String Id = rs.getString("id");
                String Name = rs.getString("name");
                double Balance = rs.getDouble("balance");
                long PhoneNumber = rs.getLong("PhoneNumber");
                String RetrievedAccNo = rs.getString("accNo");
                String Address = rs.getString("address");
                String BirthDate = rs.getString("BirthDate");
                String CreateDate = rs.getString("CreateDate");
                boolean Active = rs.getBoolean("Active");
                String hashedPassword = rs.getString("password");

                Customer customer = new Customer(RetrievedAccNo, Id, Name, hashedPassword, Balance, PhoneNumber, Address, BirthDate, CreateDate, Active);
                return customer;
            } else {
                System.out.println("No user found with Account Number: " + AccNo);
                return null;
            }
            
        } catch (SQLException e) {
            System.out.println("Error searching user: " + e.getMessage());
            return null;
        }
    }
}
