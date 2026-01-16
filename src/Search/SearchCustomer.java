package Search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Config.DatabaseConfig;
import Object.Customer;

public class SearchCustomer {
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
			System.out.println("Error finding customer by accNo: " + e.getMessage());
		}
		return null;
	}

	public List<Customer> findByName(String namePattern) {
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE name LIKE ? ORDER BY name";
		try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + namePattern + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				customers.add(mapResultSetToCustomer(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error searching customers by name: " + e.getMessage());
		}
		return customers;
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
