package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.DatabaseConfig;
import Object.Customer;
import Object.CustomerSearchCriteria;

public class SearchCustomer {	public Customer findByAccNo(String accNo) {
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

	public List<Customer> filter(CustomerSearchCriteria criteria) {
		List<Customer> customers = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE 1=1");
		List<Object> params = new ArrayList<>();

		if (criteria.getAccNo() != null && !criteria.getAccNo().isEmpty()) {
			sql.append(" AND accNo = ?");
			params.add(criteria.getAccNo());
		}
		if (criteria.getId() != null && !criteria.getId().isEmpty()) {
			sql.append(" AND id = ?");
			params.add(criteria.getId());
		}
		if (criteria.getName() != null && !criteria.getName().isEmpty()) {
			sql.append(" AND name = ?");
			params.add(criteria.getName());
		}
		if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isEmpty()) {
			sql.append(" AND PhoneNumber = ?");
			params.add(Integer.parseInt(criteria.getPhoneNumber()));
		}

		if (criteria.getNameFilter() != null && !criteria.getNameFilter().isEmpty()) {
			sql.append(" AND name LIKE ?");
			params.add("%" + criteria.getNameFilter() + "%");
		}
		if (criteria.getAddressFilter() != null && !criteria.getAddressFilter().isEmpty()) {
			sql.append(" AND address LIKE ?");
			params.add("%" + criteria.getAddressFilter() + "%");
		}
		if (criteria.getMinBalance() != null) {
			sql.append(" AND balance >= ?");
			params.add(criteria.getMinBalance());
		}
		if (criteria.getMaxBalance() != null) {
			sql.append(" AND balance <= ?");
			params.add(criteria.getMaxBalance());
		}
		if (criteria.getActive() != null) {
			sql.append(" AND Active = ?");
			params.add(criteria.getActive());
		}
		if (criteria.getCreateDateFrom() != null && !criteria.getCreateDateFrom().isEmpty()) {
			sql.append(" AND CreateDate >= ?");
			params.add(criteria.getCreateDateFrom());
		}
		if (criteria.getCreateDateTo() != null && !criteria.getCreateDateTo().isEmpty()) {
			sql.append(" AND CreateDate <= ?");
			params.add(criteria.getCreateDateTo());
		}

		sql.append(" ORDER BY name");

		try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
			 PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				customers.add(mapResultSetToCustomer(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error filtering customers: " + e.getMessage());
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
