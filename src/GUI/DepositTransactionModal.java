package GUI;

import javax.swing.*;
import java.awt.*;
import Object.Customer;
import Object.Transaction;
import Database.CustomerHandling;
import Database.TransactionImplement;
import Database.TransactionInterface;
import Service.TransactionService;

public class DepositTransactionModal extends JDialog {
    private CustomerHandling customerHandling = new CustomerHandling();
    private TransactionInterface transactionHandling = new TransactionImplement();
    private TransactionService transactionService;
    private JFrame parentFrame;

    public DepositTransactionModal(JFrame parent) {
        super(parent, false);
        this.parentFrame = parent;
        this.transactionService = new TransactionService(customerHandling, transactionHandling);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 420);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));
        
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 550, 420, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setBounds(0, 0, 550, 420);
        add(panel);

        // ----- Title -----
        JLabel title = new JLabel("Creating Deposit Transaction", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(0, 35, 550, 30);
        panel.add(title);

        // Divider line
        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(40, 80, 470, 1);
        panel.add(line);

        // ----- Recipient account label -----
        JLabel recLabel = new JLabel("Account Number");
        recLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recLabel.setForeground(new Color(10, 32, 68));
        recLabel.setBounds(50, 100, 400, 25);
        panel.add(recLabel);

        JTextField recField = styledInput("Enter account number");
        recField.setBounds(50, 130, 450, 45);
        panel.add(recField);

        // ----- Amount label -----
        JLabel amtLabel = new JLabel("Amount");
        amtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amtLabel.setForeground(new Color(10, 32, 68));
        amtLabel.setBounds(50, 190, 200, 25);
        panel.add(amtLabel);

        JTextField amtField = styledInput("Enter amount");
        amtField.setBounds(50, 220, 450, 45);
        panel.add(amtField);

        // ----- Buttons row -----
        JButton cancelBtn = styledDarkBtn("Cancel");
        cancelBtn.setBackground(new Color(108, 130, 173));
        cancelBtn.setBounds(180, 300, 90, 40);
        cancelBtn.addActionListener(e -> dispose());
        panel.add(cancelBtn);

        JButton createBtn = styledDarkBtn("Create");
        createBtn.setBackground(new Color(8, 25, 64));
        createBtn.setBounds(290, 300, 90, 40);
        panel.add(createBtn);

        createBtn.addActionListener(e -> {
            String accNo = recField.getText().trim();
            String amountStr = amtField.getText().trim();
            
            if (accNo.isEmpty() || accNo.equals("Enter account number")) {
                JOptionPane.showMessageDialog(this, "Please enter account number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (amountStr.isEmpty() || amountStr.equals("Enter amount")) {
                JOptionPane.showMessageDialog(this, "Please enter amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Customer customer = customerHandling.getCustomerByAccNo(accNo);
                if (customer == null) {
                    JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!customer.isActive()) {
                    JOptionPane.showMessageDialog(this, "Account is deactivated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Transaction result = transactionService.DepositService(customer, amount);
                if (result != null) {
                    dispose();
                    new TransactionSuccessPopup(parentFrame);
                    // Refresh the parent if it's HomePageAdminTran
                    if (parentFrame instanceof HomePageAdminTran) {
                        ((HomePageAdminTran) parentFrame).refreshTransactions();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Transaction failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private JTextField styledInput(String placeholder) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setForeground(Color.DARK_GRAY);
        field.setBackground(new Color(218, 186, 121));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setOpaque(true);

        field.setText(placeholder);
        field.setForeground(new Color(80, 80, 80));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(80, 80, 80));
                }
            }
        });

        return field;
    }

    private JButton styledDarkBtn(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }
}
