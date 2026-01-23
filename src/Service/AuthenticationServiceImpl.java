package Service;

import Repository.CustomerRepository;

/**
 * Single Responsibility: Handles authentication logic only
 * Dependency Inversion: Depends on abstractions
 */
public class AuthenticationServiceImpl implements IAuthenticationService {
    
    private final CustomerRepository customerRepository;
    private final IPasswordEncryption passwordEncryption;
    
    public AuthenticationServiceImpl(CustomerRepository customerRepository,
                                     IPasswordEncryption passwordEncryption) {
        this.customerRepository = customerRepository;
        this.passwordEncryption = passwordEncryption;
    }
    
    @Override
    public boolean authenticate(String accNo, String password) {
        String storedPassword = customerRepository.getPasswordByAccNo(accNo);
        if (storedPassword == null) {
            return false;
        }
        return passwordEncryption.verifyPassword(password, storedPassword);
    }
    
    @Override
    public boolean validatePassword(String accNo, String password) {
        return authenticate(accNo, password);
    }
}
