package Presentation.Controller;

import Factory.ServiceFactory;
import Object.Customer;
import Repository.ICustomerRepository;
import Service.IAuthenticationService;

/**
 * Presentation Controller for customer authentication
 * Presentation layer: Handles customer login presentation logic
 * Single Responsibility: Manages customer authentication operations
 */
public class CustomerAuthController {
    private final IAuthenticationService authenticationService = ServiceFactory.getAuthenticationService();
    private final ICustomerRepository customerRepository = ServiceFactory.getCustomerRepository();

    public boolean authenticate(String accNo, String password) {
        return authenticationService.authenticate(accNo, password);
    }

    public Customer findCustomerByAccNo(String accNo) {
        return customerRepository.findByAccNo(accNo).orElse(null);
    }
}
