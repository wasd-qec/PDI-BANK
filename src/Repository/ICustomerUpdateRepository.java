package Repository;

import Object.Customer;

/**
 * Interface Segregation Principle: Separate interface for update operations
 */
public interface ICustomerUpdateRepository {
    void updateBalance(Customer customer);
    void updateBasicInfo(Customer customer);  // phone, address
    void updateFullProfile(Customer customer); // name, id, phone, address, birthdate
    void updatePassword(String accNo, String newPassword);
}
