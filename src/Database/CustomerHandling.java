package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Config.DatabaseConfig;
import Object.Customer;

public class CustomerHandling {
    public String getPasswordByAccNo(String accNo){
        String sql = "SELECT password FROM users WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Customer getCustomerByAccNo(String accNo){
        String sql = "SELECT * FROM users WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateBalance(Customer customer){
        String sql = "UPDATE users SET balance = ? WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, customer.getBalance());
            pstmt.setString(2, customer.getAccNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
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

    public boolean existsid(String id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking transaction existence: " + e.getMessage());
        }
        return false;
    }

    public boolean existsAccNo(String accNo) {
        String sql = "SELECT COUNT(*) FROM users WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking transaction existence: " + e.getMessage());
        }
        return false;
    }

    public boolean IsActive(String accNo){
        String sql = "SELECT Active FROM users WHERE accNo = ?";
    try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
         PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, accNo);
            ResultSet rs = pstmt.executeQuery();
            boolean Active = rs.getBoolean("Active");
            return Active;
         } catch (Exception e){     
            System.out.println("Error checking Status: " + e.getMessage());
         }
         return false;
    }
    
    public void DeactivateCustomer(String accNo){
        String sql = "UPDATE users SET Active = ? WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setString(2, accNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCustomer(Customer customer){
        String sql = "UPDATE users SET PhoneNumber = ?, address = ? WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getPhoneNumber());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getAccNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePassword(String accNo, String newPassword){
        String sql = "UPDATE users SET password = ? WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, accNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
