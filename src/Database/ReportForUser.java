package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Config.DatabaseConfig;
import Object.Customer;

public class ReportForUser implements ReportInterface {

    public double getTotalDeposit(Customer customer) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (ReceiverID = ? OR ReceiverID = ?) AND Type = 'Deposit'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getID());
            pstmt.setString(2, customer.getAccNo());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer deposits: " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalWithdrawal(Customer customer) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (SenderID = ? OR SenderID = ?) AND Type = 'Withdrawal'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getID());
            pstmt.setString(2, customer.getAccNo());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer withdrawals: " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalTransferOut(Customer customer) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (SenderID = ? OR SenderID = ?) AND Type = 'Transfer'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getID());
            pstmt.setString(2, customer.getAccNo());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer transfer out: " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalTransferIn(Customer customer) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (ReceiverID = ? OR ReceiverID = ?) AND Type = 'Transfer'";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getID());
            pstmt.setString(2, customer.getAccNo());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer transfer in: " + e.getMessage());
        }
        return 0.0;
    }

    public void printAccountSummary(Customer customer) {
        System.out.println("======== ACCOUNT SUMMARY ========");
        System.out.println("Account Number:   " + customer.getAccNo());
        System.out.println("Account Name:     " + customer.getName());
        System.out.println("---------------------------------");
        System.out.println("Total Deposits:     $" + String.format("%.2f", getCustomerTotalDeposit(customer)));
        System.out.println("Total Withdrawals:  $" + String.format("%.2f", getCustomerTotalWithdrawal(customer)));
        System.out.println("Transfer In:        $" + String.format("%.2f", getCustomerTotalTransferIn(customer)));
        System.out.println("Transfer Out:       $" + String.format("%.2f", getCustomerTotalTransferOut(customer)));
        System.out.println("---------------------------------");
        System.out.println("Current Balance:    $" + String.format("%.2f", customer.getBalance()));
        System.out.println("=================================");
    }

    public double getTotalDeposit(Customer customer, String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (ReceiverID = ?) AND Type = 'DEPOSIT' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getAccNo());
            pstmt.setString(2, startTimestamp);
            pstmt.setString(3, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer deposits (range): " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalWithdrawal(Customer customer, String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (SenderID = ?) AND Type = 'WITHDRAWAL' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getAccNo());
            pstmt.setString(2, startTimestamp);
            pstmt.setString(3, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer withdrawals (range): " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalTransferOut(Customer customer, String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (SenderID = ?) AND Type = 'TRANSFER' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getAccNo());
            pstmt.setString(2, startTimestamp);
            pstmt.setString(3, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer transfer out (range): " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalTransferIn(Customer customer, String startTimestamp, String endTimestamp) {
        String sql = "SELECT COALESCE(SUM(Amount), 0) AS total FROM burger WHERE (ReceiverID = ?) AND Type = 'TRANSFER' AND Timestamp BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getAccNo());
            pstmt.setString(2, startTimestamp);
            pstmt.setString(3, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating customer transfer in (range): " + e.getMessage());
        }
        return 0.0;
    }

    public double getTotalBalance() {
        // Not applicable for ReportForUser
        return 0.0;
    }
    public int getTotalUsers() {
        // Not applicable for ReportForUser
        return 0;
    }
    public int getActiveUsers() {
        // Not applicable for ReportForUser
        return 0;
    }
    public int getDeactivatedUsers() {
        // Not applicable for ReportForUser
        return 0;
    }

}

