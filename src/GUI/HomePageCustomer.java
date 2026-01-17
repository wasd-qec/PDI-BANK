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

        ImageIcon logo = new ImageIcon("PDI-BANK/src/GUI/TMB_Logo.png");
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
        add(depositBtn);

        RoundedButton withdrawBtn = new RoundedButton("Withdraw");
        withdrawBtn.setBackground(new Color(218, 186, 121));
        withdrawBtn.setBounds(460, 150, 180, 50);
        add(withdrawBtn);

        RoundedButton transferBtn = new RoundedButton("Transfer");
        transferBtn.setBackground(new Color(218, 186, 121));
        transferBtn.setBounds(660, 150, 180, 50);
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
