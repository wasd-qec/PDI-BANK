import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Use OR condition to search 
 * ===== AVAILABLE FUNCTIONS =====
 * 2. createTable() - Creates the users table in the database
 * 3. insertData() - Inserts a new user record into the database
 * 4. deleteUser(String userId) - Deletes a user by ID
 * 5. readAllData() - Displays all users in the database
 * 6. readDataByBalance(double minBalance) - Displays users with balance >= minBalance
 * 7. InitializedCus(String AccNo) - Searches for a user by account number and make customer object
 * 8. getPasswordByAccNo(String AccNo) - Retrieves the password for a given account number
 * ================================
 */

public class Read {
    
    private static final String DB_URL = "jdbc:sqlite:d:/School/PROJECT/TestingDB/mydatabase.db";
    
    public static void main(String[] args) {
        Read dbReader = new Read();
        dbReader.readAllData();
    }
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "accNo TEXT PRIMARY KEY," +
                    "id TEXT," +
                    "name TEXT," +
                    "password TEXT," +
                    "balance REAL," +
                    "PhoneNumber INTEGER," +
                    "address TEXT," +
                    "BirthDate TEXT," +
                    "CreateDate TEXT," +
                    "Active BOOLEAN DEFAULT 1)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Table created successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    public void insertData(String Id, String Name, String Password, double Balance, 
                                     long PhoneNumber, String AccNo, String Address, 
                                     String BirthDate, String CreateDate, boolean Active) {
        String sql = "INSERT INTO users (accNo, id, name, password, balance, PhoneNumber, address, BirthDate, CreateDate, Active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, AccNo);
            pstmt.setString(2, Id);
            pstmt.setString(3, Name);
            pstmt.setString(4, Password);
            pstmt.setDouble(5, Balance);
            pstmt.setLong(6, PhoneNumber);
            pstmt.setString(7, Address);
            pstmt.setString(8, BirthDate);
            pstmt.setString(9, CreateDate);
            pstmt.setBoolean(10, Active);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
    
    public void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("User with ID '" + userId + "' deleted successfully!");
            } else {
                System.out.println("No user found with ID '" + userId + "'.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
    
    public void readAllData() {
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("All users in database:\n");
            
            int count = 1;
            while (rs.next()) {
                String Id = rs.getString("id");
                String Name = rs.getString("name");
                double Balance = rs.getDouble("balance");
                long PhoneNumber = rs.getLong("PhoneNumber");
                String AccNo = rs.getString("accNo");
                String Address = rs.getString("address");
                String BirthDate = rs.getString("BirthDate");
                String CreateDate = rs.getString("CreateDate");
                boolean Active = rs.getBoolean("Active");
                
                System.out.println("User #" + count);
                System.out.printf("  ID: %-15s | Name: %-20s | Account: %s\n", Id, Name, AccNo);
                System.out.printf("  Balance: $%-10.2f | Phone: %d\n", Balance, PhoneNumber);
                System.out.printf("  Address: %s\n", Address);
                System.out.printf("  Birth Date: %-12s | Create Date: %-12s | Active: %b\n", BirthDate, CreateDate, Active);
                System.out.println("  " + "-".repeat(80));
                count++;
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }
    
   
}
