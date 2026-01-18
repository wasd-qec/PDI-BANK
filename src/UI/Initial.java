package UI;

import java.util.Scanner;
import Object.Customer;

public class Initial {
    Scanner scanner = new Scanner(System.in);
    public void launch(){
        System.out.println("Customer UI Launched");
        rolepicker();
    }
    public void rolepicker(){

        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("3. Exit");
        System.out.print("Select your role: ");
        int roleChoice = scanner.nextInt();
        switch (roleChoice) {
            case 1:
                System.out.println("Admin role selected.");
                AdminUI adminUI = new AdminUI();
                adminUI.AdminLoginPrompt();
                break;
            case 2:
                System.out.println("Customer role selected.");
                 CustomerUI customerUI = new CustomerUI();
                 Customer customer = customerUI.LoginPrompt();
                 if (!customer.isActive()){
                    System.out.print("The account has been deactivated, please contact our nearest branch!");
                    break;
                 }
                 if (customer != null ) {
                    System.out.println("Login successful!");
                     customerUI.displayMenu(customer);
                 }
                break;
            case 3:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                rolepicker();
                break;
        }
    }
}
