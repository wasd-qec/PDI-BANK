package Service;

import Repository.IAdminRepository;

/**
 * Admin authentication logic
 */
public class AdminAuthenticationServiceImpl implements IAdminAuthenticationService {

    private final IAdminRepository adminRepository;
    private final IPasswordEncryption passwordEncryption;

    public AdminAuthenticationServiceImpl(IAdminRepository adminRepository,
                                          IPasswordEncryption passwordEncryption) {
        this.adminRepository = adminRepository;
        this.passwordEncryption = passwordEncryption;
    }

    @Override
    public boolean authenticate(String username, String password) {
        String storedPassword = adminRepository.getPasswordByUsername(username);
        if (storedPassword == null) {
            return false;
        }
        return passwordEncryption.verifyPassword(password, storedPassword);
    }
}