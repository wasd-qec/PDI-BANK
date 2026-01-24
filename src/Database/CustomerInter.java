package Database;

import java.util.List;

import Object.Customer;

public interface CustomerInter {
    String getPasswordByAccNo(String accNo);
    Customer getCustomerByAccNo(String accNo);
    void save(Customer customer);
    void delete(String customerId);
    List<Customer> getAllCustomers();
    void updateBalance(Customer customer);
    boolean existsid(String id);
    boolean existsAccNo(String accNo);
    boolean IsActive(String accNo);
    void DeactivateCustomer(String accNo);
    void ActivateCustomer(String accNo);
    void updateCustomerInfo(Customer customer);
    void updateCustomerPro(Customer customer);
    void updatePassword(String accNo, String newPassword);

}
