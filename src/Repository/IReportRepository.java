package Repository;

import Object.Customer;

/**
 * Interface for report/statistics data access
 */
public interface IReportRepository {
    // User statistics
    int getTotalUsers();
    int getActiveUsers();
    int getDeactivatedUsers();
    double getTotalBalance();
    
    // Transaction statistics (all time)
    int getTotalTransactions();
    double getTotalDeposit();
    double getTotalWithdrawal();
    double getTotalTransfer();
    
    // Transaction statistics (date range)
    double getTotalDeposit(String startTimestamp, String endTimestamp);
    double getTotalWithdrawal(String startTimestamp, String endTimestamp);
    double getTotalTransfer(String startTimestamp, String endTimestamp);
    
    // Customer-specific statistics
    double getCustomerTotalDeposit(Customer customer);
    double getCustomerTotalWithdrawal(Customer customer);
    double getCustomerTotalTransferIn(Customer customer);
    double getCustomerTotalTransferOut(Customer customer);
    
    // Customer statistics (date range)
    double getCustomerTotalDeposit(Customer customer, String startTimestamp, String endTimestamp);
    double getCustomerTotalWithdrawal(Customer customer, String startTimestamp, String endTimestamp);
    double getCustomerTotalTransferIn(Customer customer, String startTimestamp, String endTimestamp);
    double getCustomerTotalTransferOut(Customer customer, String startTimestamp, String endTimestamp);
}
