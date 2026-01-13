package UI;

import java.util.Scanner;

import Security.PasswordEncryption;
import Database.CustomerImple;
import Database.TransactionImple;
import Service.TransactionService;
import Object.Customer;
import Object.Transaction;
import java.util.List;

public class CustomerUI {
    UIcomponent ui = new UIcomponent();
    Scanner scanner = new Scanner(System.in);
    CustomerImple customerIN = new CustomerImple();
    TransactionImple transactionImple = new TransactionImple();
    TransactionService transactionService = new TransactionService(customerIN, transactionImple);
    
    public Customer LoginPrompt() {
        
        while(true) {
        PasswordEncryption pe = new PasswordEncryption();
        System.out.print("Enter your Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();
        String storedPassword = customerIN.getPasswordByAccNo(accountNumber);
        if (storedPassword != null && pe.verifyPassword(password, storedPassword)) {
            return customerIN.getCustomerByAccNo(accountNumber);
        } else {
            System.out.println("Login failed! Invalid account number or password.");
        }
}   
}
    public void displayMenu(Customer customer) {
        Boolean Status = true;
        while(Status){ 
        System.out.println("Customer Menu:");
        System.out.println("1. Transfer Funds");
        System.out.println("2. Withdraw Funds");
        System.out.println("3. Deposit Funds");
        System.out.println("4. View Transaction History");
        System.out.println("5. Deactivate Account");
        System.out.println("6. View Account Information");
        System.out.println("8. Logout");

        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("1. Transfer Funds");
            transferFunds(customer);
        } 
        else if (choice == 2) {
            System.out.println("2. Withdrawing Funds");
            withdrawFunds(customer);
        } 
        else if (choice == 3) {
            System.out.println("3. Depositing Funds");
            DepositFunds(customer); 
        } 
        else if (choice == 4) {
            System.out.println("4. View Transaction History");
            viewTransactionHistory(customer);
        } 
        else if (choice == 5) {
            System.out.println("5. Deactivate Account");
            customer.setActive(false);
            customerIN.DeactivateCustomer(customer.getAccNo());
            System.out.println("Account Deactivated Successfully.");
            Status = false;
        }
        else if (choice == 6) {
            System.out.println("6. View Account Information");
            displayinfo(customer);
        }
        else if (choice == 8) {
            System.out.println("8. Logout");
            logout();
            Status = false;
        }
        else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
    public void displayinfo(Customer customer) {
        System.out.println("Account Information:");
        System.out.printf("%-16s %-20s \n","Account Number:", customer.getAccNo());
        System.out.printf("%-16s %-20s \n","Account ID:", customer.getID());
        System.out.printf("%-16s %-20s \n","Phone Number:", customer.getPhoneNumber());
        System.out.printf("%-16s %-20s \n","Address:", customer.getAddress());
        System.out.printf("%-16s %-20s \n","Birth Date:", customer.getBirthDate());
        System.out.printf("%-16s %.2f\n","Balance:", customer.getBalance());
        System.out.printf("%-16s %-20s \n","Account Active:", customer.isActive());
    }
    public void viewTransactionHistory(Customer customer) {
        System.out.println("Transaction History:");
        List<Transaction> transactions = transactionImple.GetTransactionByCustomer(customer);
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
    public void logout() {
        System.out.println("Logging out...");
        ui.pause();
        ui.clearscreen();
        Initial initial = new Initial();
        initial.launch();
    }
    public void transferFunds(Customer customer) {
        scanner.nextLine(); 
        System.out.println("Enter receipient account number:");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter amount to transfer:");
        double amount = scanner.nextDouble();
        Customer receiver = customerIN.getCustomerByAccNo(accountNumber);
        System.out.println(transactionService.processTransfer(customer, receiver, amount));

    }
    public void withdrawFunds(Customer customer) {
        scanner.nextLine();
        System.out.println("Enter amount to Withdraw:");
        double amount = scanner.nextDouble();
        System.out.println(transactionService.WithdrawService(customer,amount));
    }
    public void DepositFunds(Customer customer) {
        scanner.nextLine();
        System.out.println("Enter amount to Deposit:");
        double amount = scanner.nextDouble();
        System.out.println(transactionService.DepositService(customer,amount));
    }
}
