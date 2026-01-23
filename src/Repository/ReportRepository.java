package Repository;

import Object.Customer;
import Config.DatabaseConfig;
import java.sql.*;

/**
 * Single Responsibility: Handles ONLY report/statistics data access
 */
public class ReportRepository implements IReportRepository {
    
    // ==================== User Statistics ====================
    
    @Override
    public int getTotalUsers() {
        return countQuery("SELECT COUNT(*) FROM users");
    }
    
    @Override
    public int getActiveUsers() {
        return countQuery("SELECT COUNT(*) FROM users WHERE Active = 1");
    }
    
    @Override
    public int getDeactivatedUsers() {
        return countQuery("SELECT COUNT(*) FROM users WHERE Active = 0");
    }
    
    @Override
    public double getTotalBalance() {
        return sumQuery("SELECT COALESCE(SUM(balance), 0) FROM users");
    }
    
    // ==================== Transaction Statistics (All Time) ====================
    
    @Override
    public int getTotalTransactions() {
        return countQuery("SELECT COUNT(*) FROM burger");
    }
    
    @Override
    public double getTotalDeposit() {
        return sumQuery("SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE Type = 'Deposit'");
    }
    
    @Override
    public double getTotalWithdrawal() {
        return sumQuery("SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE Type = 'Withdrawal'");
    }
    
    @Override
    public double getTotalTransfer() {
        return sumQuery("SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE Type = 'Transfer'");
    }
    
    // ==================== Transaction Statistics (Date Range) ====================
    
    @Override
    public double getTotalDeposit(String startTimestamp, String endTimestamp) {
        return sumQueryWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE Type = 'DEPOSIT' AND Timestamp BETWEEN ? AND ?",
            startTimestamp, endTimestamp
        );
    }
    
    @Override
    public double getTotalWithdrawal(String startTimestamp, String endTimestamp) {
        return sumQueryWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE Type = 'WITHDRAWAL' AND Timestamp BETWEEN ? AND ?",
            startTimestamp, endTimestamp
        );
    }
    
    @Override
    public double getTotalTransfer(String startTimestamp, String endTimestamp) {
        return sumQueryWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE Type = 'TRANSFER' AND Timestamp BETWEEN ? AND ?",
            startTimestamp, endTimestamp
        );
    }
    
    // ==================== Customer-Specific Statistics ====================
    
    @Override
    public double getCustomerTotalDeposit(Customer customer) {
        return sumQueryForCustomer(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE (ReceiverID = ? OR ReceiverID = ?) AND Type = 'Deposit'",
            customer.getID(), customer.getAccNo()
        );
    }
    
    @Override
    public double getCustomerTotalWithdrawal(Customer customer) {
        return sumQueryForCustomer(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE (SenderID = ? OR SenderID = ?) AND Type = 'Withdrawal'",
            customer.getID(), customer.getAccNo()
        );
    }
    
    @Override
    public double getCustomerTotalTransferIn(Customer customer) {
        return sumQueryForCustomer(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE (ReceiverID = ? OR ReceiverID = ?) AND Type = 'Transfer'",
            customer.getID(), customer.getAccNo()
        );
    }
    
    @Override
    public double getCustomerTotalTransferOut(Customer customer) {
        return sumQueryForCustomer(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE (SenderID = ? OR SenderID = ?) AND Type = 'Transfer'",
            customer.getID(), customer.getAccNo()
        );
    }
    
    // ==================== Customer Statistics (Date Range) ====================
    
    @Override
    public double getCustomerTotalDeposit(Customer customer, String startTimestamp, String endTimestamp) {
        return sumQueryForCustomerWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE ReceiverID = ? AND Type = 'DEPOSIT' AND Timestamp BETWEEN ? AND ?",
            customer.getAccNo(), startTimestamp, endTimestamp
        );
    }
    
    @Override
    public double getCustomerTotalWithdrawal(Customer customer, String startTimestamp, String endTimestamp) {
        return sumQueryForCustomerWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE SenderID = ? AND Type = 'WITHDRAWAL' AND Timestamp BETWEEN ? AND ?",
            customer.getAccNo(), startTimestamp, endTimestamp
        );
    }
    
    @Override
    public double getCustomerTotalTransferIn(Customer customer, String startTimestamp, String endTimestamp) {
        return sumQueryForCustomerWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE ReceiverID = ? AND Type = 'TRANSFER' AND Timestamp BETWEEN ? AND ?",
            customer.getAccNo(), startTimestamp, endTimestamp
        );
    }
    
    @Override
    public double getCustomerTotalTransferOut(Customer customer, String startTimestamp, String endTimestamp) {
        return sumQueryForCustomerWithDateRange(
            "SELECT COALESCE(SUM(Amount), 0) FROM burger WHERE SenderID = ? AND Type = 'TRANSFER' AND Timestamp BETWEEN ? AND ?",
            customer.getAccNo(), startTimestamp, endTimestamp
        );
    }
    
    // ==================== Print Methods (Legacy Support) ====================
    
    /**
     * Prints a formatted summary report of the bank's statistics
     */
    public void printSummaryReport() {
        System.out.println("============================================");
        System.out.println("             BANK SUMMARY REPORT            ");
        System.out.println("============================================");
        System.out.println();
        System.out.println("USER STATISTICS:");
        System.out.printf("  Total Users:       %d%n", getTotalUsers());
        System.out.printf("  Active Users:      %d%n", getActiveUsers());
        System.out.printf("  Inactive Users:    %d%n", getDeactivatedUsers());
        System.out.println();
        System.out.println("TRANSACTION STATISTICS:");
        System.out.printf("  Total Transactions: %d%n", getTotalTransactions());
        System.out.printf("  Total Deposits:     $%.2f%n", getTotalDeposit());
        System.out.printf("  Total Withdrawals:  $%.2f%n", getTotalWithdrawal());
        System.out.printf("  Total Transfers:    $%.2f%n", getTotalTransfer());
        System.out.println();
        System.out.println("BALANCE STATISTICS:");
        System.out.printf("  Total Balance:      $%.2f%n", getTotalBalance());
        System.out.println("============================================");
    }
    
    /**
     * Prints a formatted account summary for a specific customer
     */
    public void printCustomerAccountSummary(Customer customer) {
        double totalDeposit = getCustomerTotalDeposit(customer);
        double totalWithdrawal = getCustomerTotalWithdrawal(customer);
        double totalTransferIn = getCustomerTotalTransferIn(customer);
        double totalTransferOut = getCustomerTotalTransferOut(customer);
        double moneyIn = totalDeposit + totalTransferIn;
        double moneyOut = totalWithdrawal + totalTransferOut;
        
        System.out.println("============================================");
        System.out.println("         CUSTOMER ACCOUNT SUMMARY          ");
        System.out.println("============================================");
        System.out.printf("  Account:     %s%n", customer.getAccNo());
        System.out.printf("  Name:        %s%n", customer.getName());
        System.out.println();
        System.out.println("TRANSACTION SUMMARY:");
        System.out.printf("  Total Deposits:      $%.2f%n", totalDeposit);
        System.out.printf("  Total Withdrawals:   $%.2f%n", totalWithdrawal);
        System.out.printf("  Transfers In:        $%.2f%n", totalTransferIn);
        System.out.printf("  Transfers Out:       $%.2f%n", totalTransferOut);
        System.out.println();
        System.out.printf("  Total Money In:      $%.2f%n", moneyIn);
        System.out.printf("  Total Money Out:     $%.2f%n", moneyOut);
        System.out.println();
        System.out.printf("  Current Balance:     $%.2f%n", customer.getBalance());
        System.out.println("============================================");
    }
    
    // ==================== Helper Methods ====================
    
    private int countQuery(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in count query: " + e.getMessage());
        }
        return 0;
    }
    
    private double sumQuery(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in sum query: " + e.getMessage());
        }
        return 0.0;
    }
    
    private double sumQueryWithDateRange(String sql, String startTimestamp, String endTimestamp) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startTimestamp);
            pstmt.setString(2, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in sum query with date range: " + e.getMessage());
        }
        return 0.0;
    }
    
    private double sumQueryForCustomer(String sql, String id1, String id2) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id1);
            pstmt.setString(2, id2);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in customer sum query: " + e.getMessage());
        }
        return 0.0;
    }
    
    private double sumQueryForCustomerWithDateRange(String sql, String accNo, String startTimestamp, String endTimestamp) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNo);
            pstmt.setString(2, startTimestamp);
            pstmt.setString(3, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in customer sum query with date range: " + e.getMessage());
        }
        return 0.0;
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.getDbUrl());
    }
}
