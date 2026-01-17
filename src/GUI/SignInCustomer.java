
import javax.swing.*;
import java.awt.*;

public class SignInCustomer extends JFrame {

    public SignInCustomer() {
        setTitle("Sign In");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(213, 197, 186));
        setLayout(null);

        // --- BACK BUTTON ---
        RoundedButton backBtn = new RoundedButton("Back");
        backBtn.setBackground(new Color(8, 25, 64));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBounds(20, 20, 100, 35);
        add(backBtn);

        // --- BLUE CARD PANEL ---
        RoundedPanel card = new RoundedPanel(35);
        card.setBounds(160, 40, 350, 400);
        card.setBackground(new Color(8, 25, 64));
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        add(card);

        // --- LOGO ---
        ImageIcon logo = new ImageIcon("PDI-BANK/src/GUI/TMB_Logo.png");
        Image scaledImg = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(25));
        card.add(logoLabel);

        // --- TITLE ---
        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(title);

        // --- USERNAME FIELD ---
        RoundedTextField usernameField = new RoundedTextField(15, "Username");
        usernameField.setMaximumSize(new Dimension(250, 35));
        usernameField.setBackground(new Color(35, 55, 112));
        usernameField.setForeground(Color.WHITE);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(Box.createVerticalStrut(25));
        card.add(usernameField);

        RoundedPasswordField passwordField = new RoundedPasswordField(15, "Password");
        passwordField.setMaximumSize(new Dimension(250, 35));
        passwordField.setBackground(new Color(35, 55, 112));
        passwordField.setForeground(Color.WHITE);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(Box.createVerticalStrut(10));
        card.add(passwordField);

        // --- FORGOT PASSWORD LINK ---
        JLabel forgotPass = new JLabel("Forget password");
        forgotPass.setForeground(Color.WHITE);
        forgotPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgotPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(15));
        card.add(forgotPass);

        // --- SIGN IN BUTTON ---
        RoundedButton signInBtn = new RoundedButton("Sign In");
        signInBtn.setMaximumSize(new Dimension(120, 35));
        signInBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInBtn.setBackground(new Color(218, 186, 121));
        card.add(Box.createVerticalStrut(15));
        card.add(signInBtn);

        setVisible(true);

        backBtn.addActionListener(e -> {
            dispose();
            new LoginSelection();
        });

        signInBtn.addActionListener(e -> {
            dispose();
            new HomePageCustomer();
        });

    }

    // Rounded Panel for card
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

    // Rounded Button Class
    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Rounded Text Field Class
    class RoundedTextField extends JTextField {
        private int radius;
        private String placeholder;

        RoundedTextField(int radius, String placeholder) {
            this.radius = radius;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);

            // show placeholder only if empty
            if (getText().isEmpty()) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(getFont());
                g2.drawString(placeholder, 12, getHeight() / 2 + 5);
            }

            g2.dispose();
        }
    }
    // Rounded Password Field Class

    class RoundedPasswordField extends JPasswordField {
        private int radius;
        private String placeholder;

        RoundedPasswordField(int radius, String placeholder) {
            this.radius = radius;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);

            // show placeholder only if empty
            if (getPassword().length == 0) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(getFont());
                g2.drawString(placeholder, 12, getHeight() / 2 + 5);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new SignInCustomer();
    }
}
