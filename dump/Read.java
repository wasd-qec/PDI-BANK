import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Read {
    
    private static final String DB_URL = "jdbc:sqlite:mydatabase.db";
    
    public static void main(String[] args) {
        // Create database and table
        createTable();
        
        // Insert some data
        insertData("John Doe", 30, "john@example.com");
        insertData("Jane Smith", 25, "jane@example.com");
        insertData("Bob Johnson", 35, "bob@example.com");
        
        System.out.println("Data inserted successfully!\n");
        
        // Read all data
        readAllData();
        
        // Read specific user
        System.out.println("\nSearching for users aged 30 or older:");
        readDataByAge(30);
    }
    
    private static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "age INTEGER," +
                    "email TEXT)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Table created successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    private static void insertData(String name, int age, String email) {
        String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
    
    private static void readAllData() {
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("All users in database:");
            System.out.println("ID\tName\t\tAge\tEmail");
            System.out.println("--------------------------------------------------");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                
                System.out.printf("%d\t%s\t%d\t%s\n", id, name, age, email);
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }
    
    private static void readDataByAge(int minAge) {
        String sql = "SELECT * FROM users WHERE age >= ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, minAge);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                
                System.out.printf("ID: %d, Name: %s, Age: %d, Email: %s\n", 
                                  id, name, age, email);
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }
}
