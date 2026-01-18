package GUI;
import javax.swing.*;
import java.awt.*;

public class HomePageCustomer extends JFrame {

    public HomePageCustomer() {
        setTitle("Home Page");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(30, 50, 85));

        // SIDE PANEL
        JPanel sideBar = new JPanel();
        sideBar.setBounds(0, 0, 200, 600);
        sideBar.setBackground(new Color(8, 25, 64));
        sideBar.setLayout(null);
        add(sideBar);

        ImageIcon logo = new ImageIcon("TMB_Logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(45, 40, 110, 110);
        sideBar.add(logoLabel);

        // MENU BUTTONS
        RoundedButton AccBT = new RoundedButton("Account Detail");
        AccBT.setBounds(30, 180, 140, 35);
        AccBT.addActionListener(e -> showAccountDetailDialog());
        sideBar.add(AccBT);

        RoundedButton deactivateBT = new RoundedButton("Deactivate");
        deactivateBT.setBounds(30, 225, 140, 35);
        deactivateBT.addActionListener(e -> showDeactivateDialog());
        sideBar.add(deactivateBT);

        RoundedButton reportBT = new RoundedButton("Report");
        reportBT.setBounds(30, 270, 140, 35);
        reportBT.addActionListener(e -> showReportDialog());
        sideBar.add(reportBT);

        JLabel logoutLabel = new JLabel("Log out");
        logoutLabel.setForeground(Color.WHITE);
        logoutLabel.setBounds(30, 520, 100, 30);
        sideBar.add(logoutLabel);

        logoutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showLogoutDialog();
            }
        });

        RoundedPanel balanceBox = new RoundedPanel(25);
        balanceBox.setBackground(new Color(8, 25, 64));
        balanceBox.setBounds(230, 40, 670, 80);
        balanceBox.setLayout(null);
        add(balanceBox);

        JLabel balanceText = new JLabel("Balance");
        balanceText.setForeground(Color.WHITE);
        balanceText.setFont(new Font("Serif", Font.BOLD, 22));
        balanceText.setBounds(20, 25, 200, 30);
        balanceBox.add(balanceText);

        JLabel amountText = new JLabel("$XXXXX.XX");
        amountText.setForeground(Color.WHITE);
        amountText.setFont(new Font("Serif", Font.BOLD, 22));
        amountText.setBounds(500, 25, 200, 30);
        balanceBox.add(amountText);

        // ACTION BUTTONS
        RoundedButton depositBtn = new RoundedButton("Deposit");
        depositBtn.setBackground(new Color(218, 186, 121));
        depositBtn.setBounds(260, 150, 180, 50);
        depositBtn.addActionListener(e -> showDepositDialog());
        add(depositBtn);

        RoundedButton withdrawBtn = new RoundedButton("Withdraw");
        withdrawBtn.setBackground(new Color(218, 186, 121));
        withdrawBtn.setBounds(460, 150, 180, 50);
        withdrawBtn.addActionListener(e -> showWithdrawDialog());
        add(withdrawBtn);

        RoundedButton transferBtn = new RoundedButton("Transfer");
        transferBtn.setBackground(new Color(218, 186, 121));
        transferBtn.setBounds(660, 150, 180, 50);
        transferBtn.addActionListener(e -> showTransferDialog());
        add(transferBtn);

        JLabel recentLabel = new JLabel("Recent Transactions");
        recentLabel.setForeground(Color.WHITE);
        recentLabel.setFont(new Font("Serif", Font.BOLD, 16));
        recentLabel.setBounds(230, 230, 300, 30);
        add(recentLabel);

        // TRANSACTION BOXES
        for (int i = 0; i < 3; i++) {
            RoundedPanel box = new RoundedPanel(20);
            box.setBackground(new Color(213, 197, 186));
            box.setBounds(230, 270 + (i * 65), 670, 50);
            add(box);
        }

        setVisible(true);
    }

    private void showDepositDialog() {
        TransactionDialog dialog = new TransactionDialog(this, "Deposit", false);
        dialog.setVisible(true);
    }

    private void showWithdrawDialog() {
        TransactionDialog dialog = new TransactionDialog(this, "Withdraw", false);
        dialog.setVisible(true);
    }

    private void showTransferDialog() {
        TransactionDialog dialog = new TransactionDialog(this, "Transfer", true);
        dialog.setVisible(true);
    }

    private void showAccountDetailDialog() {
        JDialog detailDialog = new JDialog(this, "Account Detail", true);
        detailDialog.setSize(350, 300);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setResizable(false);
        detailDialog.getContentPane().setBackground(new Color(245, 240, 235));
        detailDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Account Detail");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 150, 25);
        detailDialog.add(titleLabel);

        String[][] details = {
            {"Account number", "ABC001"},
            {"Customer ID", "ABC001"},
            {"Name", "Hteng"},
            {"Phone Number", "5551234567"},
            {"Address", "100 Bank Street, New York"},
            {"Birth Date", "1985-06-20"},
            {"Account Created", "2025-03-01"},
            {"Balance", "$33,501"},
            {"Status", "Active"}
        };

        int yPos = 55;
        for (String[] detail : details) {
            JLabel labelName = new JLabel(detail[0]);
            labelName.setForeground(new Color(30, 50, 85));
            labelName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            labelName.setBounds(20, yPos, 150, 15);
            detailDialog.add(labelName);

            JLabel labelValue = new JLabel(detail[1]);
            labelValue.setForeground(new Color(218, 186, 121));
            labelValue.setFont(new Font("Segoe UI", Font.BOLD, 11));
            labelValue.setBounds(180, yPos, 140, 15);
            detailDialog.add(labelValue);

            yPos += 18;
        }

        detailDialog.setVisible(true);
    }

    private void showDeactivateDialog() {
        JDialog confirmDialog = new JDialog(this, "Deactivate Account", true);
        confirmDialog.setSize(390, 270);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setResizable(false);
        confirmDialog.getContentPane().setBackground(new Color(245, 240, 235));
        confirmDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Deactivate Account");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 250, 25);
        confirmDialog.add(titleLabel);

        RoundedPanel messagePanel = new RoundedPanel(15);
        messagePanel.setBackground(new Color(245, 230, 220));
        messagePanel.setBounds(20, 55, 340, 90);
        messagePanel.setLayout(null);
        confirmDialog.add(messagePanel);

        JLabel messageLabel = new JLabel("<html>Are you sure you want to deactivate your account?<br><br>This action cannot be undone.</html>");
        messageLabel.setForeground(new Color(30, 50, 85));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setBounds(15, 10, 310, 70);
        messagePanel.add(messageLabel);

        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBounds(70, 160, 100, 35);
        cancelBtn.addActionListener(e -> confirmDialog.dispose());
        confirmDialog.add(cancelBtn);

        RoundedButton confirmBtn = new RoundedButton("Confirm");
        confirmBtn.setBounds(200, 160, 100, 35);
        confirmBtn.addActionListener(e -> {
            confirmDialog.dispose();
            showDeactivateSuccessDialog();
        });
        confirmDialog.add(confirmBtn);

        confirmDialog.setVisible(true);
    }

    private void showDeactivateSuccessDialog() {
        JDialog successDialog = new JDialog(this, "Information", true);
        successDialog.setSize(320, 250);
        successDialog.setLocationRelativeTo(this);
        successDialog.setResizable(false);
        successDialog.getContentPane().setBackground(new Color(245, 240, 235));
        successDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Information");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 200, 25);
        successDialog.add(titleLabel);

        JLabel messageLabel = new JLabel("You have successfully deactivated your account.");
        messageLabel.setForeground(new Color(30, 50, 85));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setBounds(20, 55, 410, 100);
        successDialog.add(messageLabel);

        RoundedButton confirmBtn = new RoundedButton("Confirm");
        confirmBtn.setBounds(175, 165, 100, 35);
        confirmBtn.addActionListener(e -> {
            successDialog.dispose();
            dispose();
        });
        successDialog.add(confirmBtn);

        successDialog.setVisible(true);
    }

    private void showReportDialog() {
        JDialog reportDialog = new JDialog(this, "Report", true);
        reportDialog.setSize(360, 220);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setResizable(false);
        reportDialog.getContentPane().setBackground(new Color(245, 240, 235));
        reportDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Report");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 150, 25);
        reportDialog.add(titleLabel);

        JLabel inputLabel = new JLabel("Report");
        inputLabel.setForeground(new Color(30, 50, 85));
        inputLabel.setBounds(20, 55, 150, 20);
        reportDialog.add(inputLabel);

        JTextField reportField = new JTextField();
        reportField.setBounds(20, 80, 310, 35);
        reportField.setBackground(new Color(218, 186, 121));
        reportField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        reportField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportField.setText("Enter your report");
        reportDialog.add(reportField);

        RoundedButton submitBtn = new RoundedButton("Submit");
        submitBtn.setBounds(130, 130, 100, 35);
        submitBtn.addActionListener(e -> reportDialog.dispose());
        reportDialog.add(submitBtn);

        reportDialog.setVisible(true);
    }

    private void showTransactionDialog() {
        JDialog transDialog = new JDialog(this, "Transaction History", true);
        transDialog.setSize(400, 300);
        transDialog.setLocationRelativeTo(this);
        transDialog.setResizable(false);
        transDialog.getContentPane().setBackground(new Color(245, 240, 235));
        transDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 200, 25);
        transDialog.add(titleLabel);

        String[] columnNames = {"Date", "Type", "Amount"};
        Object[][] data = {
            {"2025-01-15", "Deposit", "+$500.00"},
            {"2025-01-10", "Withdrawal", "-$200.00"},
            {"2025-01-05", "Transfer", "-$100.00"},
            {"2024-12-28", "Deposit", "+$1000.00"},
            {"2024-12-20", "Withdrawal", "-$50.00"}
        };

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 55, 360, 200);
        transDialog.add(scrollPane);

        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.setBounds(160, 265, 100, 35);
        closeBtn.addActionListener(e -> transDialog.dispose());
        transDialog.add(closeBtn);

        transDialog.setVisible(true);
    }

    private void showLogoutDialog() {
        JDialog logoutDialog = new JDialog(this, "Logout", true);
        logoutDialog.setSize(380, 200);
        logoutDialog.setLocationRelativeTo(this);
        logoutDialog.setResizable(false);
        logoutDialog.getContentPane().setBackground(new Color(245, 240, 235));
        logoutDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Logout");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 150, 25);
        logoutDialog.add(titleLabel);

        JLabel messageLabel = new JLabel("Are you sure you want to log out?");
        messageLabel.setForeground(new Color(30, 50, 85));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setBounds(20, 55, 310, 30);
        logoutDialog.add(messageLabel);

        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBounds(60, 105, 100, 35);
        cancelBtn.addActionListener(e -> logoutDialog.dispose());
        logoutDialog.add(cancelBtn);

        RoundedButton confirmBtn = new RoundedButton("Confirm");
        confirmBtn.setBounds(190, 105, 100, 35);
        confirmBtn.addActionListener(e -> {
            logoutDialog.dispose();
            dispose();
        });
        logoutDialog.add(confirmBtn);

        logoutDialog.setVisible(true);
    }

    private void showSuccessMessage(String type) {
        JDialog successDialog = new JDialog(this, type, true);
        successDialog.setSize(300, 120);
        successDialog.setLocationRelativeTo(this);
        successDialog.setResizable(false);
        successDialog.getContentPane().setBackground(new Color(245, 240, 235));
        successDialog.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(type + " successful!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 16));
        messageLabel.setForeground(new Color(30, 50, 85));
        successDialog.add(messageLabel, BorderLayout.CENTER);

        Timer timer = new Timer(2000, e -> successDialog.dispose());
        timer.setRepeats(false);
        timer.start();

        successDialog.setVisible(true);
    }

    // Transaction Dialog
    class TransactionDialog extends JDialog {
        TransactionDialog(JFrame parent, String type, boolean isTransfer) {
            super(parent, type, true);
            setSize(400, isTransfer ? 320 : 270);
            setLocationRelativeTo(parent);
            setResizable(false);
            setLayout(null);
            getContentPane().setBackground(new Color(245, 240, 235));

            JLabel titleLabel = new JLabel(type);
            titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
            titleLabel.setForeground(new Color(30, 50, 85));
            titleLabel.setBounds(20, 20, 150, 25);
            add(titleLabel);

            int yPos = 55;

            if (isTransfer) {
                JLabel recipientLabel = new JLabel("Recipient Account number");
                recipientLabel.setForeground(new Color(30, 50, 85));
                recipientLabel.setBounds(20, yPos, 200, 20);
                add(recipientLabel);

                JTextField recipientField = new JTextField();
                recipientField.setBounds(20, yPos + 25, 340, 35);
                recipientField.setBackground(new Color(218, 186, 121));
                recipientField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                recipientField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                recipientField.setText("Enter recipient account number");
                add(recipientField);

                yPos += 70;
            }

            JLabel amountLabel = new JLabel("Amount");
            amountLabel.setForeground(new Color(30, 50, 85));
            amountLabel.setBounds(20, yPos, 150, 20);
            add(amountLabel);

            JTextField amountField = new JTextField();
            amountField.setBounds(20, yPos + 25, 340, 35);
            amountField.setBackground(new Color(218, 186, 121));
            amountField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            amountField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            amountField.setText("Enter amount");
            add(amountField);

            RoundedButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBounds(70, isTransfer ? 215 : 180, 110, 35);
            cancelBtn.addActionListener(e -> dispose());
            add(cancelBtn);

            RoundedButton actionBtn = new RoundedButton(type);
            actionBtn.setBounds(220, isTransfer ? 215 : 180, 110, 35);
            actionBtn.addActionListener(e -> {
                dispose();
                showSuccessMessage(type);
            });
            add(actionBtn);
        }
    }

    // Rounded Panel
    class RoundedPanel extends JPanel {
        private int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
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

    // Rounded Button
    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setBackground(new Color(108, 130, 173));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new HomePageCustomer();
    }
}