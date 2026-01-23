package Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Config.DatabaseConfig;

/**
 * Repository for admin credentials
 */
public class AdminRepository implements IAdminRepository {

    @Override
    public String getPasswordByUsername(String username) {
        String sql = "SELECT password FROM admins WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}