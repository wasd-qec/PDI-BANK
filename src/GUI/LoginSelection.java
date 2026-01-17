package GUI;


import javax.swing.*;
import java.awt.*;

public class LoginSelection extends JFrame {

    public LoginSelection() {
        setTitle("Login Selection");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(213, 197, 186));
        setLayout(null);

        RoundedPanel card = new RoundedPanel(30);
        card.setBackground(new Color(8, 25, 64));
        card.setBounds(100, 50, 350, 330);
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        add(card);

        ImageIcon logo = new ImageIcon("PDI-BANK/src/GUI/TMB_Logo.png");
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
            dispose();
            new SignInCustomer();
        });

        adminBtn.addActionListener(e -> {
            dispose();
            new SignInAdmin(); // or AdminSignInPage if you make one
        });

        setVisible(true);
    }

    class RoundedPanel extends JPanel {
        private int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // FIXED ROUND VALUES (proper button shape)
            g2.setColor(new Color(218, 186, 121));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new LoginSelection();
    }
}
