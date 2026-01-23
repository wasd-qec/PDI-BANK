import GUI.Views.MainFrame;

/**
 * Main entry point for PDI-BAN Banking Application
 * Launches the GUI with SOLID architecture refactoring
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Launching PDI-BAN Application...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
