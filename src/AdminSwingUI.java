import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Swing UI - A modern admin interface for ABA Banking System
 * Features two tabs: Accounts and Transactions with dynamic sidebar
 */
public class AdminSwingUI extends JFrame {
    
    // Color scheme matching the design
    private static final Color PRIMARY_BG = new Color(102, 128, 144);      // Steel blue background
    private static final Color SIDEBAR_BG = new Color(217, 217, 217);      // Light gray sidebar
    private static final Color HEADER_BG = new Color(192, 192, 192);       // Header background
    private static final Color TEXT_COLOR = Color.BLACK;                    // Text color
    private static final Color WHITE = Color.WHITE;
    private static final Color TAB_ACTIVE = new Color(61, 82, 96);         // Active tab color (dark)
    private static final Color TAB_INACTIVE = WHITE;                        // Inactive tab color
    private static final Color CARD_BG = new Color(217, 217, 217);         // Card background
    private static final Color CARD_BORDER = new Color(138, 43, 226);      // Purple border for cards
    
    // Database connection
    private static final String DB_URL = "jdbc:sqlite:d:/School/PROJECT/TestingDB/mydatabase.db";
    
    // Services
    private Read dbReader = new Read();
    private PasswordEncryption passwordEncryption = new PasswordEncryption();
    
    // UI Components
    private CardLayout mainCardLayout;
    private JPanel mainContainer;
    private JPanel sidebarPanel;
    private JPanel accountListPanel;
    private JPanel transactionListPanel;
    private JButton accountTabBtn;
    private JButton transactionTabBtn;
    private String currentTab = "ACCOUNT"; // Track current tab
    
    public AdminSwingUI() {
        setTitle("ABA Banking System - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        
        // Initialize admin table and default admin
        createAdminTable();
        insertDefaultAdmin();
        
        mainCardLayout = new CardLayout();
        mainContainer = new JPanel(mainCardLayout);
        
        // Create login panel first
        mainContainer.add(createLoginPanel(), "LOGIN");
        
        // Create dashboard panel
        mainContainer.add(createDashboardPanel(), "DASHBOARD");
        
        add(mainContainer);
        
        // Show login first
        mainCardLayout.show(mainContainer, "LOGIN");
    }
    
    /**
     * Creates the admin login panel
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
        backBtn.addActionListener(e -> {
            dispose();
            CustomerSwingUI.launch();
        });
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
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(79, 166, 183));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setBackground(PRIMARY_BG);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(79, 166, 183));
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
        passwordLabel.setForeground(new Color(79, 166, 183));
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
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginBtn.setPreferredSize(new Dimension(150, 35));
        loginBtn.setMaximumSize(new Dimension(150, 35));
        loginBtn.setBackground(new Color(230, 230, 230));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Login Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Verify admin credentials from database
            if (verifyAdminLogin(username, password)) {
                // Clear fields
                usernameField.setText("");
                passwordField.setText("");
                // Show dashboard
                mainCardLayout.show(mainContainer, "DASHBOARD");
                refreshAccountList();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
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
     * Creates the main dashboard panel with sidebar and content area
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        
        // Sidebar
        sidebarPanel = createSidebar();
        
        // Main content area
        JPanel contentArea = createContentArea();
        
        panel.add(sidebarPanel, BorderLayout.WEST);
        panel.add(contentArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the sidebar with dynamic menu based on active tab
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(160, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Logo at top
        JPanel logoPanel = createLogoPanel();
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(40));
        
        // Menu items will be updated based on tab
        updateSidebarMenu(sidebar);
        
        return sidebar;
    }
    
    /**
     * Updates the sidebar menu based on current tab
     */
    private void updateSidebarMenu(JPanel sidebar) {
        // Remove all components except logo (first 2 components: logo + strut)
        while (sidebar.getComponentCount() > 2) {
            sidebar.remove(2);
        }
        
        if (currentTab.equals("ACCOUNT")) {
            // Account tab menu: Search, Create Account, Log out
            String[] menuItems = {"Search", "Create Account", "Log out"};
            for (String item : menuItems) {
                JButton menuBtn = createMenuButton(item);
                menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                sidebar.add(menuBtn);
                sidebar.add(Box.createVerticalStrut(15));
                
                final String menuItem = item;
                menuBtn.addActionListener(e -> handleAccountMenuAction(menuItem));
            }
        } else {
            // Transaction tab menu: Search, Report
            String[] menuItems = {"Search", "Report"};
            for (String item : menuItems) {
                JButton menuBtn = createMenuButton(item);
                menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                sidebar.add(menuBtn);
                sidebar.add(Box.createVerticalStrut(15));
                
                final String menuItem = item;
                menuBtn.addActionListener(e -> handleTransactionMenuAction(menuItem));
            }
        }
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    /**
     * Creates the main content area with tabs and list
     */
    private JPanel createContentArea() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(PRIMARY_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tab header panel
        JPanel tabHeader = createTabHeader();
        
        // Card layout for switching between account and transaction views
        CardLayout contentCardLayout = new CardLayout();
        JPanel contentCards = new JPanel(contentCardLayout);
        contentCards.setBackground(PRIMARY_BG);
        
        // Account list panel
        JPanel accountPanel = createAccountListPanel();
        contentCards.add(accountPanel, "ACCOUNT");
        
        // Transaction list panel  
        JPanel transactionPanel = createTransactionListPanel();
        contentCards.add(transactionPanel, "TRANSACTION");
        
        // Tab button actions
        accountTabBtn.addActionListener(e -> {
            currentTab = "ACCOUNT";
            updateTabStyles();
            contentCardLayout.show(contentCards, "ACCOUNT");
            updateSidebarMenu(sidebarPanel);
            refreshAccountList();
        });
        
        transactionTabBtn.addActionListener(e -> {
            currentTab = "TRANSACTION";
            updateTabStyles();
            contentCardLayout.show(contentCards, "TRANSACTION");
            updateSidebarMenu(sidebarPanel);
            refreshTransactionList();
        });
        
        content.add(tabHeader, BorderLayout.NORTH);
        content.add(contentCards, BorderLayout.CENTER);
        
        return content;
    }
    
    /**
     * Creates the tab header with Account and Transaction tabs
     */
    private JPanel createTabHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Tab buttons panel
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        tabPanel.setBackground(HEADER_BG);
        
        // Account tab
        accountTabBtn = createTabButton("Account", true);
        
        // Separator
        JLabel separator = new JLabel("|");
        separator.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        separator.setForeground(new Color(100, 100, 100));
        
        // Transaction tab
        transactionTabBtn = createTabButton("Transaction", false);
        
        tabPanel.add(accountTabBtn);
        tabPanel.add(separator);
        tabPanel.add(transactionTabBtn);
        
        headerPanel.add(tabPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Creates a tab button
     */
    private JButton createTabButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        
        if (isActive) {
            button.setBackground(TAB_ACTIVE);
            button.setForeground(WHITE);
        } else {
            button.setBackground(TAB_INACTIVE);
            button.setForeground(TEXT_COLOR);
        }
        
        return button;
    }
    
    /**
     * Updates tab button styles based on current tab
     */
    private void updateTabStyles() {
        if (currentTab.equals("ACCOUNT")) {
            accountTabBtn.setBackground(TAB_ACTIVE);
            accountTabBtn.setForeground(WHITE);
            transactionTabBtn.setBackground(TAB_INACTIVE);
            transactionTabBtn.setForeground(TEXT_COLOR);
        } else {
            accountTabBtn.setBackground(TAB_INACTIVE);
            accountTabBtn.setForeground(TEXT_COLOR);
            transactionTabBtn.setBackground(TAB_ACTIVE);
            transactionTabBtn.setForeground(WHITE);
        }
    }
    
    /**
     * Creates the account list panel
     */
    private JPanel createAccountListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Account list container with purple border
        accountListPanel = new JPanel();
        accountListPanel.setLayout(new BoxLayout(accountListPanel, BoxLayout.Y_AXIS));
        accountListPanel.setBackground(PRIMARY_BG);
        
        JScrollPane scrollPane = new JScrollPane(accountListPanel);
        scrollPane.setBackground(PRIMARY_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 2));
        scrollPane.getViewport().setBackground(PRIMARY_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the transaction list panel
     */
    private JPanel createTransactionListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Transaction list container with purple border
        transactionListPanel = new JPanel();
        transactionListPanel.setLayout(new BoxLayout(transactionListPanel, BoxLayout.Y_AXIS));
        transactionListPanel.setBackground(PRIMARY_BG);
        
        JScrollPane scrollPane = new JScrollPane(transactionListPanel);
        scrollPane.setBackground(PRIMARY_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 2));
        scrollPane.getViewport().setBackground(PRIMARY_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Refreshes the account list from database
     */
    private void refreshAccountList() {
        accountListPanel.removeAll();
        
        List<Customer> accounts = getAllAccounts();
        
        for (Customer account : accounts) {
            JPanel accountCard = createAccountCard(account);
            accountListPanel.add(accountCard);
        }
        
        if (accounts.isEmpty()) {
            JLabel noDataLabel = new JLabel("No accounts found");
            noDataLabel.setForeground(TEXT_COLOR);
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noDataLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            accountListPanel.add(noDataLabel);
        }
        
        accountListPanel.revalidate();
        accountListPanel.repaint();
    }
    
    /**
     * Refreshes the transaction list from database
     */
    private void refreshTransactionList() {
        transactionListPanel.removeAll();
        
        List<Transaction> transactions = TransactionService.getTransactionHistory();
        
        for (Transaction t : transactions) {
            JPanel transactionCard = createTransactionCard(t);
            transactionListPanel.add(transactionCard);
        }
        
        if (transactions.isEmpty()) {
            JLabel noDataLabel = new JLabel("No transactions found");
            noDataLabel.setForeground(TEXT_COLOR);
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noDataLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            transactionListPanel.add(noDataLabel);
        }
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
    
    /**
     * Creates an account card for the list
     */
    private JPanel createAccountCard(Customer account) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, CARD_BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setPreferredSize(new Dimension(0, 60));
        
        // Account info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        infoPanel.setBackground(CARD_BG);
        
        JLabel nameLabel = new JLabel(account.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        
        JLabel accNoLabel = new JLabel(" - " + account.getAccNo());
        accNoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accNoLabel.setForeground(new Color(100, 100, 100));
        
        infoPanel.add(nameLabel);
        infoPanel.add(accNoLabel);
        
        // Balance on right
        JLabel balanceLabel = new JLabel(String.format("$%,.2f", account.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(34, 139, 34));
        
        // View button
        JButton viewBtn = new JButton("View");
        viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        viewBtn.setPreferredSize(new Dimension(60, 25));
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> showAccountDetails(account));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(CARD_BG);
        rightPanel.add(balanceLabel);
        rightPanel.add(viewBtn);
        
        card.add(infoPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    /**
     * Creates a transaction card for the list (matching the last design)
     */
    private JPanel createTransactionCard(Transaction t) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, CARD_BORDER),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(0, 90));
        
        // Left side - Transaction details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(CARD_BG);
        
        // Transaction ID and Type
        JLabel idLabel = new JLabel(t.getTransactionId() + " - " + t.getType());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(TEXT_COLOR);
        
        // Sender and Receiver
        JLabel senderLabel = new JLabel("From: " + t.getSenderId());
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        senderLabel.setForeground(new Color(80, 80, 80));
        
        JLabel receiverLabel = new JLabel("To: " + t.getReceiverId());
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        receiverLabel.setForeground(new Color(80, 80, 80));
        
        // Timestamp
        JLabel timestampLabel = new JLabel(t.getTimestamp());
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timestampLabel.setForeground(new Color(120, 120, 120));
        
        detailsPanel.add(idLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(senderLabel);
        detailsPanel.add(receiverLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(timestampLabel);
        
        // Right side - Amount
        String amountText;
        Color amountColor;
        
        if (t.getType().equals("DEPOSIT")) {
            amountText = String.format("+$%.2f", t.getAmount());
            amountColor = new Color(34, 139, 34); // Green
        } else if (t.getType().equals("WITHDRAWAL")) {
            amountText = String.format("-$%.2f", t.getAmount());
            amountColor = new Color(178, 34, 34); // Red
        } else {
            amountText = String.format("$%.2f", t.getAmount());
            amountColor = new Color(0, 100, 200); // Blue for transfers
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
     * Gets all accounts from database
     */
    private List<Customer> getAllAccounts() {
        List<Customer> accounts = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY name";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("accNo"),
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getDouble("balance"),
                    rs.getLong("PhoneNumber"),
                    rs.getString("address"),
                    rs.getString("BirthDate"),
                    rs.getString("CreateDate"),
                    rs.getBoolean("Active")
                );
                accounts.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving accounts: " + e.getMessage());
        }
        
        return accounts;
    }
    
    /**
     * Handles account tab menu actions
     */
    private void handleAccountMenuAction(String action) {
        switch (action) {
            case "Search" -> showSearchAccountDialog();
            case "Create Account" -> showCreateAccountDialog();
            case "Log out" -> logout();
        }
    }
    
    /**
     * Handles transaction tab menu actions
     */
    private void handleTransactionMenuAction(String action) {
        switch (action) {
            case "Search" -> showSearchTransactionDialog();
            case "Report" -> showTransactionReport();
        }
    }
    
    /**
     * Shows search account dialog
     */
    private void showSearchAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel label = new JLabel("Enter account number or name:");
        JTextField searchField = new JTextField();
        panel.add(label);
        panel.add(searchField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Search Account", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                searchAccounts(searchTerm);
            }
        }
    }
    
    /**
     * Searches accounts by name or account number
     */
    private void searchAccounts(String searchTerm) {
        accountListPanel.removeAll();
        
        List<Customer> accounts = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name LIKE ? OR accNo LIKE ? ORDER BY name";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("accNo"),
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getDouble("balance"),
                    rs.getLong("PhoneNumber"),
                    rs.getString("address"),
                    rs.getString("BirthDate"),
                    rs.getString("CreateDate"),
                    rs.getBoolean("Active")
                );
                accounts.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error searching accounts: " + e.getMessage());
        }
        
        for (Customer account : accounts) {
            JPanel accountCard = createAccountCard(account);
            accountListPanel.add(accountCard);
        }
        
        if (accounts.isEmpty()) {
            JLabel noDataLabel = new JLabel("No accounts found matching: " + searchTerm);
            noDataLabel.setForeground(TEXT_COLOR);
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noDataLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            accountListPanel.add(noDataLabel);
        }
        
        accountListPanel.revalidate();
        accountListPanel.repaint();
    }
    
    /**
     * Shows create account dialog
     */
    private void showCreateAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField birthDateField = new JTextField();
        JTextField balanceField = new JTextField("0");
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        panel.add(birthDateField);
        panel.add(new JLabel("Initial Balance:"));
        panel.add(balanceField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Create New Account", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String password = new String(passwordField.getPassword());
                long phone = Long.parseLong(phoneField.getText().trim());
                String address = addressField.getText().trim();
                String birthDate = birthDateField.getText().trim();
                double balance = Double.parseDouble(balanceField.getText().trim());
                
                if (name.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and password are required.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Generate IDs
                String id = "USR-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                String accNo = "ACC-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                String createDate = java.time.LocalDate.now().toString();
                
                // Hash password
                PasswordEncryption pe = new PasswordEncryption();
                String hashedPassword = pe.hashPassword(password);
                
                // Insert into database
                dbReader.insertUser(id, name, hashedPassword, balance, phone, accNo, address, birthDate, createDate, true);
                
                JOptionPane.showMessageDialog(this, 
                    "Account created successfully!\nAccount Number: " + accNo, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                refreshAccountList();
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format for phone or balance.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows account details dialog
     */
    private void showAccountDetails(Customer account) {
        String details = String.format(
            "<html><body style='width: 350px; padding: 10px;'>" +
            "<h2 style='color: #4FA6B7;'>Account Details</h2>" +
            "<table style='font-size: 12px;'>" +
            "<tr><td><b>Account No:</b></td><td>%s</td></tr>" +
            "<tr><td><b>ID:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Name:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Balance:</b></td><td>$%,.2f</td></tr>" +
            "<tr><td><b>Phone:</b></td><td>%d</td></tr>" +
            "<tr><td><b>Address:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Birth Date:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Created:</b></td><td>%s</td></tr>" +
            "</table></body></html>",
            account.getAccNo(),
            account.getId(),
            account.getName(),
            account.getBalance(),
            account.getPhoneNumber(),
            account.getAddress(),
            account.getBirthDate(),
            account.getCreateDate()
        );
        
        JOptionPane.showMessageDialog(this, details, "Account Details", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Shows search transaction dialog
     */
    private void showSearchTransactionDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel label = new JLabel("Enter transaction ID or user ID:");
        JTextField searchField = new JTextField();
        panel.add(label);
        panel.add(searchField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Search Transaction", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                searchTransactions(searchTerm);
            }
        }
    }
    
    /**
     * Searches transactions by ID or user ID
     */
    private void searchTransactions(String searchTerm) {
        transactionListPanel.removeAll();
        
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE TransactionId LIKE ? OR SenderId LIKE ? OR ReceiverId LIKE ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            pstmt.setString(3, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction t = new Transaction(
                    rs.getString("TransactionId"),
                    rs.getString("ReceiverId"),
                    rs.getString("SenderId"),
                    rs.getDouble("Amount"),
                    rs.getString("Timestamp"),
                    rs.getString("Type")
                );
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error searching transactions: " + e.getMessage());
        }
        
        for (Transaction t : transactions) {
            JPanel transactionCard = createTransactionCard(t);
            transactionListPanel.add(transactionCard);
        }
        
        if (transactions.isEmpty()) {
            JLabel noDataLabel = new JLabel("No transactions found matching: " + searchTerm);
            noDataLabel.setForeground(TEXT_COLOR);
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noDataLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            transactionListPanel.add(noDataLabel);
        }
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
    
    /**
     * Shows transaction report
     */
    private void showTransactionReport() {
        List<Transaction> transactions = TransactionService.getTransactionHistory();
        
        // Calculate totals
        double totalDeposits = 0;
        double totalWithdrawals = 0;
        double totalTransfers = 0;
        int depositCount = 0;
        int withdrawalCount = 0;
        int transferCount = 0;
        
        for (Transaction t : transactions) {
            switch (t.getType()) {
                case "DEPOSIT" -> {
                    totalDeposits += t.getAmount();
                    depositCount++;
                }
                case "WITHDRAWAL" -> {
                    totalWithdrawals += t.getAmount();
                    withdrawalCount++;
                }
                case "TRANSFER" -> {
                    totalTransfers += t.getAmount();
                    transferCount++;
                }
            }
        }
        
        StringBuilder report = new StringBuilder();
        report.append("<html><body style='width: 350px; font-family: Segoe UI;'>");
        report.append("<h2 style='color: #4FA6B7;'>Transaction Report</h2>");
        report.append("<hr>");
        report.append("<h3>Summary</h3>");
        report.append("<p><b>Total Transactions:</b> ").append(transactions.size()).append("</p>");
        report.append("<hr>");
        report.append("<h3>By Type</h3>");
        report.append("<p style='color: green;'><b>Deposits:</b> ").append(depositCount)
              .append(" (Total: $").append(String.format("%,.2f", totalDeposits)).append(")</p>");
        report.append("<p style='color: red;'><b>Withdrawals:</b> ").append(withdrawalCount)
              .append(" (Total: $").append(String.format("%,.2f", totalWithdrawals)).append(")</p>");
        report.append("<p style='color: blue;'><b>Transfers:</b> ").append(transferCount)
              .append(" (Total: $").append(String.format("%,.2f", totalTransfers)).append(")</p>");
        report.append("<hr>");
        report.append("<p><b>Net Flow:</b> $").append(String.format("%,.2f", totalDeposits - totalWithdrawals)).append("</p>");
        report.append("</body></html>");
        
        JOptionPane.showMessageDialog(this, report.toString(), "Transaction Report", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Logs out the admin
     */
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
            "Logout", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            // Return to main login screen
            CustomerSwingUI.launch();
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
     * Creates a menu button for the sidebar
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(TEXT_COLOR);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(140, 30));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(79, 166, 183));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates the admin table in the database if it doesn't exist
     */
    private void createAdminTable() {
        String sql = "CREATE TABLE IF NOT EXISTS admins (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "name TEXT," +
                    "created_date TEXT)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            
        } catch (SQLException e) {
            System.out.println("Error creating admin table: " + e.getMessage());
        }
    }
    
    /**
     * Inserts default admin if no admins exist
     */
    private void insertDefaultAdmin() {
        // Check if any admin exists
        String checkSql = "SELECT COUNT(*) FROM admins";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // No admin exists, create default one
                String insertSql = "INSERT INTO admins (username, password, name, created_date) VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    String hashedPassword = passwordEncryption.encryptPassword("admin123");
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, hashedPassword);
                    pstmt.setString(3, "System Administrator");
                    pstmt.setString(4, java.time.LocalDate.now().toString());
                    pstmt.executeUpdate();
                    System.out.println("Default admin created: username=admin, password=admin123");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error inserting default admin: " + e.getMessage());
        }
    }
    
    /**
     * Verifies admin login credentials against database
     */
    private boolean verifyAdminLogin(String username, String password) {
        String sql = "SELECT password FROM admins WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return passwordEncryption.verifyPassword(password, storedPassword);
            }
            
        } catch (SQLException e) {
            System.out.println("Error verifying admin login: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Launch the Admin Swing UI
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
            AdminSwingUI app = new AdminSwingUI();
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
