package UI;

import java.util.Scanner;

import Security.PasswordEncryption;
import Database.CustomerImple;
import Database.TransactionImple;
import Service.TransactionService;
import Object.Customer;

public class CustomerUI {
    UIcomponent ui = new UIcomponent();
    Scanner scanner = new Scanner(System.in);
    CustomerImple customerIN = new CustomerImple();
    
    public Customer LoginPrompt() {
        
        while(true) {
        PasswordEncryption pe = new PasswordEncryption();
        System.out.print("Enter your Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();
        String storedPassword = customerIN.getPasswordByAccNo(accountNumber);
        if (storedPassword != null && pe.verifyPassword(password, storedPassword)) {
            System.out.println("Login successful!");
            return customerIN.getCustomerByAccNo(accountNumber);
        } else {
            System.out.println("Login failed! Invalid account number or password.");
        
        }
}   
}
    public void displayMenu(Customer customer) {
        System.out.println("Customer Menu:");
        System.out.println("1. Transfer Funds");
        System.out.println("2. Withdraw Funds");
        System.out.println("3. Deposit Funds");

        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                transferFunds(customer);
                break;
            case 2:
                System.out.println("Displaying Cart...");
                // Code to display cart
                break;
            case 3:
                System.out.println("Proceeding to Checkout...");
                // Code to handle checkout
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayMenu(customer);
                break;
        }
    }
    public void transferFunds(Customer customer) {
        TransactionService transactionService = new TransactionService(customerIN, new TransactionImple());
        scanner.nextLine(); // Consume newline
        System.out.println("Enter receipient account number:");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter amount to transfer:");
        double amount = scanner.nextDouble();
        Customer receiver = customerIN.getCustomerByAccNo(accountNumber);
        System.out.println(transactionService.processTransfer(customer, receiver, amount));

    }
}
