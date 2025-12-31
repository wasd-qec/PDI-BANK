import java.util.Scanner;

public class CustomerUI {
    UXmin ui = new UXmin();
    Scanner scanner = new Scanner(System.in);
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
    }
    
    public void checkBalance(Customer customer) { 
        System.out.printf("\nYour current balance is: $%.2f\n", customer.getBalance());
    }   
    public void deposit(Customer customer) { }
    public void withdraw(Customer customer) { }
    public void transfer(Customer customer) { }
    public void viewTransactionHistory(Customer customer) { }
    public void editProfile(Customer customer) { }
    public void changePassword(Customer customer) { }
}
