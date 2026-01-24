package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Config.DatabaseConfig;

public class ReportForAdmin implements ReportInterfaceAdmin {
    
    public double getTotalDeposit(String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'DEPOSIT' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startTimestamp);
            pstmt.setString(2, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total deposits (range): " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalWithdrawal(String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'WITHDRAWAL' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startTimestamp);
            pstmt.setString(2, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total withdrawals (range): " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalTransfer(String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'TRANSFER' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startTimestamp);
            pstmt.setString(2, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total transfers (range): " + e.getMessage());
        }
        return 0.0;
    }

    public int getTotalTransactions(String startTimestamp, String endTimestamp) {
        String sql = "SELECT COUNT(*) AS total FROM burger WHERE Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startTimestamp);
            pstmt.setString(2, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting total transactions: " + e.getMessage());
        }
        return 0;
    }

    public double getTotalBalance() {
        String sql = "SELECT COALESCE(SUM(balance), 0) AS total FROM users";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total balance: " + e.getMessage());
        }
        return 0.0;
    }

    public int getTotalTransactions() {
        String sql = "SELECT COUNT(*) AS total FROM burger";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting total transactions: " + e.getMessage());
        }
        return 0;
    }

    public double getTotalDeposit() {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'DEPOSIT'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total deposits: " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalWithdrawal() {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'WITHDRAWAL'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total withdrawals: " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalTransfer() {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'TRANSFER'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();   
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total transfers: " + e.getMessage());
        }
        return 0.0;
    }

    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) AS total FROM users";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting total users: " + e.getMessage());
        }
        return 0;
    }

    public int getActiveUsers() {
        String sql = "SELECT COUNT(*) AS total FROM users WHERE Active = 1";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting active users: " + e.getMessage());
        }
        return 0;
    }

    public int getDeactivatedUsers() {
        String sql = "SELECT COUNT(*) AS total FROM users WHERE Active = 0";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting deactivated users: " + e.getMessage());
        }
        return 0;
    }

    public void printSummaryReport() {
        System.out.println("========== BANK SUMMARY REPORT ==========");
        System.out.println("Total Users:        " + getTotalUsers());
        System.out.println("Active Users:       " + getActiveUsers());
        System.out.println("Deactivated Users:  " + getDeactivatedUsers());
        System.out.println("------------------------------------------");
        System.out.println("Total Deposits:     $" + String.format("%.2f", getTotalDeposit()));
        System.out.println("Total Withdrawals:  $" + String.format("%.2f", getTotalWithdrawal()));
        System.out.println("Total Transfers:    $" + String.format("%.2f", getTotalTransfer()));
        System.out.println("------------------------------------------");
        System.out.println("Total Transactions: " + getTotalTransactions());
        System.out.println("Total Balance:      $" + String.format("%.2f", getTotalBalance()));
        System.out.println("==========================================");
    }
}
