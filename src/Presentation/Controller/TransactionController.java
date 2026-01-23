package Presentation.Controller;

import java.util.Optional;
import Factory.ServiceFactory;
import Object.Customer;
import Object.Transaction;
import Repository.ICustomerRepository;
import Service.ITransactionService;

/**
 * Presentation Controller for transaction-related GUI actions
 * Presentation layer: Coordinates transaction operations with business logic
 * Single Responsibility: Manages transaction presentation logic
 */
public class TransactionController {
    private final ICustomerRepository customerRepository = ServiceFactory.getCustomerRepository();
    private final ITransactionService transactionService = ServiceFactory.getTransactionService();

    public Optional<Customer> findCustomerByAccNo(String accNo) {
        return customerRepository.findByAccNo(accNo);
    }

    public Transaction processDeposit(Customer customer, double amount) {
        return transactionService.processDeposit(customer, amount);
    }

    public Transaction processWithdrawal(Customer customer, double amount) {
        return transactionService.processWithdrawal(customer, amount);
    }

    public Transaction processTransfer(Customer sender, Customer receiver, double amount) {
        return transactionService.processTransfer(sender, receiver, amount);
    }
}
