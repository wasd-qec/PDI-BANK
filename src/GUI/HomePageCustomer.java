package GUI;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import Object.Customer;
import Object.Transaction;
import Database.CustomerHandling;
import Database.TransactionInterface;
import Database.ReportInterfaceUser;
import Database.TransactionImplement;
import Service.TransactionService;

public class HomePageCustomer extends JFrame {
    private Customer customer;
    private CustomerHandling customerHandling = new CustomerHandling();
    private TransactionInterface transactionHandling = new TransactionImplement();
    private TransactionService transactionService;
    private JLabel amountText;
    private JPanel transactionPanel;

    public HomePageCustomer(Customer customer) {
        this.customer = customer;
        this.transactionService = new TransactionService(customerHandling, transactionHandling);
        
        setTitle("Home Page - " + customer.getName());
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(30, 50, 85));

        // ========== LEFT SIDE PANEL ==========
        JPanel sideBar = new JPanel(null);
        sideBar.setBounds(0, 0, 200, 600);
        sideBar.setBackground(new Color(8, 25, 64));
        add(sideBar);

        // LOGO
        ImageIcon logo = new ImageIcon("src\\GUI\\TMB_Logo.png");
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

        RoundedButton changePasswordBT = new RoundedButton("Change Password");
        changePasswordBT.setBounds(30, 315, 140, 35);
        changePasswordBT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changePasswordBT.addActionListener(e -> showChangePasswordDialog());
        sideBar.add(changePasswordBT);

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

        // TRANSACTION PANEL - will be populated dynamically (scrollable)
        transactionPanel = new JPanel();
        transactionPanel.setLayout(new BoxLayout(transactionPanel, BoxLayout.Y_AXIS));
        transactionPanel.setOpaque(false);
        transactionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane transactionScroll = new JScrollPane(transactionPanel);
        transactionScroll.setBounds(230, 270, 670, 280);
        transactionScroll.setBorder(null);
        transactionScroll.getVerticalScrollBar().setUnitIncrement(16);
        transactionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        transactionScroll.getViewport().setOpaque(false);
        transactionScroll.setOpaque(false);
        add(transactionScroll);

        loadRecentTransactions();

        setVisible(true);
    }

    private void loadRecentTransactions() {
        transactionPanel.removeAll();

        List<Transaction> transactions = transactionHandling.GetTransactionByCustomer(customer);

        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction trans : transactions) {
                RoundedPanel box = new RoundedPanel(20);
                box.setBackground(new Color(213, 197, 186));
                box.setPreferredSize(new Dimension(650, 50));
                box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                box.setLayout(null);
                box.setAlignmentX(Component.LEFT_ALIGNMENT);
                
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

                transactionPanel.add(box);
                transactionPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noTransLabel = new JLabel("No recent transactions");
            noTransLabel.setForeground(Color.WHITE);
            noTransLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noTransLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionPanel.add(noTransLabel);
        }

        transactionPanel.revalidate();
        transactionPanel.repaint();
    }

    private void refreshCustomerData() {
        customer = customerHandling.getCustomerByAccNo(customer.getAccNo());
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
        detailDialog.setSize(400, 380);
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
        RoundedButton editBtn = new RoundedButton("Edit");
        editBtn.setBackground(new Color(218, 186, 121));
        editBtn.setBounds(145, 270, 130, 35);
        editBtn.addActionListener(e -> {
            detailDialog.dispose();
            showEditProfileDialog();
        });
        detailDialog.add(editBtn);
        detailDialog.setVisible(true);
    }

    private void showEditProfileDialog() {
        JDialog editDialog = new JDialog(this, "Edit Profile", true);
        editDialog.setSize(400, 300);
        editDialog.setLocationRelativeTo(this);
        editDialog.setResizable(false);
        editDialog.getContentPane().setBackground(new Color(245, 240, 235));
        editDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 150, 25);
        editDialog.add(titleLabel);

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setForeground(new Color(30, 50, 85));
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        phoneLabel.setBounds(20, 60, 150, 15);
        editDialog.add(phoneLabel);

        JTextField phoneField = new JTextField(String.valueOf(customer.getPhoneNumber()));
        phoneField.setBounds(20, 80, 340, 30);
        phoneField.setBackground(new Color(218, 186, 121));
        phoneField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        phoneField.setForeground(Color.BLACK);
        editDialog.add(phoneField);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setForeground(new Color(30, 50, 85));
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        addressLabel.setBounds(20, 120, 150, 15);
        editDialog.add(addressLabel);

        JTextField addressField = new JTextField(customer.getAddress());
        addressField.setBounds(20, 140, 340, 30);
        addressField.setBackground(new Color(218, 186, 121));
        addressField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addressField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addressField.setForeground(Color.BLACK);
        editDialog.add(addressField);

        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBounds(80, 200, 100, 35);
        cancelBtn.setBackground(new Color(108, 130, 173));
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> editDialog.dispose());
        editDialog.add(cancelBtn);

        RoundedButton saveBtn = new RoundedButton("Save");
        saveBtn.setBackground(new Color(8, 25, 64));
        saveBtn.setBounds(200, 200, 100, 35);
        saveBtn.addActionListener(e -> {
            try {
                String newPhone = phoneField.getText().trim();
                String newAddress = addressField.getText().trim();
                
                // Validation
                if (newPhone.isEmpty() || newAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate phone number is numeric
                int phoneNumber;
                try {
                    phoneNumber = Integer.parseInt(newPhone);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editDialog, "Phone number must be numeric!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Update customer object
                customer.setPhoneNumber(phoneNumber);
                customer.setAddress(newAddress);
                
                // Save to database
                customerHandling.UpdateCustomer(customer);
                
                // Refresh customer data from database
                customer = customerHandling.getCustomerByAccNo(customer.getAccNo());
                
                editDialog.dispose();
                showSuccessMessage("Profile Updated");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Failed to update profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        editDialog.add(saveBtn);

        editDialog.setVisible(true);
    }

    private void showChangePasswordDialog() {
       new ChangePasswordDialog(this, customer).setVisible(true);
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

        // (search bar removed)

        // Summary similar to Report.printCustomerAccountSummary
        Report report = new Report();
        double totalDeposit = report.getCustomerTotalDeposit(customer);
        double totalWithdrawal = report.getCustomerTotalWithdrawal(customer);
        double totalTransferIn = report.getCustomerTotalTransferIn(customer);
        double totalTransferOut = report.getCustomerTotalTransferOut(customer);
        double moneyIn = totalDeposit + totalTransferIn;
        double moneyOut = totalWithdrawal + totalTransferOut;

        // Date range pickers (start / end)
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = cal.getTime();

        JSpinner startSpinner = new JSpinner(new SpinnerDateModel(startDate, null, null, Calendar.DAY_OF_MONTH));
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd"));
        startSpinner.setBounds(30, 60, 150, 25);
        reportDialog.add(startSpinner);
        JSpinner endSpinner = new JSpinner(new SpinnerDateModel(endDate, null, null, Calendar.DAY_OF_MONTH));
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd"));
        endSpinner.setBounds(200, 60, 150, 25);
        reportDialog.add(endSpinner);
        RoundedButton applyBtn = new RoundedButton("Apply");
        applyBtn.setBounds(370, 60, 100, 25);
        reportDialog.add(applyBtn);

        RoundedPanel summaryPanel = new RoundedPanel(12);
        summaryPanel.setBackground(new Color(245, 240, 235));
        summaryPanel.setBounds(30, 110, 640, 70);
        summaryPanel.setLayout(null);
        reportDialog.add(summaryPanel);

        JLabel inLabel = new JLabel("Money In");
        inLabel.setFont(new Font("Serif", Font.BOLD, 14));
        inLabel.setForeground(new Color(30, 50, 85));
        inLabel.setBounds(20, 12, 120, 20);
        summaryPanel.add(inLabel);

        JLabel inValue = new JLabel(String.format("$%.2f", moneyIn));
        inValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inValue.setForeground(new Color(34, 139, 34));
        inValue.setBounds(20, 34, 200, 20);
        summaryPanel.add(inValue);

        JLabel outLabel = new JLabel("Money Out");
        outLabel.setFont(new Font("Serif", Font.BOLD, 14));
        outLabel.setForeground(new Color(30, 50, 85));
        outLabel.setBounds(240, 12, 120, 20);
        summaryPanel.add(outLabel);

        JLabel outValue = new JLabel(String.format("$%.2f", moneyOut));
        outValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        outValue.setForeground(new Color(220, 20, 60));
        outValue.setBounds(240, 34, 200, 20);
        summaryPanel.add(outValue);

        JLabel balLabel = new JLabel("Current Balance");
        balLabel.setFont(new Font("Serif", Font.BOLD, 14));
        balLabel.setForeground(new Color(30, 50, 85));
        balLabel.setBounds(460, 12, 140, 20);
        summaryPanel.add(balLabel);

        JLabel balValue = new JLabel(String.format("$%.2f", customer.getBalance()));
        balValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balValue.setForeground(new Color(30, 50, 85));
        balValue.setBounds(460, 34, 160, 20);
        summaryPanel.add(balValue);


        // Display per-customer totals returned by Report (no extra calculation)
        JPanel valuesPanel = new JPanel(null);
        valuesPanel.setBackground(new Color(245, 240, 235));
        valuesPanel.setBounds(30, 190, 640, 220);
        reportDialog.add(valuesPanel);

        JLabel depLabel = new JLabel("Total Deposits:");
        depLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        depLabel.setForeground(new Color(30, 50, 85));
        depLabel.setBounds(20, 20, 200, 20);
        valuesPanel.add(depLabel);

        JLabel depVal = new JLabel(String.format("$%.2f", totalDeposit));
        depVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        depVal.setForeground(new Color(34, 139, 34));
        depVal.setBounds(220, 20, 200, 20);
        valuesPanel.add(depVal);

        JLabel wdrLabel = new JLabel("Total Withdrawals:");
        wdrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        wdrLabel.setForeground(new Color(30, 50, 85));
        wdrLabel.setBounds(20, 55, 200, 20);
        valuesPanel.add(wdrLabel);

        JLabel wdrVal = new JLabel(String.format("$%.2f", totalWithdrawal));
        wdrVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        wdrVal.setForeground(new Color(220, 20, 60));
        wdrVal.setBounds(220, 55, 200, 20);
        valuesPanel.add(wdrVal);

        JLabel tinLabel = new JLabel("Transfer In:");
        tinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tinLabel.setForeground(new Color(30, 50, 85));
        tinLabel.setBounds(20, 90, 200, 20);
        valuesPanel.add(tinLabel);

        JLabel tinVal = new JLabel(String.format("$%.2f", totalTransferIn));
        tinVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tinVal.setForeground(new Color(34, 139, 34));
        tinVal.setBounds(220, 90, 200, 20);
        valuesPanel.add(tinVal);

        JLabel toutLabel = new JLabel("Transfer Out:");
        toutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toutLabel.setForeground(new Color(30, 50, 85));
        toutLabel.setBounds(20, 125, 200, 20);
        valuesPanel.add(toutLabel);

        JLabel toutVal = new JLabel(String.format("$%.2f", totalTransferOut));
        toutVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        toutVal.setForeground(new Color(220, 20, 60));
        toutVal.setBounds(220, 125, 200, 20);
        valuesPanel.add(toutVal);

        JLabel balLabel2 = new JLabel("Current Balance:");
        balLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        balLabel2.setForeground(new Color(30, 50, 85));
        balLabel2.setBounds(20, 160, 200, 20);
        valuesPanel.add(balLabel2);

        JLabel balVal2 = new JLabel(String.format("$%.2f", customer.getBalance()));
        balVal2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balVal2.setForeground(new Color(30, 50, 85));
        balVal2.setBounds(220, 160, 200, 20);
        valuesPanel.add(balVal2);

        // Apply button listener: update values using selected date range
        applyBtn.addActionListener(e -> {
            Date s = (Date) startSpinner.getValue();
            Date en = (Date) endSpinner.getValue();
            String sStr = dateFmt.format(s) + " 00:00:00";
            String eStr = dateFmt.format(en) + " 23:59:59";

            double depR = report.getCustomerTotalDeposit(customer, sStr, eStr);
            double wdrR = report.getCustomerTotalWithdrawal(customer, sStr, eStr);
            double tinR = report.getCustomerTotalTransferIn(customer, sStr, eStr);
            double toutR = report.getCustomerTotalTransferOut(customer, sStr, eStr);

            depVal.setText(String.format("$%.2f", depR));
            wdrVal.setText(String.format("$%.2f", wdrR));
            tinVal.setText(String.format("$%.2f", tinR));
            toutVal.setText(String.format("$%.2f", toutR));

            double inR = depR + tinR;
            double outR = wdrR + toutR;
            inValue.setText(String.format("$%.2f", inR));
            outValue.setText(String.format("$%.2f", outR));
        });

        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.setBounds(300, 430, 100, 35);
        closeBtn.addActionListener(e -> reportDialog.dispose());
        reportDialog.add(closeBtn);

        reportDialog.setVisible(true);
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
