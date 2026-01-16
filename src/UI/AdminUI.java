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
import Search.SearchCustomer;
import Search.SearchTransaction;
import Search.CustomerSearchCriteria;
import Search.TransactionSearchCriteria;

public class AdminUI {
    Scanner scanner = new Scanner(System.in);
    Admin adminIN = new Admin();
    TransactionImple transactionimple = new TransactionImple();
    CustomerImple customerIN = new CustomerImple();
    SearchCustomer searchCustomer = new SearchCustomer();
    SearchTransaction searchTransaction = new SearchTransaction();
    
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
        System.out.println("7. Filter Transactions");
        System.out.println("8. Filter Customers");
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
        } else if (choice == 7) {
            filterTransactionsMenu();
        } else if (choice == 8) {
            filterCustomersMenu();
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

    public void filterTransactionsMenu() {
        scanner.nextLine(); // consume newline
        TransactionSearchCriteria.Builder builder = TransactionSearchCriteria.builder();
        
        System.out.println("\n=== Filter Transactions ===");
        System.out.println("Leave blank to skip any filter.\n");

        System.out.print("Transaction ID (exact): ");
        String txId = scanner.nextLine().trim();
        if (!txId.isEmpty()) builder.transactionId(txId);

        System.out.print("Sender ID: ");
        String senderId = scanner.nextLine().trim();
        if (!senderId.isEmpty()) builder.senderId(senderId);

        System.out.print("Receiver ID: ");
        String receiverId = scanner.nextLine().trim();
        if (!receiverId.isEmpty()) builder.receiverId(receiverId);

        System.out.print("Type (e.g., DEPOSIT, WITHDRAW, TRANSFER): ");
        String type = scanner.nextLine().trim();
        if (!type.isEmpty()) builder.type(type);

        System.out.print("Min Amount: ");
        String minAmt = scanner.nextLine().trim();
        if (!minAmt.isEmpty()) builder.minAmount(Double.parseDouble(minAmt));

        System.out.print("Max Amount: ");
        String maxAmt = scanner.nextLine().trim();
        if (!maxAmt.isEmpty()) builder.maxAmount(Double.parseDouble(maxAmt));

        System.out.print("Date From (YYYY-MM-DD): ");
        String dateFrom = scanner.nextLine().trim();
        if (!dateFrom.isEmpty()) builder.dateFrom(dateFrom);

        System.out.print("Date To (YYYY-MM-DD): ");
        String dateTo = scanner.nextLine().trim();
        if (!dateTo.isEmpty()) builder.dateTo(dateTo);

        TransactionSearchCriteria criteria = builder.build();
        List<Transaction> results = searchTransaction.filter(criteria);

        System.out.println("\n--- Results: " + results.size() + " transaction(s) ---");
        System.out.printf("%-36s | %-10s | %-10s | %10s | %-10s | %s\n",
                "TransactionID", "Receiver", "Sender", "Amount", "Type", "Timestamp");
        System.out.println("-".repeat(100));
        for (Transaction t : results) {
            System.out.printf("%-36s | %-10s | %-10s | %10.2f | %-10s | %s\n",
                t.getTransactionID(),
                t.getReceiverID(),
                t.getSenderID(),
                t.getAmount(),
                t.getType(),
                t.getTimestamp()
            );
        }
    }

    public void filterCustomersMenu() {
        scanner.nextLine(); // consume newline
        CustomerSearchCriteria.Builder builder = CustomerSearchCriteria.builder();

        System.out.println("\n=== Filter Customers ===");
        System.out.println("Leave blank to skip any filter.\n");

        System.out.print("Account Number (exact): ");
        String accNo = scanner.nextLine().trim();
        if (!accNo.isEmpty()) builder.accNo(accNo);

        System.out.print("ID (exact): ");
        String id = scanner.nextLine().trim();
        if (!id.isEmpty()) builder.id(id);

        System.out.print("Name (contains): ");
        String nameFilter = scanner.nextLine().trim();
        if (!nameFilter.isEmpty()) builder.nameFilter(nameFilter);

        System.out.print("Address (contains): ");
        String addressFilter = scanner.nextLine().trim();
        if (!addressFilter.isEmpty()) builder.addressFilter(addressFilter);

        System.out.print("Min Balance: ");
        String minBal = scanner.nextLine().trim();
        if (!minBal.isEmpty()) builder.minBalance(Double.parseDouble(minBal));

        System.out.print("Max Balance: ");
        String maxBal = scanner.nextLine().trim();
        if (!maxBal.isEmpty()) builder.maxBalance(Double.parseDouble(maxBal));

        System.out.print("Active only? (yes/no/blank): ");
        String activeStr = scanner.nextLine().trim().toLowerCase();
        if (activeStr.equals("yes")) builder.active(true);
        else if (activeStr.equals("no")) builder.active(false);

        System.out.print("Created From (YYYY-MM-DD): ");
        String createFrom = scanner.nextLine().trim();
        if (!createFrom.isEmpty()) builder.createDateFrom(createFrom);

        System.out.print("Created To (YYYY-MM-DD): ");
        String createTo = scanner.nextLine().trim();
        if (!createTo.isEmpty()) builder.createDateTo(createTo);

        CustomerSearchCriteria criteria = builder.build();
        List<Customer> results = searchCustomer.filter(criteria);

        System.out.println("\n--- Results: " + results.size() + " customer(s) ---");
        System.out.printf("%-15s | %-15s | %-20s | %12s | %-8s\n",
                "AccNo", "ID", "Name", "Balance", "Active");
        System.out.println("-".repeat(80));
        for (Customer c : results) {
            System.out.printf("%-15s | %-15s | %-20s | %12.2f | %-8s\n",
                c.getAccNo(),
                c.getID(),
                c.getName(),
                c.getBalance(),
                c.isActive() ? "Yes" : "No"
            );
        }
    }
}
