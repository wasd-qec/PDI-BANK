package Service;

import Object.Customer;
import java.util.Scanner;
import Security.PasswordEncryption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Database.CustomerHandling;


public class CustomerService {
    Scanner scanner = new Scanner(System.in);
    PasswordEncryption pe = new PasswordEncryption();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    CustomerHandling customerHandling = new CustomerHandling();
    
    public boolean isAccountActive(String accNo) {
        Customer customer = customerHandling.getCustomerByAccNo(accNo);
        return customer != null && customer.isActive();
    }
    public Customer createCustomerAccount(String ID, String AccNo, String Name,
    String Password, int Phone, String Address, double Balance, String Birthdate){
    try {
        String HashedPassword = pe.encryptPassword(Password);
        Boolean Active = true;
        String CreateDate = LocalDateTime.now().format(FORMATTER);
        Customer customer = new Customer(
            Name,
            HashedPassword,
            ID,
            AccNo, 
            Balance, 
            Phone, 
            Address, 
            Birthdate,
            CreateDate,
            Active);
        return customer;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
    public void updatePassword(Customer customer, String newPassword) {
        try {
            String hashedPassword = pe.encryptPassword(newPassword);
            customerHandling.updatePassword(customer.getAccNo(), hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Customer getCustomerByAccNo(String accNo) {
        return customerHandling.getCustomerByAccNo(accNo);
    }
}