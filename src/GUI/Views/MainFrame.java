package GUI.Views;

import javax.swing.*;

/**
 * Main entry point for the application GUI
 * Views layer: Orchestrates the presentation flow
 */
public class MainFrame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginSelection().showInFrame();
        });
    }

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
