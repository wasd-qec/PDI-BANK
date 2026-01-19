import javax.swing.*;
import java.awt.*;

public class HomePageCustomer extends JFrame {

    private RoundedButton withdrawBtn;

    public HomePageCustomer() {
        setTitle("Home Page");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(30, 50, 85));

        // SIDE BAR
        JPanel sideBar = new JPanel(null);
        sideBar.setBounds(0, 0, 200, 600);
        sideBar.setBackground(new Color(8, 25, 64));
        add(sideBar);

        ImageIcon logo = new ImageIcon("PDI-BANK/src/GUI/TMB_Logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(45, 40, 110, 110);
        sideBar.add(logoLabel);

        RoundedButton AccBT = new RoundedButton("Account Detail");
        AccBT.addActionListener(e -> new AccountDetailCustomer(this));
        AccBT.setBounds(30, 180, 140, 35);
        AccBT.setBackground(new Color(180, 190, 210));
        AccBT.setForeground(new Color(20, 30, 60));
        sideBar.add(AccBT);

        RoundedButton deactivateBT = new RoundedButton("Deactivate");
        deactivateBT.addActionListener(e -> new DeactivateAccCustomer(this));
        deactivateBT.setBounds(30, 225, 140, 35);
        deactivateBT.setBackground(new Color(180, 190, 210));
        deactivateBT.setForeground(new Color(20, 30, 60));
        sideBar.add(deactivateBT);

        RoundedButton reportBT = new RoundedButton("Report");
        reportBT.addActionListener(e -> new ReportCustomer(this));

        reportBT.setBounds(30, 270, 140, 35);
        reportBT.setBackground(new Color(180, 190, 210));
        reportBT.setForeground(new Color(20, 30, 60));
        sideBar.add(reportBT);

        JLabel logoutLabel = new JLabel("Log out");
        logoutLabel.setForeground(Color.WHITE);
        logoutLabel.setBounds(75, 520, 110, 40);
        sideBar.add(logoutLabel);

        logoutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LogOutCustomer(HomePageCustomer.this);
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

        RoundedButton depositBtn = new RoundedButton("Deposit");
        depositBtn.setBackground(new Color(218, 186, 121));
        depositBtn.setBounds(260, 150, 180, 50);
        depositBtn.addActionListener(e -> new DepositCustomerPopup(this));
        add(depositBtn);

        withdrawBtn = new RoundedButton("Withdraw");
        withdrawBtn.setBackground(new Color(218, 186, 121));
        withdrawBtn.setBounds(460, 150, 180, 50);
        withdrawBtn.addActionListener(e -> new WithdrawCustomerPopup(this));
        add(withdrawBtn);

        RoundedButton transferBtn = new RoundedButton("Transfer");
        transferBtn.setBackground(new Color(218, 186, 121));
        transferBtn.setBounds(660, 150, 180, 50);
        transferBtn.addActionListener(e -> new TransferCustomer(this));
        add(transferBtn);

        JLabel recentLabel = new JLabel("Recent Transactions");
        recentLabel.setForeground(Color.WHITE);
        recentLabel.setFont(new Font("Serif", Font.BOLD, 16));
        recentLabel.setBounds(230, 230, 300, 30);
        add(recentLabel);

        for (int i = 0; i < 3; i++) {
            RoundedPanel box = new RoundedPanel(20);
            box.setBackground(new Color(213, 197, 186));
            box.setBounds(230, 270 + (i * 65), 670, 50);
            add(box);
        }

        setVisible(true);
    }

    // ================= Rounded UI Components =================

    class RoundedPanel extends JPanel {
        private int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
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

    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
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

    public static void main(String[] args) {
        new HomePageCustomer();
    }
}
