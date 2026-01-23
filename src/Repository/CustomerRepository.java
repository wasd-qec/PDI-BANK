package Repository;

import Object.Customer;
import Config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Single Responsibility: Handles ONLY Customer CRUD and existence checks
 * Implements Repository interfaces for Customer
 */
public class CustomerRepository implements ICustomerRepository, ICustomerStatusRepository, ICustomerUpdateRepository {
    
    // ==================== ICustomerRepository ====================
    
    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO users(accNo, id, name, password, balance, PhoneNumber, address, BirthDate, CreateDate, Active) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getAccNo());
            pstmt.setString(2, customer.getID());
            pstmt.setString(3, customer.getName());
            pstmt.setString(4, customer.getPassword());
            pstmt.setDouble(5, customer.getBalance());
            pstmt.setInt(6, customer.getPhoneNumber());
            pstmt.setString(7, customer.getAddress());
            pstmt.setString(8, customer.getBirthDate());
            pstmt.setString(9, customer.getCreateDate());
            pstmt.setBoolean(10, customer.isActive());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(String customerId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
    
    @Override
    public Optional<Customer> findByAccNo(String accNo) {
        String sql = "SELECT * FROM users WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding customer by accNo: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<Customer> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding customer by id: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY name";
        try (Connection conn = getConnection();
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
    public boolean existsById(String id) {
        return checkExists("SELECT COUNT(*) FROM users WHERE id = ?", id);
    }
    
    @Override
    public boolean existsByAccNo(String accNo) {
        return checkExists("SELECT COUNT(*) FROM users WHERE accNo = ?", accNo);
    }
    
    // ==================== ICustomerStatusRepository ====================
    
    @Override
    public void activate(String accNo) {
        updateActiveStatus(accNo, true);
    }
    
    @Override
    public void deactivate(String accNo) {
        updateActiveStatus(accNo, false);
    }
    
    @Override
    public boolean isActive(String accNo) {
        String sql = "SELECT Active FROM users WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("Active");
            }
        } catch (SQLException e) {
            System.out.println("Error checking status: " + e.getMessage());
        }
        return false;
    }
    
    // ==================== ICustomerUpdateRepository ====================
    
    @Override
    public void updateBalance(Customer customer) {
        String sql = "UPDATE users SET balance = ? WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, customer.getBalance());
            pstmt.setString(2, customer.getAccNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }
    
    @Override
    public void updateBasicInfo(Customer customer) {
        String sql = "UPDATE users SET PhoneNumber = ?, address = ? WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getPhoneNumber());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getAccNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating basic info: " + e.getMessage());
        }
    }
    
    @Override
    public void updateFullProfile(Customer customer) {
        String sql = "UPDATE users SET name = ?, PhoneNumber = ?, address = ?, id = ?, BirthDate = ? WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setInt(2, customer.getPhoneNumber());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getID());
            pstmt.setString(5, customer.getBirthDate());
            pstmt.setString(6, customer.getAccNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }
    
    @Override
    public void updatePassword(String accNo, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, accNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }
    
    // ==================== Legacy Compatibility Methods ====================
    
    /**
     * Legacy method: Updates full profile (alias for updateFullProfile)
     */
    public void update(Customer customer) {
        updateFullProfile(customer);
    }
    
    /**
     * Legacy method: Updates basic info only (phone, address)
     */
    public void updateInfo(Customer customer) {
        updateBasicInfo(customer);
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Gets password by account number (for authentication)
     */
    public String getPasswordByAccNo(String accNo) {
        String sql = "SELECT password FROM users WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Error getting password: " + e.getMessage());
        }
        return null;
    }
    
    private void updateActiveStatus(String accNo, boolean active) {
        String sql = "UPDATE users SET Active = ? WHERE accNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, active);
            pstmt.setString(2, accNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating active status: " + e.getMessage());
        }
    }
    
    private boolean checkExists(String sql, String param) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, param);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking existence: " + e.getMessage());
        }
        return false;
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.getDbUrl());
    }
    
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getString("name"),
            rs.getString("password"),
            rs.getString("id"),
            rs.getString("accNo"),
            rs.getDouble("balance"),
            rs.getInt("PhoneNumber"),
            rs.getString("address"),
            rs.getString("BirthDate"),
            rs.getString("CreateDate"),
            rs.getBoolean("Active")
        );
    }
}
