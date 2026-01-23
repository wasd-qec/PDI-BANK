package GUI.Forms;

import javax.swing.*;
import java.awt.*;

/**
 * Admin login form panel
 * Forms layer: Component for admin authentication UI
 */
public class LoginFormAdmin extends JPanel {

    public LoginFormAdmin(CardLayout cardLayout, JPanel mainPanel) {

        setBackground(new Color(96, 129, 146));
        setLayout(null);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(20, 20, 80, 30);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "selection"));

        JLabel title = new JLabel("Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(230, 100, 240, 30);

        JLabel accLabel = new JLabel("Username");
        accLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accLabel.setForeground(new Color(30, 70, 90));
        accLabel.setBounds(200, 170, 200, 25);

        JTextField accField = new JTextField();
        accField.setBounds(200, 200, 300, 30);

        // Password label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passLabel.setForeground(new Color(30, 70, 90));
        passLabel.setBounds(200, 245, 200, 25);

        // Password field
        JPasswordField passField = new JPasswordField();
        passField.setBounds(200, 275, 300, 30);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(300, 320, 100, 35);

        add(backBtn);
        add(title);
        add(accLabel);
        add(accField);
        add(passLabel);
        add(passField);
        add(loginBtn);
    }
}
