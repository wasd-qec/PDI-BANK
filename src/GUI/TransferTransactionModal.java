package GUI;

import javax.swing.*;
import java.awt.*;
import Object.Customer;
import Object.Transaction;
import Database.CustomerHandling;
import Database.TransactionImplement;
import Database.TransactionInterface;
import Service.TransactionService;

public class TransferTransactionModal extends JDialog {
    private CustomerHandling customerHandling = new CustomerHandling();
    private TransactionInterface transactionHandling = new TransactionImplement();
    private TransactionService transactionService;
    private JFrame parentFrame;

    public TransferTransactionModal(JFrame parent) {
        super(parent, false);
        this.parentFrame = parent;
        this.transactionService = new TransactionService(customerHandling, transactionHandling);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 520);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 550, 520, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setBounds(0, 0, 550, 520);
        add(panel);

        // ----- Title -----
        JLabel title = new JLabel("Creating Transfer Transaction", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(0, 35, 550, 30);
        panel.add(title);

        // Divider line
        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(40, 80, 470, 1);
        panel.add(line);

        // ----- Sender Account label -----
        JLabel senderLabel = new JLabel("Sender Account Number");
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        senderLabel.setForeground(new Color(10, 32, 68));
        senderLabel.setBounds(50, 100, 400, 25);
        panel.add(senderLabel);

        JTextField senderField = styledInput("Enter sender account number");
        senderField.setBounds(50, 130, 450, 45);
        panel.add(senderField);

        // ----- Receiver Account label -----
        JLabel receiverLabel = new JLabel("Receiver Account Number");
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        receiverLabel.setForeground(new Color(10, 32, 68));
        receiverLabel.setBounds(50, 190, 400, 25);
        panel.add(receiverLabel);

        JTextField receiverField = styledInput("Enter receiver account number");
        receiverField.setBounds(50, 220, 450, 45);
        panel.add(receiverField);

        // ----- Amount label -----
        JLabel amtLabel = new JLabel("Amount");
        amtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amtLabel.setForeground(new Color(10, 32, 68));
        amtLabel.setBounds(50, 280, 200, 25);
        panel.add(amtLabel);

        JTextField amtField = styledInput("Enter amount");
        amtField.setBounds(50, 310, 450, 45);
        panel.add(amtField);

        // ----- Buttons -----
        JButton cancelBtn = styledDarkBtn("Cancel");
        cancelBtn.setBounds(180, 400, 90, 40);
        cancelBtn.addActionListener(e -> dispose());
        panel.add(cancelBtn);

        JButton createBtn = styledDarkBtn("Create");
        createBtn.setBounds(290, 400, 90, 40);
        panel.add(createBtn);

        createBtn.addActionListener(e -> {
            String senderAccNo = senderField.getText().trim();
            String receiverAccNo = receiverField.getText().trim();
            String amountStr = amtField.getText().trim();
            
            if (senderAccNo.isEmpty() || senderAccNo.equals("Enter sender account number")) {
                JOptionPane.showMessageDialog(this, "Please enter sender account number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (receiverAccNo.isEmpty() || receiverAccNo.equals("Enter receiver account number")) {
                JOptionPane.showMessageDialog(this, "Please enter receiver account number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (senderAccNo.equals(receiverAccNo)) {
                JOptionPane.showMessageDialog(this, "Sender and receiver cannot be the same account.", "Error", JOptionPane.ERROR_MESSAGE);
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
                
                Customer sender = customerHandling.getCustomerByAccNo(senderAccNo);
                if (sender == null) {
                    JOptionPane.showMessageDialog(this, "Sender account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!sender.isActive()) {
                    JOptionPane.showMessageDialog(this, "Sender account is deactivated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Customer receiver = customerHandling.getCustomerByAccNo(receiverAccNo);
                if (receiver == null) {
                    JOptionPane.showMessageDialog(this, "Receiver account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!receiver.isActive()) {
                    JOptionPane.showMessageDialog(this, "Receiver account is deactivated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount > sender.getBalance()) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance. Sender balance: $" + String.format("%.2f", sender.getBalance()), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Transaction result = transactionService.processTransfer(sender, receiver, amount);
                if (result != null) {
                    dispose();
                    new TransactionSuccessPopup(parentFrame);
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
        toFront();
        requestFocus();
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
        JButton btn = new JButton(text);
        btn.setBackground(new Color(8, 25, 64));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return btn;
    }
}
