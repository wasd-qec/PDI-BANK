import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Customer Swing UI - A modern banking application interface
 * Integrates with existing Customer, TransactionService, and CustomRead classes
 */
public class CustomerSwingUI extends JFrame {
    
    // Color scheme matching the screenshots
    private static final Color PRIMARY_BG = new Color(102, 128, 144);      // Steel blue background
    private static final Color PANEL_BG = new Color(61, 82, 96);           // Darker panel background
    private static final Color SIDEBAR_BG = new Color(61, 82, 96);         // Sidebar background
    private static final Color TEXT_COLOR = new Color(79, 166, 183);       // Teal text color
    private static final Color WHITE = Color.WHITE;
    private static final Color BUTTON_BG = new Color(230, 230, 230);       // Light gray button
    private static final Color CARD_BG = new Color(75, 95, 110);           // Card background
    
    // Services
    private CustomRead customDbReader = new CustomRead();
    private PasswordEncryption passwordEncryption = new PasswordEncryption();
    
    // Current logged-in customer
    private Customer currentCustomer;
    
    // Card layout for switching panels
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    // Dashboard components
    private JLabel balanceLabel;
    private JPanel transactionListPanel;
    
    public CustomerSwingUI() {
        setTitle("ABA Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        // Create all screens
        mainContainer.add(createRoleSelectionPanel(), "ROLE_SELECTION");
        mainContainer.add(createLoginPanel(), "LOGIN");
        mainContainer.add(createDashboardPanel(), "DASHBOARD");
        
        add(mainContainer);
        
        // Show role selection first
        cardLayout.show(mainContainer, "ROLE_SELECTION");
    }
    
    /**
     * Creates the role selection panel (Screen 1)
     * Shows "Login as" with Customer and Admin buttons
     */
    private JPanel createRoleSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        
        // Center panel with login box
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_BG);
        
        // Login box
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(PANEL_BG);
        loginBox.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        loginBox.setPreferredSize(new Dimension(300, 280));
        
        // Logo
        JPanel logoPanel = createLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // "Login as" text
        JLabel loginAsLabel = new JLabel("Login as");
        loginAsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        loginAsLabel.setForeground(TEXT_COLOR);
        loginAsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Customer button
        JButton customerBtn = createStyledButton("Customer");
        customerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerBtn.addActionListener(e -> cardLayout.show(mainContainer, "LOGIN"));
        
        // Admin button
        JButton adminBtn = createStyledButton("Admin");
        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminBtn.addActionListener(e -> {
            dispose();
            AdminSwingUI.launch();
        });
        
        // Add components with spacing
        loginBox.add(logoPanel);
        loginBox.add(Box.createVerticalStrut(20));
        loginBox.add(loginAsLabel);
        loginBox.add(Box.createVerticalStrut(30));
        loginBox.add(customerBtn);
        loginBox.add(Box.createVerticalStrut(15));
        loginBox.add(adminBtn);
        
        centerPanel.add(loginBox);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the login panel (Screen 2)
     * Shows username and password fields with Back button
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        
        // Back button at top-left
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(PRIMARY_BG);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backBtn.setPreferredSize(new Dimension(70, 28));
        backBtn.addActionListener(e -> cardLayout.show(mainContainer, "ROLE_SELECTION"));
        topPanel.add(backBtn);
        
        // Center login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_BG);
        
        JPanel loginForm = new JPanel();
        loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.Y_AXIS));
        loginForm.setBackground(PRIMARY_BG);
        loginForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Logo
        JPanel logoPanel = createLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Login Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Account Number field
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setBackground(PRIMARY_BG);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel usernameLabel = new JLabel("Account Number");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 35));
        usernameField.setMaximumSize(new Dimension(300, 35));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernamePanel.add(usernameLabel);
        usernamePanel.add(Box.createVerticalStrut(5));
        usernamePanel.add(usernameField);
        
        // Password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBackground(PRIMARY_BG);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setMaximumSize(new Dimension(300, 35));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createVerticalStrut(5));
        passwordPanel.add(passwordField);
        
        // Login button
        JButton loginBtn = createStyledButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            String accNo = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (accNo.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Login Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Authenticate using existing functions
            String storedPassword = customDbReader.getPasswordByAccNo(accNo);
            if (storedPassword != null && passwordEncryption.verifyPassword(password, storedPassword)) {
                currentCustomer = customDbReader.InitializedCus(accNo);
                if (currentCustomer != null) {
                    // Clear fields
                    usernameField.setText("");
                    passwordField.setText("");
                    // Update dashboard and show it
                    updateDashboard();
                    cardLayout.show(mainContainer, "DASHBOARD");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid account number or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Add Enter key listener for password field
        passwordField.addActionListener(e -> loginBtn.doClick());
        
        // Add components
        loginForm.add(logoPanel);
        loginForm.add(Box.createVerticalStrut(25));
        loginForm.add(titleLabel);
        loginForm.add(Box.createVerticalStrut(30));
        loginForm.add(usernamePanel);
        loginForm.add(Box.createVerticalStrut(15));
        loginForm.add(passwordPanel);
        loginForm.add(Box.createVerticalStrut(30));
        loginForm.add(loginBtn);
        
        centerPanel.add(loginForm);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the main dashboard panel (Screen 3)
     * Shows sidebar with menu and main content area with balance and transactions
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        
        // Sidebar
        JPanel sidebar = createSidebar();
        
        // Main content
        JPanel content = createMainContent();
        
        panel.add(sidebar, BorderLayout.WEST);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the sidebar with logo and menu items
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(150, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        // Logo at top
        JPanel logoPanel = createLogoPanel();
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(30));
        
        // Menu items
        String[] menuItems = {"Account detail", "Withdraw", "Transfer", "Deposit", "Deactivate", "Report", "Log out"};
        
        for (String item : menuItems) {
            JButton menuBtn = createMenuButton(item);
            menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(5));
            
            // Add action listeners for each menu item
            final String menuItem = item;
            menuBtn.addActionListener(e -> handleMenuAction(menuItem));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    /**
     * Creates the main content area with balance display and transaction list
     */
    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(PRIMARY_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top section with balance card
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(PRIMARY_BG);
        
        // Balance card
        JPanel balanceCard = new JPanel(new BorderLayout());
        balanceCard.setBackground(CARD_BG);
        balanceCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        balanceCard.setPreferredSize(new Dimension(0, 80));
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceLabel.setForeground(WHITE);
        balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        balanceCard.add(balanceLabel, BorderLayout.CENTER);
        
        // Recent Transactions header
        JLabel recentTransLabel = new JLabel("Recent Transactions");
        recentTransLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recentTransLabel.setForeground(TEXT_COLOR);
        recentTransLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentTransLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        topSection.add(balanceCard);
        topSection.add(recentTransLabel);
        
        // Transaction list panel (scrollable)
        transactionListPanel = new JPanel();
        transactionListPanel.setLayout(new BoxLayout(transactionListPanel, BoxLayout.Y_AXIS));
        transactionListPanel.setBackground(PRIMARY_BG);
        
        JScrollPane scrollPane = new JScrollPane(transactionListPanel);
        scrollPane.setBackground(PRIMARY_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PRIMARY_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        content.add(topSection, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        
        return content;
    }
    
    /**
     * Updates the dashboard with current customer data
     */
    private void updateDashboard() {
        if (currentCustomer == null) return;
        
        // Update balance
        String formattedBalance = String.format("$%,.0f", currentCustomer.getBalance());
        balanceLabel.setText(formattedBalance);
        
        // Update transaction list
        transactionListPanel.removeAll();
        
        List<Transaction> transactions = TransactionService.getCustomerTransactions(currentCustomer.getId());
        
        for (Transaction t : transactions) {
            JPanel transactionCard = createTransactionCard(t);
            transactionListPanel.add(transactionCard);
            transactionListPanel.add(Box.createVerticalStrut(10));
        }
        
        if (transactions.isEmpty()) {
            JLabel noTransLabel = new JLabel("No transactions found");
            noTransLabel.setForeground(TEXT_COLOR);
            noTransLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noTransLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            transactionListPanel.add(noTransLabel);
        }
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
    
    /**
     * Creates a transaction card for the transaction list
     * Shows: transactionId, receiver, sender, type, timestamp, and amount
     */
    private JPanel createTransactionCard(Transaction t) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(0, 90));
        
        // Left side - Transaction details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(CARD_BG);
        
        // Transaction ID and Type
        JLabel idLabel = new JLabel(t.getTransactionId() + " - " + t.getType());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(WHITE);
        
        // Sender and Receiver
        JLabel senderLabel = new JLabel("From: " + t.getSenderId());
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        senderLabel.setForeground(new Color(180, 180, 180));
        
        JLabel receiverLabel = new JLabel("To: " + t.getReceiverId());
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        receiverLabel.setForeground(new Color(180, 180, 180));
        
        // Timestamp
        JLabel timestampLabel = new JLabel(t.getTimestamp());
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timestampLabel.setForeground(new Color(150, 150, 150));
        
        detailsPanel.add(idLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(senderLabel);
        detailsPanel.add(receiverLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(timestampLabel);
        
        // Right side - Amount
        String amountText;
        Color amountColor;
        
        if (t.getType().equals("DEPOSIT") || 
            (t.getType().equals("TRANSFER") && t.getReceiverId().equals(currentCustomer.getId()))) {
            amountText = String.format("+$%.2f", t.getAmount());
            amountColor = new Color(100, 200, 100); // Green for incoming
        } else {
            amountText = String.format("-$%.2f", t.getAmount());
            amountColor = new Color(200, 100, 100); // Red for outgoing
        }
        
        JLabel amountLabel = new JLabel(amountText);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountLabel.setForeground(amountColor);
        amountLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(amountLabel, BorderLayout.EAST);
        
        return card;
    }
    
    /**
     * Handles menu item actions
     */
    private void handleMenuAction(String action) {
        switch (action) {
            case "Account detail" -> showAccountDetail();
            case "Withdraw" -> showWithdrawDialog();
            case "Transfer" -> showTransferDialog();
            case "Deposit" -> showDepositDialog();
            case "Deactivate" -> showDeactivateConfirm();
            case "Report" -> showReport();
            case "Log out" -> logout();
        }
    }
    
    /**
     * Shows account detail dialog
     */
    private void showAccountDetail() {
        if (currentCustomer == null) return;
        
        String details = String.format(
            "<html><body style='width: 300px; padding: 10px;'>" +
            "<h2 style='color: #4FA6B7;'>Account Details</h2>" +
            "<table style='font-size: 12px;'>" +
            "<tr><td><b>Account No:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Name:</b></td><td>%s</td></tr>" +
            "<tr><td><b>ID:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Balance:</b></td><td>$%,.2f</td></tr>" +
            "<tr><td><b>Phone:</b></td><td>%d</td></tr>" +
            "<tr><td><b>Address:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Birth Date:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Created:</b></td><td>%s</td></tr>" +
            "</table></body></html>",
            currentCustomer.getAccNo(),
            currentCustomer.getName(),
            currentCustomer.getId(),
            currentCustomer.getBalance(),
            currentCustomer.getPhoneNumber(),
            currentCustomer.getAddress(),
            currentCustomer.getBirthDate(),
            currentCustomer.getCreateDate()
        );
        
        JOptionPane.showMessageDialog(this, details, "Account Details", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Shows withdraw dialog
     */
    private void showWithdrawDialog() {
        if (currentCustomer == null) return;
        
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel label = new JLabel("Enter amount to withdraw:");
        JTextField amountField = new JTextField();
        panel.add(label);
        panel.add(amountField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Withdraw", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (currentCustomer.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Process withdrawal using existing service
                Transaction t = TransactionService.processWithdrawal(currentCustomer, amount);
                if (t != null) {
                    JOptionPane.showMessageDialog(this, 
                        String.format("Successfully withdrew $%.2f\nNew balance: $%.2f", 
                            amount, currentCustomer.getBalance()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateDashboard();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows transfer dialog
     */
    private void showTransferDialog() {
        if (currentCustomer == null) return;
        
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        JLabel recipientLabel = new JLabel("Recipient Account Number:");
        JTextField recipientField = new JTextField();
        JLabel amountLabel = new JLabel("Amount to Transfer:");
        JTextField amountField = new JTextField();
        
        panel.add(recipientLabel);
        panel.add(recipientField);
        panel.add(amountLabel);
        panel.add(amountField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Transfer", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String recipientAccNo = recipientField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                
                if (recipientAccNo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter recipient account.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (currentCustomer.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Customer recipient = customDbReader.InitializedCus(recipientAccNo);
                if (recipient == null) {
                    JOptionPane.showMessageDialog(this, "Recipient account not found.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Confirm transfer
                int confirm = JOptionPane.showConfirmDialog(this,
                    String.format("Transfer $%.2f to %s (%s)?", amount, recipient.getName(), recipient.getAccNo()),
                    "Confirm Transfer", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Process transfer using existing service
                    Transaction t = TransactionService.processTransfer(currentCustomer, recipient, amount);
                    if (t != null) {
                        JOptionPane.showMessageDialog(this, 
                            String.format("Successfully transferred $%.2f to %s\nNew balance: $%.2f", 
                                amount, recipient.getName(), currentCustomer.getBalance()),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateDashboard();
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows deposit dialog
     */
    private void showDepositDialog() {
        if (currentCustomer == null) return;
        
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel label = new JLabel("Enter amount to deposit:");
        JTextField amountField = new JTextField();
        panel.add(label);
        panel.add(amountField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Deposit", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Process deposit using existing service
                Transaction t = TransactionService.processDeposit(currentCustomer, amount);
                if (t != null) {
                    JOptionPane.showMessageDialog(this, 
                        String.format("Successfully deposited $%.2f\nNew balance: $%.2f", 
                            amount, currentCustomer.getBalance()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateDashboard();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows deactivate account confirmation
     */
    private void showDeactivateConfirm() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to deactivate your account?\nThis action cannot be undone.",
            "Deactivate Account", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Account deactivation request submitted.\nPlease contact customer service.",
                "Request Submitted", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Shows account report (without transactions - they are shown in the main dashboard)
     */
    private void showReport() {
        if (currentCustomer == null) return;
        
        List<Transaction> transactions = TransactionService.getCustomerTransactions(currentCustomer.getId());
        
        // Calculate total in and total out
        double totalIn = 0;
        double totalOut = 0;
        
        for (Transaction t : transactions) {
            if (t.getType().equals("DEPOSIT") || 
                (t.getType().equals("TRANSFER") && t.getReceiverId().equals(currentCustomer.getId()))) {
                totalIn += t.getAmount();
            } else {
                totalOut += t.getAmount();
            }
        }
        
        StringBuilder report = new StringBuilder();
        report.append("<html><body style='width: 300px; font-family: Segoe UI;'>");
        report.append("<h2 style='color: #4FA6B7;'>Account Report</h2>");
        report.append("<hr>");
        report.append("<h3>Account Information</h3>");
        report.append("<p><b>Account:</b> ").append(currentCustomer.getAccNo()).append("</p>");
        report.append("<p><b>Name:</b> ").append(currentCustomer.getName()).append("</p>");
        report.append("<p><b>Current Balance:</b> $").append(String.format("%,.2f", currentCustomer.getBalance())).append("</p>");
        report.append("<hr>");
        report.append("<h3>Transaction Summary</h3>");
        report.append("<p><b>Total Transactions:</b> ").append(transactions.size()).append("</p>");
        report.append("<p style='color: green; font-size: 14px;'><b>Total In:</b> $").append(String.format("%,.2f", totalIn)).append("</p>");
        report.append("<p style='color: red; font-size: 14px;'><b>Total Out:</b> $").append(String.format("%,.2f", totalOut)).append("</p>");
        report.append("</body></html>");
        
        JOptionPane.showMessageDialog(this, report.toString(), "Account Report", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Logs out the current user
     */
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
            "Logout", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            currentCustomer = null;
            cardLayout.show(mainContainer, "ROLE_SELECTION");
        }
    }
    
    /**
     * Creates the ABA logo panel
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(37, 64, 97));
        logoPanel.setPreferredSize(new Dimension(60, 45));
        logoPanel.setMaximumSize(new Dimension(60, 45));
        
        JLabel logoLabel = new JLabel("ABA");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(WHITE);
        logoPanel.add(logoLabel);
        
        return logoPanel;
    }
    
    /**
     * Creates a styled button matching the design
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(150, 35));
        button.setMaximumSize(new Dimension(150, 35));
        button.setBackground(BUTTON_BG);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200, 200, 200));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_BG);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a menu button for the sidebar
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(WHITE);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(130, 30));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(TEXT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(WHITE);
            }
        });
        
        return button;
    }
    
    /**
     * Launch the Swing UI - can be called from Main class
     */
    public static void launch() {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run on EDT
        SwingUtilities.invokeLater(() -> {
            CustomerSwingUI app = new CustomerSwingUI();
            app.setVisible(true);
        });
    }
    
    /**
     * Main entry point for standalone testing
     */
    public static void main(String[] args) {
        launch();
    }
}
