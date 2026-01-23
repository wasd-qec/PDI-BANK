package Presentation.Controller;

import java.util.List;
import java.util.Optional;
import Object.Customer;
import Factory.ServiceFactory;
import Repository.ICustomerRepository;
import Service.ICustomerService;
import Search.CustomerSearchCriteria;
import Search.SearchCustomer;

/**
 * Presentation Controller for account-related operations
 * Presentation layer: Keeps GUI classes thin by delegating to services/repositories
 * Single Responsibility: Manages customer account presentation logic
 */
public class AccountController {
    private final ICustomerRepository customerRepository = ServiceFactory.getCustomerRepository();
    private final ICustomerService customerService = ServiceFactory.getCustomerService();
    private final SearchCustomer searchCustomer = new SearchCustomer();

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findByAccNo(String accNo) {
        return customerRepository.findByAccNo(accNo);
    }

    public List<Customer> findByName(String namePattern) {
        return searchCustomer.findByName(namePattern);
    }

    public List<Customer> filter(CustomerSearchCriteria criteria) {
        return searchCustomer.filter(criteria);
    }

    public boolean existsById(String id) {
        return customerRepository.existsById(id);
    }

    public boolean existsByAccNo(String accNo) {
        return customerRepository.existsByAccNo(accNo);
    }

    public Customer createCustomerAccount(String id, String accNo, String name, String password,
                                          int phone, String address, double balance, String birthDate) {
        return customerService.createCustomerAccount(id, accNo, name, password, phone, address, balance, birthDate);
    }

    public void updateCustomer(Customer customer) {
        customerService.updateProfile(customer);
    }

    public void updateBasicInfo(Customer customer) {
        customerService.updateBasicInfo(customer);
    }

    public void deleteCustomerById(String id) {
        customerService.deleteCustomer(id);
    }

    public void activate(String accNo) {
        customerService.activateAccount(accNo);
    }

    public void deactivate(String accNo) {
        customerService.deactivateAccount(accNo);
    }
}
