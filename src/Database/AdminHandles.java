package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import Config.DatabaseConfig;
import Object.Customer;

public class AdminHandles extends CustomerHandling {
    public void save(Customer customer){
        String sql = "INSERT INTO users(accNo, id, name, password, balance, PhoneNumber, address, BirthDate, CreateDate, Active) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
            PreparedStatement pstmt = conn.prepareStatement(sql)){

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
            }
            catch (Exception e){
                System.out.println ("Error"+e.getMessage());
            }
    }
    public List<Customer> getAllCustomers(){
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
    public void delete(String customerId){
      String sql = "DELETE FROM users WHERE id = ?";
      try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, customerId);
          pstmt.executeUpdate();
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }  
    }
    public void ActivateCustomer(String accNo){
        String sql = "UPDATE users SET Active = ? WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setString(2, accNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void updateCustomer(Customer customer){
        String sql = "UPDATE users SET name = ?, PhoneNumber = ?, address = ?, id = ?,BirthDate = ? WHERE accNo = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setInt(2, customer.getPhoneNumber());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getID());
            pstmt.setString(5, customer.getBirthDate());
            pstmt.setString(6, customer.getAccNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
}
