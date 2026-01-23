package Presentation.Controller;

import java.util.List;
import Factory.ServiceFactory;
import Object.Transaction;
import Repository.IReportRepository;
import Repository.ITransactionRepository;
import Search.TransactionSearchCriteria;
import Search.SearchTransaction;

/**
 * Presentation Controller for admin transaction screen
 * Presentation layer: Provides transaction data to admin views
 * Single Responsibility: Handles admin transaction presentation logic
 */
public class AdminTransactionController {
    private final ITransactionRepository transactionRepository = ServiceFactory.getTransactionRepository();
    private final IReportRepository reportRepository = ServiceFactory.getReportRepository();

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public double getTotalDeposit() {
        return reportRepository.getTotalDeposit();
    }

    public double getTotalWithdrawal() {
        return reportRepository.getTotalWithdrawal();
    }

    public double getTotalTransfer() {
        return reportRepository.getTotalTransfer();
    }

    public int getTotalUsers() {
        return reportRepository.getTotalUsers();
    }

    public int getActiveUsers() {
        return reportRepository.getActiveUsers();
    }

    public int getDeactivatedUsers() {
        return reportRepository.getDeactivatedUsers();
    }

    public double getTotalDeposit(String startDate, String endDate) {
        return reportRepository.getTotalDeposit(startDate, endDate);
    }

    public double getTotalWithdrawal(String startDate, String endDate) {
        return reportRepository.getTotalWithdrawal(startDate, endDate);
    }

    public double getTotalTransfer(String startDate, String endDate) {
        return reportRepository.getTotalTransfer(startDate, endDate);
    }

    public double getTotalBalance() {
        return reportRepository.getTotalBalance();
    }

    public Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    public List<Transaction> filter(TransactionSearchCriteria criteria) {
        SearchTransaction search = new SearchTransaction();
        return search.filter(criteria);
    }
}
