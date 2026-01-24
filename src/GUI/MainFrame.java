package GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
    public MainFrame() {
        setTitle("ABA Login System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create pages
        LoginSelection selection = new LoginSelection();
        
        mainPanel.add(selection, "selection");
        add(mainPanel);
        cardLayout.show(mainPanel, "selection");
    }

    
}
