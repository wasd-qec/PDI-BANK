package Database;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import Config.DatabaseConfig;
import Object.Customer;
import Object.Transaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class CustomerImple implements CustomerInter {
    
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

    public void save(Customer customer){
        String sql = "INSERT INTO users(accNo, id, name, password, balance, PhoneNumber, address, BirthDate, CreateDate, Active" +
                    " Values (?,?,?,?,?,?,?,?,?,?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setString(1, customer.getAccNo());
                pstmt.setString(2, customer.getID());
                pstmt.setString(3, customer.getName());
                pstmt.setString(4, customer.getPassword());
                pstmt.setDouble(5, customer.getBalance());
                pstmt.setInt(5, customer.getPhoneNumber());
                pstmt.setString(6, customer.getAddress());
                pstmt.setString(7, customer.getBirthDate());
                pstmt.setString(8, customer.getCreateDate());
                pstmt.setBoolean(9, customer.isActive());
                pstmt.executeUpdate();

            }
            catch (Exception e){
                System.out.println ("Error"+e.getMessage());
            }
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
    public void updateBalance(Customer customer){
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, customer.getBalance());
            pstmt.setString(2, customer.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
