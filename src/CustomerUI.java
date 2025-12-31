import java.util.List;
import java.util.Scanner;

public class CustomerUI {
    UXmin ui = new UXmin();
    Scanner scanner = new Scanner(System.in);
    TransactionService TranSer = new TransactionService();
    public void showMenu(Customer customer) {
        
        while (true) {
            ui.clearScreen();
            ui.println("CUSTOMER DASHBOARD - " + customer.getName());
            ui.println("  1. View Profile");
            ui.println("  2. Check Balance");
            ui.println("  3. Deposit Money");
            ui.println("  4. Withdraw Money");
            ui.println("  5. Transfer Money");
            ui.println("  6. View Transaction History");
            ui.println("  7. Edit Profile");
            ui.println("  8. Change Password");
            ui.println("  0. Logout");
            ui.println("==============================");

            ui.println("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> viewProfile(customer);
                case 2 -> checkBalance(customer);
                case 3 -> deposit(customer);
                case 4 -> withdraw(customer);
                case 5 -> transfer(customer);
                case 6 -> viewTransactionHistory(customer);
                case 7 -> editProfile(customer);
                case 8 -> changePassword(customer);
                case 0 -> {
                    ui.println  ("Logged out successfully.");
                    return;
                }
                default -> ui.println("Invalid option.");
            }
        }
    }
    public void viewProfile(Customer customer) {
        System.out.println("\n=== Search Result for Account: " + customer.getAccNo() + " ===");
                System.out.printf("  ID: %-15s | Name: %-20s | Account: %s\n", customer.getId(), customer.getName(), customer.getAccNo());
                System.out.printf("  Balance: $%-10.2f | Phone: %d\n", customer.getBalance(), customer.getPhoneNumber());
                System.out.printf("  Address: %s\n", customer.getAddress());
                System.out.printf("  Birth Date: %-12s | Create Date: %-12s ", customer.getBirthDate(), customer.getCreateDate());
                System.out.println("  " + "-".repeat(80));
        ui.pause();
    }
    
    public void checkBalance(Customer customer) { 
        System.out.printf("\nYour current balance is: $%.2f\n", customer.getBalance());
        ui.pause();
    }   
    public void deposit(Customer customer) {
        System.out.println("\nEnter amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        if (amount > 0) {
            TransactionService.processDeposit(customer, amount);
            System.out.printf("Successfully deposited $%.2f. New balance: $%.2f\n", amount, customer.getBalance());
        } else {
            System.out.println("Deposit failed.");
        }
        ui.pause();
     }
    public void withdraw(Customer customer) {
        System.out.println("\nEnter amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        if (customer.withdraw(amount)) {
            TransactionService.processWithdrawal(customer, amount);
            System.out.printf("Successfully withdrew $%.2f. New balance: $%.2f\n", amount, customer.getBalance());
        } else {
            System.out.println("Withdrawal failed: insufficient funds.");
        }
        ui.pause();
     }
    public void transfer(Customer customer) {
        System.out.println("\nEnter recipient account number: ");
        String recipientAccNo = scanner.nextLine();
        CustomRead dbReader = new CustomRead();
        Customer recipient = dbReader.InitializedCus(recipientAccNo);
        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }
        System.out.println("Enter amount to transfer: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        if (customer.getBalance() < amount) {
            System.out.println("Insufficient funds for transfer.");
            return;
        }
        // Process transfer
        if (customer.withdraw(amount) && recipient.deposit(amount)) {
            TransactionService.processTransfer(customer, recipient, amount);
            System.out.printf("Successfully transferred $%.2f to %s. New balance: $%.2f\n", amount, recipient.getName(), customer.getBalance());
        } else {
            System.out.println("Transfer failed.");
        }
        ui.pause();
     }
    public void viewTransactionHistory(Customer customer) { 
        System.out.println("\n=== Transaction History for Account: " + customer.getAccNo() + " ===");
        List<Transaction> transactions = TransactionService.getCustomerTransactions(customer.getId());
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
        ui.pause();
    }
    public void editProfile(Customer customer) { 
        ui.pause();
    }
    public void changePassword(Customer customer) { 
        ui.pause();
    }
}
