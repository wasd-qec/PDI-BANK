package Database;

import Config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Report {

    // Count total number of users
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

    // Count active users
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

    // Count deactivated users
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

    // Get total deposit amount
    public double getTotalDeposit() {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'Deposit'";
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

    // Get total withdrawal amount
    public double getTotalWithdrawal() {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'Withdrawal'";
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

    // Get total transfer amount
    public double getTotalTransfer() {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE Type = 'Transfer'";
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

    // Get total balance across all users
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

    // Get total number of transactions
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

    // Print a summary report
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
