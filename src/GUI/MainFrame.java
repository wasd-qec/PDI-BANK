package GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    public MainFrame() {
        setTitle("ABA Login System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create pages
        LoginSelection selection = new LoginSelection(cardLayout, mainPanel);
        LoginFormAdmin adminForm = new LoginFormAdmin(cardLayout, mainPanel);
        LoginFormUser userForm = new LoginFormUser(cardLayout, mainPanel);

        // Add pages
        mainPanel.add(selection, "selection");
        mainPanel.add(adminForm, "admin");
        mainPanel.add(userForm, "user");

        add(mainPanel);
        cardLayout.show(mainPanel, "selection");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
