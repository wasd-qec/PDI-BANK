package UI;

import Object.Customer;
import Security.PasswordEncryption;
import Database.CustomerImple;
import java.util.Scanner;
import java.util.List;
import Database.Admin;
import Database.TransactionImple;
import Object.Transaction;
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
        System.out.println("1. Exit");
        System.out.println("2. View Transactions");
        System.out.println("3. View Users");
        System.out.println("4. Create new Account");
        System.out.println("5. Deactivate account");
        System.out.println("6. Delete Accounts");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            exit = false;
        } else if (choice == 2) {
            ViewTransactions();
        } else if (choice == 3) {
            ViewUsers();
        } else if (choice == 4) {
            CreateNewAccounts();
        } else if (choice == 5) {
            DeactivateAccount();
        } else if (choice == 6) {
            // Future feature can be added here
        }
        else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
    }
    public void DeactivateAccount() {
        System.out.println("Deactivating Account...");
        scanner.nextLine();
        System.out.println("Enter Account Number to Deactivate: ");
        String accountNumber = scanner.nextLine();
        Customer customer = customerIN.getCustomerByAccNo(accountNumber);
        if (customer != null) {
            customerIN.DeactivateCustomer(accountNumber);
            System.out.println("Account " + accountNumber + " has been deactivated.");
        } else {
            System.out.println("Account not found. Please check the Account Number and try again.");
        }
    }
    public void CreateNewAccounts() {
        System.out.println("Creating New Account...");
        scanner.nextLine();
        CustomerService customerService = new CustomerService();
        System.out.println("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        if (customerIN.existsAccNo(accountNumber)) {
            System.out.println("Account Number already exists. Please try again with a different Account Number.");
            return;
        }
        System.out.println("Enter ID:");
        String id = scanner.nextLine();
        if (customerIN.existsid(id)) {
            System.out.println("ID already exists. Please try again with a different ID.");
            return;
        }
        System.out.println("Enter Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Initial Deposit Amount: ");
        double initialDeposit = scanner.nextDouble();
        System.out.println("Enter Password: ");
        scanner.nextLine(); 
        String password = scanner.nextLine();
        
        System.out.println("Enter Phone Number:");
        int phoneNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Address:");
        String address = scanner.nextLine();
        System.out.println("Enter Birth Date (YYYY-MM-DD):");
        String birthDate = scanner.nextLine();
        Customer newCustomer = customerService.createCustomerAccount(
            id, 
            accountNumber,
            name, 
            password, 
            phoneNumber, 
            address, 
            initialDeposit, 
            birthDate);
            customerIN.save(newCustomer);
        if (newCustomer != null) {
            System.out.println("Account created successfully!");
            System.out.println("Account Number: " + newCustomer.getAccNo());
            System.out.println("Name: " + newCustomer.getName());
            System.out.println("Initial Balance: " + newCustomer.getBalance());
        } else {
            System.out.println("Failed to create account.");
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
