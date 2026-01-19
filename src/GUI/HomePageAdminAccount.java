package GUI;

import javax.swing.*;
import java.awt.*;

public class HomePageAdminAccount extends JFrame {

    public HomePageAdminAccount() {
        
        setTitle("Home Page");
        setSize(1000, 650);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
        ImageIcon logo = new ImageIcon("PDI-BANK/src/GUI/TMB_Logo.png");
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
        String[][] accounts = {
            {"Both", "ABC002", "$12,500.5"},
            {"Caro", "ABC003", "$26,599.75"},
            {"Heng", "ABC001", "-$33,501"},
            {"Rith", "ABC004", "$32,500"},
            {"Inaco", "ABC005", "$35,400"}
        };

        int y = 170;
        for (String[] account : accounts) {
            RoundedPanel accBox = new RoundedPanel(20);
            accBox.setBackground(new Color(235, 235, 235));
            accBox.setBounds(240, y, 710, 60);
            accBox.setLayout(null);
            add(accBox);
            
            JLabel nameLabel = new JLabel(account[0]);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            nameLabel.setForeground(new Color(30, 50, 85));
            nameLabel.setBounds(20, 10, 150, 20);
            accBox.add(nameLabel);
            
            JLabel accNumLabel = new JLabel(account[1]);
            accNumLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            accNumLabel.setForeground(new Color(100, 100, 100));
            accNumLabel.setBounds(20, 35, 150, 15);
            accBox.add(accNumLabel);
            
            JLabel balanceLabel = new JLabel(account[2]);
            balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            if (account[2].startsWith("-")) {
                balanceLabel.setForeground(new Color(220, 20, 60)); // Red for negative
            } else {
                balanceLabel.setForeground(new Color(34, 139, 34)); // Green for positive
            }
            balanceLabel.setBounds(600, 20, 100, 25);
            accBox.add(balanceLabel);
            
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
            new FilterCustomersDialog(this);
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
        setSize(510, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Creating Account");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(new Color(30, 50, 85));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 15, 500, 40);
        add(title);

      // Divider line
        JPanel line = new JPanel();
        line.setBackground(new Color(30, 50, 85));
        line.setBounds(30, 62, 440, 1);
        add(line);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(new Color(30, 50, 85));
        userLabel.setBounds(30, 75, 100, 20);
        add(userLabel);

        RoundedFilterField userField = new RoundedFilterField();
        userField.setBounds(30, 100, 440, 35);
        userField.setBackground(new Color(218, 186, 121));
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userField.setForeground(new Color(180, 160, 130));
        userField.setText("Enter username");
        userField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (userField.getText().equals("Enter username")) {
                    userField.setText("");
                    userField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (userField.getText().isEmpty()) {
                    userField.setForeground(new Color(180, 160, 130));
                    userField.setText("Enter username");
                }
            }
        });
        add(userField);

        // Account Number
        JLabel accLabel = new JLabel("Account number");
        accLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accLabel.setForeground(new Color(30, 50, 85));
        accLabel.setBounds(30, 150, 200, 20);
        add(accLabel);

        RoundedFilterField accField = new RoundedFilterField();
        accField.setBounds(30, 175, 440, 35);
        accField.setBackground(new Color(218, 186, 121));
        accField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accField.setForeground(new Color(180, 160, 130));
        accField.setText("Enter account number");
        accField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (accField.getText().equals("Enter account number")) {
                    accField.setText("");
                    accField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (accField.getText().isEmpty()) {
                    accField.setForeground(new Color(180, 160, 130));
                    accField.setText("Enter account number");
                }
            }
        });
        add(accField);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passLabel.setForeground(new Color(30, 50, 85));
        passLabel.setBounds(30, 225, 200, 20);
        add(passLabel);

        RoundedFilterField passField = new RoundedFilterField();
        passField.setBounds(30, 250, 440, 35);
        passField.setBackground(new Color(218, 186, 121));
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passField.setForeground(new Color(180, 160, 130));
        passField.setText("Enter password");
        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (passField.getText().equals("Enter password")) {
                    passField.setText("");
                    passField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (passField.getText().isEmpty()) {
                    passField.setForeground(new Color(180, 160, 130));
                    passField.setText("Enter password");
                }
            }
        });
        add(passField);

        // Cancel Button
        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBounds(200, 300, 110, 35);
        cancelBtn.setBackground(new Color(70, 90, 130));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        // Create Button
        RoundedButton createBtn = new RoundedButton("Create");
        createBtn.setBounds(320, 300, 110, 35);
        createBtn.setBackground(new Color(8, 25, 64));
        createBtn.setForeground(Color.WHITE);
        createBtn.addActionListener(e -> {
            dispose();
            new createdPopup(parent, "Account created!");
        });
        add(createBtn);

        setVisible(true);
    }

    class RoundedFilterField extends JTextField {
        public RoundedFilterField() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            super.paintComponent(g);
        }
    }

    class createdPopup extends JDialog {
        public createdPopup(JFrame parent, String message) {
            super(parent, true);
            setSize(420, 150);
            setUndecorated(true);
            setLocationRelativeTo(parent);
            setBackground(new Color(0, 0, 0, 0));

            setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 420, 150, 40, 40));

            JPanel panel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(245, 238, 228));
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

            Timer timer = new Timer(1400, e -> dispose());
            timer.setRepeats(false);
            timer.start();

            setVisible(true);
        }
    }
}
    class FilterCustomersDialog extends JDialog {
    
        public FilterCustomersDialog(JFrame parent) {
            super(parent, "Filter Customers", true);
            setSize(550, 480);
            setLocationRelativeTo(parent);
            setResizable(false);
            setLayout(null);
            getContentPane().setBackground(new Color(245, 240, 235));
            
            // Title
            JLabel titleLabel = new JLabel("Filter Customers By:");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(30, 50, 85));
            titleLabel.setBounds(30, 20, 300, 25);
            titleLabel.setForeground(Color.BLACK);
            add(titleLabel);
            
            // Name
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            nameLabel.setForeground(new Color(30, 50, 85));
            nameLabel.setBounds(30, 60, 100, 20);
            add(nameLabel);

            RoundedFilterField nameField = new RoundedFilterField();
            nameField.setBounds(30, 85, 220, 35);
            nameField.setBackground(new Color(218, 186, 121));
            nameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            nameField.setForeground(Color.BLACK);
            add(nameField);

            // Status
            JLabel statusLabel = new JLabel("Status:");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusLabel.setForeground(new Color(30, 50, 85));
            statusLabel.setBounds(280, 60, 100, 20);
            add(statusLabel);

            String[] statusOptions = {"All", "Active", "Deactivated", "Deleted"};
            JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
            statusCombo.setBounds(280, 85, 240, 35);
            statusCombo.setBackground(new Color(218, 186, 121));
            statusCombo.setForeground(Color.BLACK);
            statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            add(statusCombo);

            // Created From
            JLabel createdLabel = new JLabel("Created From:");
            createdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            createdLabel.setForeground(new Color(30, 50, 85));
            createdLabel.setBounds(30, 135, 100, 20);
            add(createdLabel);

            RoundedFilterField createdFromField = new RoundedFilterField();
            createdFromField.setBounds(30, 160, 220, 35);
            createdFromField.setBackground(new Color(218, 186, 121));
            createdFromField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            createdFromField.setForeground(Color.BLACK);
            add(createdFromField);

            // Created To
            JLabel toLabel = new JLabel("To:");
            toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            toLabel.setForeground(new Color(30, 50, 85));
            toLabel.setBounds(280, 135, 100, 20);
            add(toLabel);

            RoundedFilterField createdToField = new RoundedFilterField();
            createdToField.setBounds(280, 160, 240, 35);
            createdToField.setBackground(new Color(218, 186, 121));
            createdToField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            createdToField.setForeground(Color.BLACK);
            add(createdToField);

            // Min Balance
            JLabel minBalanceLabel = new JLabel("Min Balance:");
            minBalanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            minBalanceLabel.setForeground(new Color(30, 50, 85));
            minBalanceLabel.setBounds(30, 210, 100, 20);
            add(minBalanceLabel);

            RoundedFilterField minBalanceField = new RoundedFilterField();
            minBalanceField.setBounds(30, 235, 220, 35);
            minBalanceField.setBackground(new Color(218, 186, 121));
            minBalanceField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            minBalanceField.setForeground(Color.BLACK);
            add(minBalanceField);

            // Max Balance Field
            JLabel maxBalanceLabel = new JLabel("Max Balance:");
            maxBalanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            maxBalanceLabel.setForeground(new Color(30, 50, 85));
            maxBalanceLabel.setBounds(280, 210, 100, 20);
            add(maxBalanceLabel);

            RoundedFilterField maxBalanceField = new RoundedFilterField();
            maxBalanceField.setBounds(280, 235, 240, 35);
            maxBalanceField.setBackground(new Color(218, 186, 121));
            maxBalanceField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            maxBalanceField.setForeground(Color.BLACK);
            add(maxBalanceField);

            // Address Field
            JLabel addressLabel = new JLabel("Address:");
            addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            addressLabel.setForeground(new Color(30, 50, 85));
            addressLabel.setBounds(30, 285, 100, 20);
            add(addressLabel);

            RoundedFilterField addressField = new RoundedFilterField();
            addressField.setBounds(30, 310, 490, 35);
            addressField.setBackground(new Color(218, 186, 121));
            addressField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            addressField.setForeground(Color.BLACK);
            add(addressField);

            // Info text
            JLabel infoLabel = new JLabel("Date format: YYYY-MM-DD. Leave empty to skip filter.");
            infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            infoLabel.setForeground(new Color(100, 100, 100));
            infoLabel.setBounds(30, 355, 490, 15);
            add(infoLabel);

            // Cancel Button (Light blue/gray)
            RoundedButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBounds(220, 375, 120, 38);
            cancelBtn.setBackground(new Color(108, 130, 173));
            cancelBtn.setForeground(Color.BLACK);
            cancelBtn.addActionListener(e -> dispose());
            add(cancelBtn);

            // OK Button (Dark blue/navy with white text)
            RoundedButton okBtn = new RoundedButton("OK");
            okBtn.setBounds(360, 375, 120, 38);
            okBtn.setBackground(new Color(8, 25, 64));
            okBtn.setForeground(Color.WHITE);
            okBtn.addActionListener(e -> dispose());
            add(okBtn);
            
            setVisible(true);
        }
        class RoundedFilterField extends JTextField {
            public RoundedFilterField() {
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g);
            }
        }
    }

    public static void main(String[] args) {
        new HomePageAdminAccount();
    }
}