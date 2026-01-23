package GUI.Views;

import javax.swing.*;
import java.awt.*;
import GUI.Components.RoundedButton;
import GUI.Components.RoundedPanel;

/**
 * Initial login selection screen
 * Views layer: Presents user with choice between Customer and Admin login
 */
public class LoginSelection extends JPanel {

    private JFrame frame;

    public void showInFrame() {
        frame = new JFrame("ABA Login System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(this);
        frame.setVisible(true);
    }

    public LoginSelection() {
        setBackground(new Color(213, 197, 186));
        setLayout(null);

        RoundedPanel card = new RoundedPanel(30);
        card.setBackground(new Color(8, 25, 64));
        card.setBounds(100, 50, 350, 300);
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        add(card);

        ImageIcon logo = new ImageIcon("src\\GUI\\TMB_Logo.png");
        Image scaled = logo.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(20));
        card.add(logoLabel);

        JLabel title = new JLabel("Login Selection");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(title);

        RoundedButton customerBtn = new RoundedButton("Customer");
        customerBtn.setMaximumSize(new Dimension(180, 40));
        customerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(25));
        card.add(customerBtn);

        RoundedButton adminBtn = new RoundedButton("Admin");
        adminBtn.setMaximumSize(new Dimension(180, 40));
        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(15));
        card.add(adminBtn);

        // ====== BUTTON EVENTS ======
        customerBtn.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new SignInCustomer().setVisible(true);
        });

        adminBtn.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new SignInAdmin().setVisible(true);
        });
    }
}
