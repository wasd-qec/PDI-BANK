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
}
