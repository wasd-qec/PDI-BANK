import java.util.Scanner;
public class AdminUI {
    UXmin ui = new UXmin();
    Read dbReader = new Read();
    public void showMenu() {

        while(true) {
        ui.clearScreen();
        ui.println("Admin Menu:");
        ui.println("1. View Transactions");
        ui.println("2. View Users");
        ui.println("3. System Settings");
        ui.println("4. Logout");
        ui.println("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                dbReader.readAllTransactions();
                ui.pause();
                break;
            case 2:
                dbReader.readAllUsers();
                ui.pause();
                break;
            case 3:
                // System Settings
                break;
            case 4:
                return;
            default:
                ui.println("Invalid choice. Please try again.");
        }
    }
}
}
