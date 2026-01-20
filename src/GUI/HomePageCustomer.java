package GUI;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import Object.Customer;
import Object.Transaction;
import Database.CustomerImple;
import Database.TransactionImple;
import Service.TransactionService;

public class HomePageCustomer extends JFrame {
    private Customer customer;
    private CustomerImple customerImple = new CustomerImple();
    private TransactionImple transactionImple = new TransactionImple();
    private TransactionService transactionService;
    private JLabel amountText;
    private JPanel transactionPanel;

    public HomePageCustomer(Customer customer) {
        this.customer = customer;
        this.transactionService = new TransactionService(customerImple, transactionImple);
        
        setTitle("Home Page - " + customer.getName());
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(30, 50, 85));

        // ========== LEFT SIDE PANEL ==========
        JPanel sideBar = new JPanel(null);
        sideBar.setBounds(0, 0, 200, 650);
        sideBar.setBackground(new Color(8, 25, 64));
        add(sideBar);

        // LOGO
        ImageIcon logo = new ImageIcon("Asset/TMB_Logo.png");
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

        amountText = new JLabel(String.format("$%.2f", customer.getBalance()));
        amountText.setForeground(Color.WHITE);
        amountText.setFont(new Font("Serif", Font.BOLD, 22));
        amountText.setBounds(450, 25, 200, 30);
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

        // TRANSACTION PANEL - will be populated dynamically
        transactionPanel = new JPanel(null);
        transactionPanel.setBounds(230, 270, 670, 280);
        transactionPanel.setOpaque(false);
        add(transactionPanel);

        loadRecentTransactions();

        setVisible(true);
    }

    private void loadRecentTransactions() {
        transactionPanel.removeAll();
        
        List<Transaction> transactions = transactionImple.GetTransactionByCustomer(customer);
        
        int y = 0;
        int count = 0;
        int maxTransactions = 4; // Show only recent 4 transactions
        
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction trans : transactions) {
                if (count >= maxTransactions) break;
                
                RoundedPanel box = new RoundedPanel(20);
                box.setBackground(new Color(213, 197, 186));
                box.setBounds(0, y, 670, 50);
                box.setLayout(null);
                transactionPanel.add(box);
                
                JLabel typeLabel = new JLabel(trans.getType());
                typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                typeLabel.setForeground(new Color(30, 50, 85));
                typeLabel.setBounds(20, 5, 100, 20);
                box.add(typeLabel);
                
                JLabel txnLabel = new JLabel(trans.getTransactionID());
                txnLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                txnLabel.setForeground(new Color(80, 80, 80));
                txnLabel.setBounds(20, 25, 200, 15);
                box.add(txnLabel);
                
                String amountStr;
                boolean isIncoming = trans.getReceiverID().equals(customer.getAccNo()) || 
                                    trans.getReceiverID().equals(customer.getID());
                boolean isOutgoing = trans.getSenderID().equals(customer.getAccNo()) || 
                                    trans.getSenderID().equals(customer.getID());
                
                if (trans.getType().equals("DEPOSIT")) {
                    amountStr = String.format("+$%.2f", trans.getAmount());
                } else if (trans.getType().equals("WITHDRAW")) {
                    amountStr = String.format("-$%.2f", trans.getAmount());
                } else if (trans.getType().equals("TRANSFER")) {
                    if (isOutgoing && !isIncoming) {
                        amountStr = String.format("-$%.2f", trans.getAmount());
                    } else if (isIncoming && !isOutgoing) {
                        amountStr = String.format("+$%.2f", trans.getAmount());
                    } else {
                        amountStr = String.format("$%.2f", trans.getAmount());
                    }
                } else {
                    amountStr = String.format("$%.2f", trans.getAmount());
                }
                
                JLabel amountLabel = new JLabel(amountStr);
                amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                if (amountStr.startsWith("+")) {
                    amountLabel.setForeground(new Color(34, 139, 34)); // Green for deposit
                } else if (amountStr.startsWith("-")) {
                    amountLabel.setForeground(new Color(220, 20, 60)); // Red for withdrawal
                } else {
                    amountLabel.setForeground(new Color(30, 50, 85));
                }
                amountLabel.setBounds(570, 5, 80, 20);
                box.add(amountLabel);
                
                JLabel dateLabel = new JLabel(trans.getTimestamp());
                dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                dateLabel.setForeground(new Color(100, 100, 100));
                dateLabel.setBounds(520, 25, 140, 15);
                box.add(dateLabel);
                
                y += 65;
                count++;
            }
        } else {
            JLabel noTransLabel = new JLabel("No recent transactions");
            noTransLabel.setForeground(Color.WHITE);
            noTransLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noTransLabel.setBounds(0, 0, 300, 30);
            transactionPanel.add(noTransLabel);
        }
        
        transactionPanel.revalidate();
        transactionPanel.repaint();
    }

    private void refreshCustomerData() {
        customer = customerImple.getCustomerByAccNo(customer.getAccNo());
        amountText.setText(String.format("$%.2f", customer.getBalance()));
        loadRecentTransactions();
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
        detailDialog.setSize(350, 320);
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
            {"Account number", customer.getAccNo()},
            {"Customer ID", customer.getID()},
            {"Name", customer.getName()},
            {"Phone Number", String.valueOf(customer.getPhoneNumber())},
            {"Address", customer.getAddress()},
            {"Birth Date", customer.getBirthDate()},
            {"Account Created", customer.getCreateDate()},
            {"Balance", String.format("$%.2f", customer.getBalance())},
            {"Status", customer.isActive() ? "Active" : "Deactivated"}
        };

        int yPos = 55;
        for (String[] detail : details) {
            JLabel labelName = new JLabel(detail[0]);
            labelName.setForeground(new Color(30, 50, 85));
            labelName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            labelName.setBounds(20, yPos, 150, 15);
            detailDialog.add(labelName);

            JLabel labelValue = new JLabel(detail[1] != null ? detail[1] : "N/A");
            labelValue.setForeground(new Color(218, 186, 121));
            labelValue.setFont(new Font("Segoe UI", Font.BOLD, 11));
            labelValue.setBounds(180, yPos, 150, 15);
            detailDialog.add(labelValue);

            yPos += 22;
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
            customerImple.DeactivateCustomer(customer.getAccNo());
            customer.setActive(false);
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
            new LoginSelection();
        });
        successDialog.add(confirmBtn);

        successDialog.setVisible(true);
    }

    private void showReportDialog() {
        JDialog reportDialog = new JDialog(this, "Transaction Report", true);
        reportDialog.setSize(700, 570);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setResizable(false);
        reportDialog.getContentPane().setBackground(new Color(30, 50, 85));
        reportDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Transaction Report");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 20, 300, 30);
        reportDialog.add(titleLabel);

        // Search Bar
        JTextField searchBar = new JTextField("Search transaction ID");
        searchBar.setBounds(30, 60, 640, 35);
        searchBar.setBackground(new Color(235, 235, 235));
        searchBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchBar.setForeground(Color.GRAY);
        
        searchBar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchBar.getText().equals("Search transaction ID")) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Search transaction ID");
                }
            }
        });
        reportDialog.add(searchBar);

        // Scrollable Transaction List Panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(30, 50, 85));

        List<Transaction> allTransactions = transactionImple.GetTransactionByCustomer(customer);

        if (allTransactions != null && !allTransactions.isEmpty()) {
            for (Transaction trans : allTransactions) {
                JPanel box = createTransactionBox(trans);
                listPanel.add(box);
                listPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noDataLabel = new JLabel("No transactions found");
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(noDataLabel);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(30, 110, 640, 340);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        reportDialog.add(scrollPane);

        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.setBounds(300, 460, 100, 35);
        closeBtn.addActionListener(e -> reportDialog.dispose());
        reportDialog.add(closeBtn);

        reportDialog.setVisible(true);
    }

    private JPanel createTransactionBox(Transaction trans) {
        JPanel box = new JPanel(null);
        box.setPreferredSize(new Dimension(620, 55));
        box.setMaximumSize(new Dimension(620, 55));
        box.setBackground(new Color(235, 235, 235));

        JLabel typeLabel = new JLabel(trans.getType());
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLabel.setForeground(new Color(30, 50, 85));
        typeLabel.setBounds(15, 5, 100, 20);
        box.add(typeLabel);

        JLabel txnLabel = new JLabel("ID: " + trans.getTransactionID());
        txnLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txnLabel.setForeground(new Color(80, 80, 80));
        txnLabel.setBounds(15, 28, 200, 15);
        box.add(txnLabel);

        String amountStr;
        boolean isIncoming = trans.getReceiverID().equals(customer.getAccNo()) || 
                            trans.getReceiverID().equals(customer.getID());
        boolean isOutgoing = trans.getSenderID().equals(customer.getAccNo()) || 
                            trans.getSenderID().equals(customer.getID());
        
        if (trans.getType().equals("DEPOSIT")) {
            amountStr = String.format("+$%.2f", trans.getAmount());
        } else if (trans.getType().equals("WITHDRAW")) {
            amountStr = String.format("-$%.2f", trans.getAmount());
        } else if (trans.getType().equals("TRANSFER")) {
            if (isOutgoing && !isIncoming) {
                amountStr = String.format("-$%.2f", trans.getAmount());
            } else if (isIncoming && !isOutgoing) {
                amountStr = String.format("+$%.2f", trans.getAmount());
            } else {
                amountStr = String.format("$%.2f", trans.getAmount());
            }
        } else {
            amountStr = String.format("$%.2f", trans.getAmount());
        }

        JLabel amountLabel = new JLabel(amountStr);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        if (amountStr.startsWith("+")) {
            amountLabel.setForeground(new Color(34, 139, 34)); // Green
        } else if (amountStr.startsWith("-")) {
            amountLabel.setForeground(new Color(220, 20, 60)); // Red
        } else {
            amountLabel.setForeground(new Color(30, 50, 85));
        }
        amountLabel.setBounds(530, 5, 80, 20);
        box.add(amountLabel);

        JLabel dateLabel = new JLabel(trans.getTimestamp());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setBounds(460, 28, 150, 15);
        box.add(dateLabel);

        return box;
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
            new LoginSelection(); 
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

    // Transaction Dialog with backend integration
    class TransactionDialog extends JDialog {
        private JTextField recipientField;
        private JTextField amountField;
        private String type;

        TransactionDialog(JFrame parent, String type, boolean isTransfer) {
            super(parent, type, true);
            this.type = type;
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

                recipientField = new JTextField();
                recipientField.setBounds(20, yPos + 25, 340, 35);
                recipientField.setBackground(new Color(218, 186, 121));
                recipientField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                recipientField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                add(recipientField);

                yPos += 70;
            }

            JLabel amountLabel = new JLabel("Amount");
            amountLabel.setForeground(new Color(30, 50, 85));
            amountLabel.setBounds(20, yPos, 150, 20);
            add(amountLabel);

            amountField = new JTextField();
            amountField.setBounds(20, yPos + 25, 340, 35);
            amountField.setBackground(new Color(218, 186, 121));
            amountField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            amountField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            add(amountField);

            RoundedButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBounds(70, isTransfer ? 215 : 180, 110, 35);
            cancelBtn.addActionListener(e -> dispose());
            add(cancelBtn);

            RoundedButton actionBtn = new RoundedButton(type);
            actionBtn.setBounds(220, isTransfer ? 215 : 180, 110, 35);
            actionBtn.addActionListener(e -> processTransaction(isTransfer));
            add(actionBtn);
        }

        private void processTransaction(boolean isTransfer) {
            try {
                String amountStr = amountField.getText().trim();
                if (amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Transaction result = null;

                if (type.equals("Deposit")) {
                    result = transactionService.DepositService(customer, amount);
                } else if (type.equals("Withdraw")) {
                    if (amount > customer.getBalance()) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    result = transactionService.WithdrawService(customer, amount);
                } else if (type.equals("Transfer")) {
                    String recipientAccNo = recipientField.getText().trim();
                    if (recipientAccNo.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please enter recipient account number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (recipientAccNo.equals(customer.getAccNo())) {
                        JOptionPane.showMessageDialog(this, "Cannot transfer to your own account.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Customer receiver = customerImple.getCustomerByAccNo(recipientAccNo);
                    if (receiver == null) {
                        JOptionPane.showMessageDialog(this, "Recipient account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!receiver.isActive()) {
                        JOptionPane.showMessageDialog(this, "Recipient account is deactivated.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (amount > customer.getBalance()) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    result = transactionService.processTransfer(customer, receiver, amount);
                }

                if (result != null) {
                    dispose();
                    refreshCustomerData();
                    showSuccessMessage(type);
                } else {
                    JOptionPane.showMessageDialog(this, "Transaction failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Transaction failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
}
