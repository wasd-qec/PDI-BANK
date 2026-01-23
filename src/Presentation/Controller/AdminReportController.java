package Presentation.Controller;

import Factory.ServiceFactory;
import Repository.IReportRepository;

/**
 * Presentation Controller for admin reporting screens
 * Presentation layer: Provides reporting data to admin views
 * Single Responsibility: Handles admin report presentation logic
 */
public class AdminReportController {
    private final IReportRepository reportRepository = ServiceFactory.getReportRepository();

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

    public double getTotalBalance() {
        return reportRepository.getTotalBalance();
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
}
