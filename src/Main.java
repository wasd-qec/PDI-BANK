import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Read dbReader = new Read();
        UI ui = new UI();
        ui.displayWelcomeMessage();
        if (ui.RolePick().equals("2")) {
            // use acc no to return customer to  initialized customer
            Customer customer = dbReader.InitializedCus(ui.LoginPrompt());
        }
        else {
            System.out.println("Admin access granted.");
        }
    }
        
    
}