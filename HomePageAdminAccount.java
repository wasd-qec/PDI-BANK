import javax.swing.*;
import java.awt.*;

public class HomePageAdminAccount extends JFrame {

    public HomePageAdminAccount() {
        setTitle("Home Page");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(40, 65, 105)); // main background

        // ========== LEFT SIDE PANEL ==========
        JPanel sidePanel = new JPanel(null);
        sidePanel.setBounds(0, 0, 200, 650);
        sidePanel.setBackground(new Color(8, 25, 64));
        add(sidePanel);

        // LOGO
        ImageIcon logo = new ImageIcon("TMB_Logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(45, 40, 110, 110);
        sidePanel.add(logoLabel);

        // ACCOUNT BUTTON
        RoundedButton accountBtn = new RoundedButton("Account");
        accountBtn.setBounds(28, 200, 145, 40);
        accountBtn.setBackground(new Color(218, 186, 121));
        sidePanel.add(accountBtn);

        // TRANSACTION BUTTON
        RoundedButton transactionBtn = new RoundedButton("Transaction");
        transactionBtn.setBounds(28, 250, 145, 40);
        transactionBtn.setBackground(new Color(108, 130, 173));
        sidePanel.add(transactionBtn);

        // LOG OUT
        JLabel logoutLabel = new JLabel("Log out");
        logoutLabel.setForeground(Color.WHITE);
        logoutLabel.setBounds(30, 580, 100, 30);
        sidePanel.add(logoutLabel);

        // ========== SEARCH BAR ==========
        RoundedTextField searchBar = new RoundedTextField(20, "Search account number");
        searchBar.setBounds(240, 30, 710, 40);
        searchBar.setBackground(new Color(235, 235, 235));
        add(searchBar);

        // ========== TOP BUTTONS ==========
        RoundedButton createAccBtn = new RoundedButton("Create account");
        createAccBtn.setBounds(240, 80, 150, 40);
        createAccBtn.setBackground(new Color(218, 186, 121));
        add(createAccBtn);

        RoundedButton filterBtn = new RoundedButton("Filter by");
        filterBtn.setBounds(400, 80, 120, 40);
        filterBtn.setBackground(new Color(218, 186, 121));
        add(filterBtn);

        // ========== ACCOUNTS TITLE ==========
        JLabel accLabel = new JLabel("Accounts");
        accLabel.setForeground(Color.WHITE);
        accLabel.setFont(new Font("Serif", Font.BOLD, 20));
        accLabel.setBounds(240, 130, 200, 40);
        add(accLabel);

        // ========== ACCOUNT BOXES ==========
        int y = 170;
        for (int i = 0; i < 5; i++) {
            RoundedPanel accBox = new RoundedPanel(20);
            accBox.setBackground(new Color(235, 235, 235));
            accBox.setBounds(240, y, 710, 60);
            add(accBox);
            y += 70;
        }

        logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // ========== CLICK ACTIONS ==========
        accountBtn.addActionListener(e -> {
            dispose();
            new HomePageAdminAccount();
        });

        transactionBtn.addActionListener(e -> {
            dispose();
            new HomePageAdminTran(); // placeholder: link to transaction page
        });

        createAccBtn.addActionListener(e -> {
            new CreateAccBT(this);
        });

        filterBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Filter Clicked!");
        });

        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginSelection();
            }
        });

        setVisible(true);
    }

    // ========== RoundedPanel ==========
    class RoundedPanel extends JPanel {
        private int radius;

        RoundedPanel(int r) {
            radius = r;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    // ========== RoundedButton ==========
    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setOpaque(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ========== RoundedTextField (search bar) ==========
    class RoundedTextField extends JTextField {
        private String placeholder;
        private int radius;

        RoundedTextField(int r, String placeholder) {
            this.radius = r;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);

            if (getText().isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.setFont(getFont());
                g2.drawString(placeholder, 12, getHeight() / 2 + 5);
            }
            g2.dispose();
        }
    }

    class CreateAccBT extends JDialog {

        public CreateAccBT(JFrame parent) {
            super(parent, "Creating Account", true);
            setSize(600, 450);
            setLocationRelativeTo(parent);
            setLayout(null);
            getContentPane().setBackground(Color.WHITE);

            JLabel title = new JLabel("Creating Account");
            title.setFont(new Font("Serif", Font.BOLD, 24));
            title.setBounds(50, 20, 450, 40);
            add(title);

            // Username
            JLabel userLabel = new JLabel("Username");
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            userLabel.setBounds(50, 90, 150, 25);
            add(userLabel);

            RoundedTextField userField = new RoundedTextField(20, "Enter username");
            userField.setBackground(new Color(218, 186, 121));
            userField.setBounds(50, 120, 480, 45);
            add(userField);

            // Account Number
            JLabel accLabel = new JLabel("Account number");
            accLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            accLabel.setBounds(50, 180, 200, 25);
            add(accLabel);

            RoundedTextField accField = new RoundedTextField(20, "Enter account number");
            accField.setBackground(new Color(218, 186, 121));
            accField.setBounds(50, 210, 480, 45);
            add(accField);

            // Password
            JLabel passLabel = new JLabel("Password");
            passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            passLabel.setBounds(50, 270, 200, 25);
            add(passLabel);

            RoundedTextField passField = new RoundedTextField(20, "Enter password");
            passField.setBackground(new Color(218, 186, 121));
            passField.setBounds(50, 300, 480, 45);
            add(passField);

            // Buttons
            RoundedButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBackground(new Color(108, 130, 173));
            cancelBtn.setBounds(250, 360, 110, 38);
            add(cancelBtn);

            RoundedButton createBtn = new RoundedButton("Create");
            createBtn.setBackground(new Color(8, 25, 64));
            createBtn.setForeground(Color.WHITE);
            createBtn.setBounds(370, 360, 110, 38);
            add(createBtn);

            cancelBtn.addActionListener(e -> dispose());
            createBtn.addActionListener(e -> {
                dispose();
                new createdPopup(parent, "Account created!");
            });

            setVisible(true);
        }

        class createdPopup extends JDialog {

            public createdPopup(JFrame parent, String message) {
                super(parent, true);
                setSize(420, 150);
                setUndecorated(true);
                setLocationRelativeTo(parent);
                setBackground(new Color(0, 0, 0, 0)); // transparent window

                // Rounded shape for the dialog itself
                setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 420, 150, 40, 40));

                JPanel panel = new JPanel(null) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(245, 238, 228)); // beige background
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                    }
                };
                panel.setBounds(0, 0, 420, 150);
                add(panel);

                JLabel msg = new JLabel(message, SwingConstants.CENTER);
                msg.setFont(new Font("Serif", Font.BOLD, 20));
                msg.setForeground(new Color(10, 32, 68));
                msg.setBounds(0, 50, 420, 30);
                panel.add(msg);

                // Auto close after delay
                Timer timer = new Timer(1400, e -> dispose());
                timer.setRepeats(false);
                timer.start();

                setVisible(true);
            }
        }

    }

    public static void main(String[] args) {
        new HomePageAdminAccount();
    }
}