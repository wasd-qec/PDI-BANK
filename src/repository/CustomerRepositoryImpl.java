package repository;

import config.DatabaseConfig;
import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of CustomerRepository
 * Single Responsibility: Only handles Customer database operations
 */
public class CustomerRepositoryImpl implements CustomerRepository {
    
    @Override
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
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    @Override
    public Customer findByAccNo(String accNo) {
        String sql = "SELECT * FROM users WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error finding customer: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Customer findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error finding customer: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public String getPasswordByAccNo(String accNo) {
        String sql = "SELECT password FROM users WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY name";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customers: " + e.getMessage());
        }
        return customers;
    }
    
    @Override
    public List<Customer> searchByNameOrAccNo(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name LIKE ? OR accNo LIKE ? ORDER BY name";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searching customers: " + e.getMessage());
        }
        return customers;
    }
    
    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO users (accNo, id, name, password, balance, PhoneNumber, address, BirthDate, CreateDate, Active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getAccNo());
            pstmt.setString(2, customer.getId());
            pstmt.setString(3, customer.getName());
            pstmt.setString(4, customer.getPassword());
            pstmt.setDouble(5, customer.getBalance());
            pstmt.setLong(6, customer.getPhoneNumber());
            pstmt.setString(7, customer.getAddress());
            pstmt.setString(8, customer.getBirthDate());
            pstmt.setString(9, customer.getCreateDate());
            pstmt.setBoolean(10, true);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    @Override
    public void updateBalance(Customer customer) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, customer.getBalance());
            pstmt.setString(2, customer.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(String customerId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
    
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getString("accNo"),
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password"),
            rs.getDouble("balance"),
            rs.getLong("PhoneNumber"),
            rs.getString("address"),
            rs.getString("BirthDate"),
            rs.getString("CreateDate"),
            rs.getBoolean("Active")
        );
    }
}
