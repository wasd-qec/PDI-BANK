import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        sideBar.add(AccBT);

        RoundedButton deactivateBT = new RoundedButton("Deactivate");
        deactivateBT.setBounds(30, 225, 140, 35);
        sideBar.add(deactivateBT);

        RoundedButton reportBT = new RoundedButton("Report");
        reportBT.setBounds(30, 270, 140, 35);
        sideBar.add(reportBT);

        RoundedButton transBT = new RoundedButton("Transaction");
        transBT.setBounds(30, 315, 140, 35);
        sideBar.add(transBT);

        JLabel logoutLabel = new JLabel("Log out");
        logoutLabel.setForeground(Color.WHITE);
        logoutLabel.setBounds(30, 520, 100, 30);
        sideBar.add(logoutLabel);

        logoutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginSelection();
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
            cancelBtn.setBounds(70, isTransfer ? 240 : 180, 110, 35);
            cancelBtn.addActionListener(e -> dispose());
            add(cancelBtn);

            RoundedButton actionBtn = new RoundedButton(type);
            actionBtn.setBounds(220, isTransfer ? 240 : 180, 110, 35);
            actionBtn.addActionListener(e -> {
                dispose();
                showSuccessMessage(type);
            });
            add(actionBtn);
        }
    }

    private void showSuccessMessage(String type) {
        JDialog successDialog = new JDialog(this, type, true);
        successDialog.setSize(350, 120);
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