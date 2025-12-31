import java.util.Scanner;


public class UI {
    Scanner scanner = new Scanner(System.in);
    Read dbReader = new Read();
    CustomRead customDbReader = new CustomRead();
    public void displayWelcomeMessage() {
        System.out.println("===================================");
        System.out.println("   Welcome to the Banking System   ");
        System.out.println("===================================");
    }
    public String RolePick() {
        while(true) {
            System.out.println("Select your role:");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.print("Enter choice (1 or 2): ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println("Admin role selected.");
                    return "1";
                case "2":
                    System.out.println("User role selected.");
                    return "2";
                default:
                    System.out.println("Invalid choice. Please select 1 or 2.");
            }
        }
    }
    public String LoginPrompt() {
        while(true) {
        PasswordEncryption pe = new PasswordEncryption();
        System.out.print("Enter your Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();
        CustomRead customDbReader = new CustomRead();
        String storedPassword = customDbReader.getPasswordByAccNo(accountNumber);
        if (storedPassword != null && pe.verifyPassword(password, storedPassword)) {
            System.out.println("Login successful!");
            return accountNumber;
        } else {
            System.out.println("Login failed! Invalid account number or password.");
        
        }
}   
}
}