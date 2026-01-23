package Presentation.Controller;

import java.util.List;
import Factory.ServiceFactory;
import Object.Customer;
import Object.Transaction;
import Repository.ICustomerRepository;
import Repository.IReportRepository;
import Repository.ITransactionRepository;
import Service.ICustomerService;

/**
 * Presentation Controller for customer home screen operations
 * Presentation layer: Coordinates GUI components with business logic
 * Single Responsibility: Handles customer dashboard presentation logic
 */
public class CustomerDashboardController {
    private final ICustomerRepository customerRepository = ServiceFactory.getCustomerRepository();
    private final ITransactionRepository transactionRepository = ServiceFactory.getTransactionRepository();
    private final IReportRepository reportRepository = ServiceFactory.getReportRepository();
    private final ICustomerService customerService = ServiceFactory.getCustomerService();

    public List<Transaction> getTransactionsForCustomer(Customer customer) {
        return transactionRepository.findByCustomer(customer);
    }

    public Customer refreshCustomer(String accNo, Customer fallback) {
        return customerRepository.findByAccNo(accNo).orElse(fallback);
    }

    public void updateBasicInfo(Customer customer) {
        customerService.updateBasicInfo(customer);
    }

    public void deactivateAccount(String accNo) {
        customerService.deactivateAccount(accNo);
    }

    public double getCustomerTotalDeposit(Customer customer) {
        return reportRepository.getCustomerTotalDeposit(customer);
    }

    public double getCustomerTotalWithdrawal(Customer customer) {
        return reportRepository.getCustomerTotalWithdrawal(customer);
    }

    public double getCustomerTotalTransferIn(Customer customer) {
        return reportRepository.getCustomerTotalTransferIn(customer);
    }

    public double getCustomerTotalTransferOut(Customer customer) {
        return reportRepository.getCustomerTotalTransferOut(customer);
    }

    public double getCustomerTotalDeposit(Customer customer, String startDate, String endDate) {
        return reportRepository.getCustomerTotalDeposit(customer, startDate, endDate);
    }

    public double getCustomerTotalWithdrawal(Customer customer, String startDate, String endDate) {
        return reportRepository.getCustomerTotalWithdrawal(customer, startDate, endDate);
    }

    public double getCustomerTotalTransferIn(Customer customer, String startDate, String endDate) {
        return reportRepository.getCustomerTotalTransferIn(customer, startDate, endDate);
    }

    public double getCustomerTotalTransferOut(Customer customer, String startDate, String endDate) {
        return reportRepository.getCustomerTotalTransferOut(customer, startDate, endDate);
    }
}
