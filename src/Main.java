import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Read dbReader = new Read();
        CustomRead customDbReader = new CustomRead();
        UI ui = new UI();
        ui.displayWelcomeMessage();
        if (ui.RolePick().equals("2")) {
            // use accno to return customer to  initialized customer
            Customer customer = customDbReader.InitializedCus(ui.LoginPrompt());
            CustomerUI customerUI = new CustomerUI();
            customerUI.showMenu(customer);
        }
        else {
            System.out.println("Admin access granted.");
            AdminUI adminUI = new AdminUI();
            adminUI.showMenu();
        }
    }
        
    
}