package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Object.Customer;

public interface CustomerImple {
    String getPasswordByAccNo(String accNo);
    void save(Customer customer);
    void delete(String customerId);
    List<Customer> getAllCustomers();
    // void updateBalance(Customer customer);
}
