import java.util.Scanner;

public class UXmin {
    Scanner input = new Scanner(System.in);
   public void println(String message) {
       System.out.println(message);
   }
   public void clearScreen() {
       System.out.print("\033[H\033[2J");
       System.out.flush();
   }
   public void pause() {
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }
}
