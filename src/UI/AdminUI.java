package UI;

import Object.Customer;
import Security.PasswordEncryption;
import Database.CustomerImple;
import java.util.Scanner;
import java.util.List;
import Database.Admin;
import Database.TransactionImple;
import Object.Transaction;
import Service.TransactionService;
import Service.CustomerService;

public class AdminUI {
    Scanner scanner = new Scanner(System.in);
    Admin adminIN = new Admin();
    TransactionImple transactionimple = new TransactionImple();
    CustomerImple customerIN = new CustomerImple();
    
    public void AdminLoginPrompt() {
        PasswordEncryption pe = new PasswordEncryption();
        System.out.print("Enter your Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();
        String storedPassword = adminIN.getPasswordByAccNo(accountNumber);
        if (storedPassword != null && pe.verifyPassword(password, storedPassword)) {
            System.out.println("Login successful!");
            displayMenu();
        } else {
            System.out.println("Login failed! Invalid account number or password.");
        }
    }
    
    public void displayMenu() {
        Boolean exit = true;
        while(exit){
        System.out.println("Admin Menu:");
        System.out.println("1. View Reports");
        System.out.println("2. Manage Users");
        System.out.println("3. Exit");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        if (choice < 1 || choice > 3) {
            System.out.println("Invalid choice. Please try again.");
        }
        if (choice == 1) {
            ViewTransactions();
        } else if (choice == 2) {
            ViewUsers();
        } else if (choice == 3) {
            exit = false;
        }
        }
    }

    public void ViewTransactions() {
        System.out.println("Viewing Reports...");
        List<Transaction> transactions = transactionimple.ShowAllTransaction();

        for (Transaction transaction : transactions) {
            System.out.printf("%-36s | %-10s | %-10s | %10.2f | %-10s | %s\n",
                transaction.getTransactionID(),
                transaction.getReceiverID(),
                transaction.getSenderID(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getTimestamp()
            );
        }
        
    }

    public void ViewUsers() {
        System.out.println("Managing Users...");
        List<Customer> accounts = customerIN.getAllCustomers();
        
        for (Customer account : accounts) {
            System.out.printf("%-15s | %-20s | %10.2f\n", account.getAccNo(), account.getName(), account.getBalance());
        }
    }
}
