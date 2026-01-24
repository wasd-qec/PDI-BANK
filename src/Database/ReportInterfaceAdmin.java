package Database;

public interface ReportInterfaceAdmin {
    double getTotalDeposit();
    double getTotalWithdrawal();
    double getTotalTransfer();
    int getTotalTransactions();
    double getTotalDeposit(String startTimestamp, String endTimestamp);
    double getTotalWithdrawal(String startTimestamp, String endTimestamp);
    double getTotalTransfer(String startTimestamp, String endTimestamp);
    int getTotalTransactions(String startTimestamp, String endTimestamp);
    double getTotalBalance();
    int getTotalUsers();
    int getActiveUsers();
    int getDeactivatedUsers();
    void printSummaryReport();
}
