package UI;

import java.util.Scanner;
public class UIcomponent {
    Scanner input = new Scanner(System.in);
    public void clearscreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public void pause() {
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }
}
