package repository;

import config.DatabaseConfig;
import security.PasswordEncryption;
import java.sql.*;

/**
 * SQLite implementation of AdminRepository
 * Single Responsibility: Only handles Admin database operations
 */
public class AdminRepositoryImpl implements AdminRepository {
    
    private final PasswordEncryption passwordEncryption;
    
    public AdminRepositoryImpl(PasswordEncryption passwordEncryption) {
        this.passwordEncryption = passwordEncryption;
    }
    
    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS admins (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "name TEXT," +
                    "created_date TEXT)";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating admin table: " + e.getMessage());
        }
    }
    
    @Override
    public void insertDefaultAdmin() {
        String checkSql = "SELECT COUNT(*) FROM admins";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO admins (username, password, name, created_date) VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    String hashedPassword = passwordEncryption.encryptPassword("admin123");
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, hashedPassword);
                    pstmt.setString(3, "System Administrator");
                    pstmt.setString(4, java.time.LocalDate.now().toString());
                    pstmt.executeUpdate();
                    System.out.println("Default admin created: username=admin, password=admin123");
                }
            }
        } catch (Exception e) {
            System.out.println("Error inserting default admin: " + e.getMessage());
        }
    }
    
    @Override
    public boolean verifyLogin(String username, String password) {
        String sql = "SELECT password FROM admins WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return passwordEncryption.verifyPassword(password, storedPassword);
            }
        } catch (SQLException e) {
            System.out.println("Error verifying admin login: " + e.getMessage());
        }
        return false;
    }
}
