public class UXmin {
   public void println(String message) {
       System.out.println(message);
   }
   public void clearScreen() {
       System.out.print("\033[H\033[2J");
       System.out.flush();
   }
}
