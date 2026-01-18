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

    class TransactionTypeModal extends JDialog {

        public TransactionTypeModal(JFrame parent) {
            super(parent, true);
            setSize(500, 300);
            setUndecorated(true);
            setLocationRelativeTo(parent);
            setBackground(new Color(0, 0, 0, 0)); // transparent

            // window shape (round)
            setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 500, 300, 35, 35));

            JPanel panel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(245, 238, 228)); // beige
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                }
            };
            panel.setBounds(0, 0, 500, 300);
            add(panel);

            // Title
            JLabel title = new JLabel("Transaction Type", SwingConstants.CENTER);
            title.setFont(new Font("Serif", Font.BOLD, 22));
            title.setBounds(0, 30, 500, 30);
            title.setForeground(new Color(10, 32, 68));
            panel.add(title);

            // Divider line
            JPanel line = new JPanel();
            line.setBackground(new Color(140, 140, 140));
            line.setBounds(30, 70, 440, 1);
            panel.add(line);

            // Buttons
            RoundedButton depositBtn = new RoundedButton("Deposit");
            depositBtn.setBounds(80, 110, 110, 40);
            depositBtn.setBackground(new Color(8, 25, 64));
            depositBtn.setForeground(Color.WHITE);
            panel.add(depositBtn);

            depositBtn.addActionListener(e -> {
                dispose();
                new DepositTransactionModal(parent);
            });

            RoundedButton withdrawBtn = new RoundedButton("Withdraw");
            withdrawBtn.setBounds(195, 110, 110, 40);
            withdrawBtn.setBackground(new Color(8, 25, 64));
            withdrawBtn.setForeground(Color.WHITE);
            panel.add(withdrawBtn);

            withdrawBtn.addActionListener(e -> {
                dispose();
                new WithdrawTransactionModal(parent);
            });

            RoundedButton transferBtn = new RoundedButton("Transfer");
            transferBtn.setBounds(310, 110, 110, 40);
            transferBtn.setBackground(new Color(8, 25, 64));
            transferBtn.setForeground(Color.WHITE);
            panel.add(transferBtn);

            transferBtn.addActionListener(e -> {
                dispose();
                new TransferTransactionModal(parent);
            });

            // Cancel label
            JLabel cancelLabel = new JLabel("Cancel", SwingConstants.CENTER);
            cancelLabel.setBounds(0, 200, 500, 25);
            cancelLabel.setForeground(new Color(10, 32, 68));
            cancelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel.add(cancelLabel);

            cancelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancelLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    dispose();
                }
            });

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new HomePageAdminAccount();
    }
}
