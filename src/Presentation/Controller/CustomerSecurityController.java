package Presentation.Controller;

import Factory.ServiceFactory;
import Object.Customer;
import Service.IAuthenticationService;
import Service.ICustomerService;

/**
 * Presentation Controller for customer security actions
 * Presentation layer: Manages password change operations
 * Single Responsibility: Handles customer security-related tasks
 */
public class CustomerSecurityController {
    private final IAuthenticationService authenticationService = ServiceFactory.getAuthenticationService();
    private final ICustomerService customerService = ServiceFactory.getCustomerService();

    public boolean validateOldPassword(String accNo, String oldPassword) {
        return authenticationService.authenticate(accNo, oldPassword);
    }

    public void changePassword(Customer customer, String newPassword) {
        customerService.updatePassword(customer, newPassword);
    }
}
