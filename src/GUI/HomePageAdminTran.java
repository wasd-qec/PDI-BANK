package GUI;

import javax.swing.*;
import java.awt.*;

public class HomePageAdminTran extends JFrame {

    public HomePageAdminTran() {
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
        ImageIcon logo = new ImageIcon("PDI-BANK/src/GUI/TMB_Logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(45, 40, 110, 110);
        sidePanel.add(logoLabel);

        // ACCOUNT BUTTON
        RoundedButton accountBtn = new RoundedButton("Account");
        accountBtn.setBounds(28, 200, 145, 40);
        accountBtn.setBackground(new Color(108, 130, 173));
        sidePanel.add(accountBtn);

        // TRANSACTION BUTTON
        RoundedButton transactionBtn = new RoundedButton("Transaction");
        transactionBtn.setBounds(28, 250, 145, 40);
        transactionBtn.setBackground(new Color(218, 186, 121));
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

        // TOP BUTTONS
        RoundedButton createAccBtn = new RoundedButton("Create Transaction");
        createAccBtn.setBounds(240, 80, 200, 40);
        createAccBtn.setBackground(new Color(218, 186, 121));
        add(createAccBtn);

        // open popup
        createAccBtn.addActionListener(e -> new TransactionTypeModal(this));

        RoundedButton filterBtn = new RoundedButton("Filter by");
        filterBtn.setBounds(450, 80, 120, 40);
        filterBtn.setBackground(new Color(218, 186, 121));
        add(filterBtn);

        // ========== ACCOUNTS TITLE ==========
        JLabel accLabel = new JLabel("Transactions");
        accLabel.setForeground(Color.WHITE);
        accLabel.setFont(new Font("Serif", Font.BOLD, 20));
        accLabel.setBounds(240, 130, 200, 40);
        add(accLabel);

        // ========== TRANSACTION BOXES ==========
        String[][] transactions = {
            {"Withdrawal", "TXN-dhsasffb", "ABC003", "-$5000", "2026-01-12  16:29:13", "Receiver: ABC001"},
            {"Deposit", "TXN-9F01296", "ABC001", "+$5000", "2026-01-07  16:29:13", "Receiver: ABC001"},
            {"Deposit", "TXN-17616E2D", "ABC001", "+$1000", "2026-01-01  23:23:03", "Receiver: ABC003"},
            {"Withdrawal", "TXN-21FE1BF5", "ABC001", "-$1200", "2026-01-01  00:41:25", "Receiver: ABC001"},
            {"Transfer", "TXN-TRANSFER1", "ABC002", "-$1299", "2026-01-02  14:30:00", "Receiver: ABC004"}
        };

        int y = 170;
        for (String[] trans : transactions) {
            RoundedPanel accBox = new RoundedPanel(20);
            accBox.setBackground(new Color(235, 235, 235));
            accBox.setBounds(240, y, 710, 60);
            accBox.setLayout(null);
            add(accBox);
            
            JLabel typeLabel = new JLabel(trans[0]);
            typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            typeLabel.setForeground(new Color(30, 50, 85));
            typeLabel.setBounds(20, 8, 100, 18);
            accBox.add(typeLabel);
            
            JLabel txnIdLabel = new JLabel("Transaction ID: " + trans[1]);
            txnIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            txnIdLabel.setForeground(new Color(80, 80, 80));
            txnIdLabel.setBounds(20, 28, 250, 13);
            accBox.add(txnIdLabel);
            
            JLabel senderLabel = new JLabel("Sender: " + trans[2]);
            senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            senderLabel.setForeground(new Color(80, 80, 80));
            senderLabel.setBounds(20, 42, 250, 13);
            accBox.add(senderLabel);
            
            JLabel amountLabel = new JLabel(trans[3]);
            amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            // Color based on + or -
            if (trans[3].startsWith("+")) {
                amountLabel.setForeground(new Color(34, 139, 34)); // Green for deposit
            } else {
                amountLabel.setForeground(new Color(220, 20, 60)); // Red for withdrawal
            }
            
            amountLabel.setBounds(630, 8, 70, 18);
            accBox.add(amountLabel);
            
            JLabel dateLabel = new JLabel(trans[4]);
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            dateLabel.setForeground(new Color(100, 100, 100));
            dateLabel.setBounds(540, 28, 170, 12);
            accBox.add(dateLabel);
            
            JLabel receiverLabel = new JLabel(trans[5]);
            receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            receiverLabel.setForeground(new Color(100, 100, 100));
            receiverLabel.setBounds(540, 42, 170, 12);
            accBox.add(receiverLabel);
            
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

        filterBtn.addActionListener(e -> {
            new FilterTransactionsDialog(this);
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

    class FilterTransactionsDialog extends JDialog {
    
    public FilterTransactionsDialog(JFrame parent) {
        super(parent, "Filter Transactions", true);
        setSize(550, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 240, 235));
        
        // Title
        JLabel titleLabel = new JLabel("Filter Transactions By:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(30, 20, 300, 25);
        add(titleLabel);
        
        // Min Amount
        JLabel minAmountLabel = new JLabel("Min Amount:");
        minAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        minAmountLabel.setForeground(new Color(30, 50, 85));
        minAmountLabel.setBounds(30, 60, 100, 20);
        add(minAmountLabel);

        RoundedFilterField minAmountField = new RoundedFilterField();
        minAmountField.setBounds(30, 85, 220, 35);
        minAmountField.setBackground(new Color(218, 186, 121));
        minAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        minAmountField.setForeground(Color.BLACK);
        add(minAmountField);

        // Max Amount
        JLabel maxAmountLabel = new JLabel("Max Amount:");
        maxAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        maxAmountLabel.setForeground(new Color(30, 50, 85));
        maxAmountLabel.setBounds(280, 60, 100, 20);
        add(maxAmountLabel);

        RoundedFilterField maxAmountField = new RoundedFilterField();
        maxAmountField.setBounds(280, 85, 240, 35);
        maxAmountField.setBackground(new Color(218, 186, 121));
        maxAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        maxAmountField.setForeground(Color.BLACK);
        add(maxAmountField);
        
        // Type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(new Color(30, 50, 85));
        typeLabel.setBounds(30, 135, 100, 20);
        add(typeLabel);

        String[] typeOptions = {"All", "Deposit", "Withdrawal", "Transfer"};
        JComboBox<String> typeCombo = new JComboBox<>(typeOptions);
        typeCombo.setBounds(30, 160, 220, 35);
        typeCombo.setBackground(new Color(218, 186, 121));
        typeCombo.setForeground(Color.BLACK);
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(typeCombo);

        // Sender ID
        JLabel senderLabel = new JLabel("Sender ID:");
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        senderLabel.setForeground(new Color(30, 50, 85));
        senderLabel.setBounds(280, 210, 100, 20);
        add(senderLabel);

        RoundedFilterField senderField = new RoundedFilterField();
        senderField.setBounds(280, 235, 240, 35);
        senderField.setBackground(new Color(218, 186, 121));
        senderField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        senderField.setForeground(Color.BLACK);
        add(senderField);

        // Receiver ID
        JLabel receiverLabel = new JLabel("Receiver ID:");
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        receiverLabel.setForeground(new Color(30, 50, 85));
        receiverLabel.setBounds(30, 210, 100, 20);
        add(receiverLabel);

        RoundedFilterField receiverField = new RoundedFilterField();
        receiverField.setBounds(30, 235, 220, 35);
        receiverField.setBackground(new Color(218, 186, 121));
        receiverField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        receiverField.setForeground(Color.BLACK);
        add(receiverField);

        // Date From
        JLabel dateFromLabel = new JLabel("Date From:");
        dateFromLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateFromLabel.setForeground(new Color(30, 50, 85));
        dateFromLabel.setBounds(30, 285, 100, 20);
        add(dateFromLabel);

        RoundedFilterField dateFromField = new RoundedFilterField();
        dateFromField.setBounds(280, 310, 240, 35);
        dateFromField.setBackground(new Color(218, 186, 121));
        dateFromField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateFromField.setForeground(Color.BLACK);
        add(dateFromField);

        // Date To
        JLabel dateToLabel = new JLabel("Date To:");
        dateToLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateToLabel.setForeground(new Color(30, 50, 85));
        dateToLabel.setBounds(280, 285, 100, 20);
        add(dateToLabel);

        RoundedFilterField dateToField = new RoundedFilterField();
        dateToField.setBounds(30, 310, 220, 35);
        dateToField.setBackground(new Color(218, 186, 121));
        dateToField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateToField.setForeground(Color.BLACK);
        add(dateToField);

        // Info text
        JLabel infoLabel = new JLabel("Date format: YYYY-MM-DD. Leave empty to skip filter.");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setBounds(30, 355, 490, 15);
        add(infoLabel);

        // Cancel Button
        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBounds(220, 375, 120, 38);
        cancelBtn.setBackground(new Color(108, 130, 173));
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        // OK Button
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