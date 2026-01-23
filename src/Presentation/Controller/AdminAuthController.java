package Presentation.Controller;

import Factory.ServiceFactory;
import Service.IAdminAuthenticationService;

/**
 * Presentation Controller for admin authentication
 * Presentation layer: Handles admin login presentation logic
 * Single Responsibility: Manages admin authentication operations
 */
public class AdminAuthController {
    private final IAdminAuthenticationService adminAuthenticationService =
        ServiceFactory.getAdminAuthenticationService();

    public boolean authenticate(String username, String password) {
        return adminAuthenticationService.authenticate(username, password);
    }
}
