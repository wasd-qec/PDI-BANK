package Database;

import Object.Customer;

public interface ReportInterfaceUser {

    double getCustomerTotalDeposit(Customer customer);
    double getCustomerTotalWithdrawal(Customer customer);
    double getCustomerTotalTransferOut(Customer customer);
    double getCustomerTotalTransferIn(Customer customer);
    void printCustomerAccountSummary(Customer customer);
    double getCustomerTotalDeposit(Customer customer, String startTimestamp, String endTimestamp);
    double getCustomerTotalWithdrawal(Customer customer, String startTimestamp, String endTimestamp);
    double getCustomerTotalTransferOut(Customer customer, String startTimestamp, String endTimestamp);
    double getCustomerTotalTransferIn(Customer customer, String startTimestamp, String endTimestamp);
} 
