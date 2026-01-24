package GUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import Database.ReportForAdmin;

public class AdminReportDialog extends JDialog {
    private ReportForAdmin report;
    private JLabel depVal;
    private JLabel wdrVal;
    private JLabel tinVal;
    private JSpinner startSpinner;
    private JSpinner endSpinner;
    private SimpleDateFormat dateFmt;

    public AdminReportDialog(JFrame parent) {
        super(parent, "Transaction Report", true);
        setSize(700, 570);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 50, 85));
        setLayout(null);

        this.report = new ReportForAdmin();
        this.dateFmt = new SimpleDateFormat("yyyy-MM-dd");

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("Transaction Report");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 20, 300, 30);
        add(titleLabel);

        // Get initial data
        double totalDeposit = report.getTotalDeposit();
        double totalWithdrawal = report.getTotalWithdrawal();
        double totalTransfer = report.getTotalTransfer();

        int totalUsers = report.getTotalUsers();
        int DeactivatedUsers = report.getDeactivatedUsers();
        int ActiveUsers = report.getActiveUsers();

        // Date range pickers (start / end)
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = cal.getTime();

        startSpinner = new JSpinner(new SpinnerDateModel(startDate, null, null, Calendar.DAY_OF_MONTH));
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd"));
        startSpinner.setBounds(30, 60, 150, 25);
        add(startSpinner);

        endSpinner = new JSpinner(new SpinnerDateModel(endDate, null, null, Calendar.DAY_OF_MONTH));
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd"));
        endSpinner.setBounds(200, 60, 150, 25);
        add(endSpinner);

        RoundedButton applyBtn = new RoundedButton("Apply");
        applyBtn.setBounds(370, 60, 100, 25);
        add(applyBtn);

        // Summary Panel
        RoundedPanel summaryPanel = new RoundedPanel(12);
        summaryPanel.setBackground(new Color(245, 240, 235));
        summaryPanel.setBounds(30, 110, 640, 70);
        summaryPanel.setLayout(null);
        add(summaryPanel);

        JLabel inLabel = new JLabel("Active Users");
        inLabel.setFont(new Font("Serif", Font.BOLD, 14));
        inLabel.setForeground(new Color(30, 50, 85));
        inLabel.setBounds(20, 12, 120, 20);
        summaryPanel.add(inLabel);

        JLabel inValue = new JLabel(String.format("%d", ActiveUsers));
        inValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inValue.setForeground(new Color(34, 139, 34));
        inValue.setBounds(20, 34, 200, 20);
        summaryPanel.add(inValue);

        JLabel outLabel = new JLabel("Deactivated Users");
        outLabel.setFont(new Font("Serif", Font.BOLD, 14));
        outLabel.setForeground(new Color(30, 50, 85));
        outLabel.setBounds(240, 12, 120, 20);
        summaryPanel.add(outLabel);

        JLabel outValue = new JLabel(String.format("%d", DeactivatedUsers));
        outValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        outValue.setForeground(new Color(220, 20, 60));
        outValue.setBounds(240, 34, 200, 20);
        summaryPanel.add(outValue);

        JLabel balLabel = new JLabel("Total Users");
        balLabel.setFont(new Font("Serif", Font.BOLD, 14));
        balLabel.setForeground(new Color(30, 50, 85));
        balLabel.setBounds(460, 12, 140, 20);
        summaryPanel.add(balLabel);

        JLabel balValue = new JLabel(String.format("%d", totalUsers));
        balValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balValue.setForeground(new Color(30, 50, 85));
        balValue.setBounds(460, 34, 160, 20);
        summaryPanel.add(balValue);

        // Values Panel
        JPanel valuesPanel = new JPanel(null);
        valuesPanel.setBackground(new Color(245, 240, 235));
        valuesPanel.setBounds(30, 190, 640, 220);
        add(valuesPanel);

        JLabel depLabel = new JLabel("Total Deposits:");
        depLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        depLabel.setForeground(new Color(30, 50, 85));
        depLabel.setBounds(20, 20, 200, 20);
        valuesPanel.add(depLabel);

        depVal = new JLabel(String.format("$%.2f", totalDeposit));
        depVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        depVal.setForeground(new Color(34, 139, 34));
        depVal.setBounds(220, 20, 200, 20);
        valuesPanel.add(depVal);

        JLabel wdrLabel = new JLabel("Total Withdrawals:");
        wdrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        wdrLabel.setForeground(new Color(30, 50, 85));
        wdrLabel.setBounds(20, 55, 200, 20);
        valuesPanel.add(wdrLabel);

        wdrVal = new JLabel(String.format("$%.2f", totalWithdrawal));
        wdrVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        wdrVal.setForeground(new Color(220, 20, 60));
        wdrVal.setBounds(220, 55, 200, 20);
        valuesPanel.add(wdrVal);

        JLabel tinLabel = new JLabel("Transfer :");
        tinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tinLabel.setForeground(new Color(30, 50, 85));
        tinLabel.setBounds(20, 90, 200, 20);
        valuesPanel.add(tinLabel);

        tinVal = new JLabel(String.format("$%.2f", totalTransfer));
        tinVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tinVal.setForeground(new Color(34, 139, 34));
        tinVal.setBounds(220, 90, 200, 20);
        valuesPanel.add(tinVal);

        JLabel balLabel2 = new JLabel("Current Balance:");
        balLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        balLabel2.setForeground(new Color(30, 50, 85));
        balLabel2.setBounds(20, 160, 200, 20);
        valuesPanel.add(balLabel2);

        JLabel balVal2 = new JLabel(String.format("$%.2f", report.getTotalBalance()));
        balVal2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balVal2.setForeground(new Color(30, 50, 85));
        balVal2.setBounds(220, 160, 200, 20);
        valuesPanel.add(balVal2);

        // Apply button listener
        applyBtn.addActionListener(e -> {
            Date s = (Date) startSpinner.getValue();
            Date en = (Date) endSpinner.getValue();
            String sStr = dateFmt.format(s) + " 00:00:00";
            String eStr = dateFmt.format(en) + " 23:59:59";

            double depR = report.getTotalDeposit(sStr, eStr);
            double wdrR = report.getTotalWithdrawal(sStr, eStr);
            double tinR = report.getTotalTransfer(sStr, eStr);

            depVal.setText(String.format("$%.2f", depR));
            wdrVal.setText(String.format("$%.2f", wdrR));
            tinVal.setText(String.format("$%.2f", tinR));
        });

        // Close Button
        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.setBounds(300, 430, 100, 35);
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn);
    }

    // ========== RoundedPanel ==========
    private static class RoundedPanel extends JPanel {
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
    private static class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setOpaque(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.WHITE);
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
}