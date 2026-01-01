package ui;

import model.Customer;
import model.Transaction;
import repository.*;
import security.PasswordEncryption;
import service.AdminService;
import service.CustomerService;
import service.TransactionService;
import ui.common.CardFactory;
import ui.common.ComponentFactory;
import ui.common.UIColors;
import ui.dialog.DialogFactory;
import ui.panel.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Admin Swing UI - Main frame for admin interface
 * Single Responsibility: Coordinates admin UI screens
 */
public class AdminSwingUI extends JFrame {
    
    private static final Color SIDEBAR_BG = new Color(217, 217, 217);
    
    private CardLayout mainCardLayout;
    private JPanel mainContainer;
    private JPanel sidebarPanel;
    private JPanel accountListPanel;
    private JPanel transactionListPanel;
    private JButton accountTabBtn;
    private JButton transactionTabBtn;
    private String currentTab = "ACCOUNT";
    
    // Services
    private final AdminService adminService;
    private final CustomerService customerService;
    private final TransactionService transactionService;
    
    public AdminSwingUI() {
        // Initialize dependencies
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        AdminRepository adminRepository = new AdminRepositoryImpl(passwordEncryption);
        
        this.adminService = new AdminService(adminRepository);
        this.customerService = new CustomerService(customerRepository, passwordEncryption);
        this.transactionService = new TransactionService(transactionRepository, customerRepository);
        
        // Initialize admin system
        adminService.initialize();
        
        initializeFrame();
        initializePanels();
    }
    
    private void initializeFrame() {
        setTitle("ABA Banking System - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        
        mainCardLayout = new CardLayout();
        mainContainer = new JPanel(mainCardLayout);
        add(mainContainer);
    }
    
    private void initializePanels() {
        // Login panel
        LoginPanel loginPanel = new LoginPanel("Admin Login", "Username", true);
        loginPanel.addBackListener(() -> {
            dispose();
            CustomerSwingUI.launch();
        });
        loginPanel.addLoginListener(() -> handleLogin(loginPanel));
        
        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(createDashboardPanel(), "DASHBOARD");
        
        mainCardLayout.show(mainContainer, "LOGIN");
    }
    
    private void handleLogin(LoginPanel loginPanel) {
        String username = loginPanel.getUsername();
        String password = loginPanel.getPassword();
        
        if (username.isEmpty() || password.isEmpty()) {
            DialogFactory.showWarning(this, "Please enter both username and password.");
            return;
        }
        
        if (adminService.authenticate(username, password)) {
            loginPanel.clearFields();
            mainCardLayout.show(mainContainer, "DASHBOARD");
            refreshAccountList();
        } else {
            DialogFactory.showError(this, "Invalid username or password.");
        }
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIColors.PRIMARY_BG);
        
        sidebarPanel = createSidebar();
        JPanel contentArea = createContentArea();
        
        panel.add(sidebarPanel, BorderLayout.WEST);
        panel.add(contentArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(160, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(40));
        
        updateSidebarMenu(sidebar);
        
        return sidebar;
    }
    
    private void updateSidebarMenu(JPanel sidebar) {
        while (sidebar.getComponentCount() > 2) {
            sidebar.remove(2);
        }
        
        String[] menuItems = currentTab.equals("ACCOUNT") 
            ? new String[]{"Search", "Create Account", "Log out"}
            : new String[]{"Search", "Report"};
        
        for (String item : menuItems) {
            JButton menuBtn = createAdminMenuButton(item);
            menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(15));
            
            final String menuItem = item;
            menuBtn.addActionListener(e -> handleMenuAction(menuItem));
        }
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    private JButton createAdminMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(UIColors.BLACK);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(140, 30));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(UIColors.TEXT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(UIColors.BLACK);
            }
        });
        
        return button;
    }
    
    private JPanel createContentArea() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIColors.PRIMARY_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel tabHeader = createTabHeader();
        
        CardLayout contentCardLayout = new CardLayout();
        JPanel contentCards = new JPanel(contentCardLayout);
        contentCards.setBackground(UIColors.PRIMARY_BG);
        
        contentCards.add(createAccountListPanel(), "ACCOUNT");
        contentCards.add(createTransactionListPanel(), "TRANSACTION");
        
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
    
    private JPanel createTabHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIColors.HEADER_BG);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        tabPanel.setBackground(UIColors.HEADER_BG);
        
        accountTabBtn = ComponentFactory.createTabButton("Account", true);
        
        JLabel separator = new JLabel("|");
        separator.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        separator.setForeground(new Color(100, 100, 100));
        
        transactionTabBtn = ComponentFactory.createTabButton("Transaction", false);
        
        tabPanel.add(accountTabBtn);
        tabPanel.add(separator);
        tabPanel.add(transactionTabBtn);
        
        headerPanel.add(tabPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private void updateTabStyles() {
        if (currentTab.equals("ACCOUNT")) {
            accountTabBtn.setBackground(UIColors.TAB_ACTIVE);
            accountTabBtn.setForeground(UIColors.WHITE);
            transactionTabBtn.setBackground(UIColors.TAB_INACTIVE);
            transactionTabBtn.setForeground(UIColors.BLACK);
        } else {
            accountTabBtn.setBackground(UIColors.TAB_INACTIVE);
            accountTabBtn.setForeground(UIColors.BLACK);
            transactionTabBtn.setBackground(UIColors.TAB_ACTIVE);
            transactionTabBtn.setForeground(UIColors.WHITE);
        }
    }
    
    private JPanel createAccountListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIColors.PRIMARY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        accountListPanel = new JPanel();
        accountListPanel.setLayout(new BoxLayout(accountListPanel, BoxLayout.Y_AXIS));
        accountListPanel.setBackground(UIColors.PRIMARY_BG);
        
        JScrollPane scrollPane = new JScrollPane(accountListPanel);
        scrollPane.setBackground(UIColors.PRIMARY_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIColors.CARD_BORDER, 2));
        scrollPane.getViewport().setBackground(UIColors.PRIMARY_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTransactionListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIColors.PRIMARY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        transactionListPanel = new JPanel();
        transactionListPanel.setLayout(new BoxLayout(transactionListPanel, BoxLayout.Y_AXIS));
        transactionListPanel.setBackground(UIColors.PRIMARY_BG);
        
        JScrollPane scrollPane = new JScrollPane(transactionListPanel);
        scrollPane.setBackground(UIColors.PRIMARY_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIColors.CARD_BORDER, 2));
        scrollPane.getViewport().setBackground(UIColors.PRIMARY_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshAccountList() {
        accountListPanel.removeAll();
        
        List<Customer> accounts = customerService.getAllCustomers();
        
        for (Customer account : accounts) {
            JPanel accountCard = CardFactory.createAccountCard(account, 
                () -> DialogFactory.showAccountDetail(this, account));
            accountListPanel.add(accountCard);
        }
        
        if (accounts.isEmpty()) {
            accountListPanel.add(createNoDataLabel("No accounts found"));
        }
        
        accountListPanel.revalidate();
        accountListPanel.repaint();
    }
    
    private void refreshTransactionList() {
        transactionListPanel.removeAll();
        
        List<Transaction> transactions = transactionService.getTransactionHistory();
        
        for (Transaction t : transactions) {
            transactionListPanel.add(CardFactory.createAdminTransactionCard(t));
        }
        
        if (transactions.isEmpty()) {
            transactionListPanel.add(createNoDataLabel("No transactions found"));
        }
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
    
    private JLabel createNoDataLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIColors.BLACK);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return label;
    }
    
    private void handleMenuAction(String action) {
        switch (action) {
            case "Search" -> {
                if (currentTab.equals("ACCOUNT")) {
                    searchAccounts();
                } else {
                    searchTransactions();
                }
            }
            case "Create Account" -> showCreateAccountDialog();
            case "Report" -> DialogFactory.showTransactionReport(this, transactionService.getTransactionHistory());
            case "Log out" -> handleLogout();
        }
    }
    
    private void searchAccounts() {
        String searchTerm = DialogFactory.showSearchDialog(this, "Search Account", "Enter account number or name:");
        if (searchTerm == null) return;
        
        accountListPanel.removeAll();
        List<Customer> accounts = customerService.searchCustomers(searchTerm);
        
        for (Customer account : accounts) {
            JPanel accountCard = CardFactory.createAccountCard(account, 
                () -> DialogFactory.showAccountDetail(this, account));
            accountListPanel.add(accountCard);
        }
        
        if (accounts.isEmpty()) {
            accountListPanel.add(createNoDataLabel("No accounts found matching: " + searchTerm));
        }
        
        accountListPanel.revalidate();
        accountListPanel.repaint();
    }
    
    private void searchTransactions() {
        String searchTerm = DialogFactory.showSearchDialog(this, "Search Transaction", "Enter transaction ID or user ID:");
        if (searchTerm == null) return;
        
        transactionListPanel.removeAll();
        List<Transaction> transactions = transactionService.searchTransactions(searchTerm);
        
        for (Transaction t : transactions) {
            transactionListPanel.add(CardFactory.createAdminTransactionCard(t));
        }
        
        if (transactions.isEmpty()) {
            transactionListPanel.add(createNoDataLabel("No transactions found matching: " + searchTerm));
        }
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
    
    private void showCreateAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
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
                    DialogFactory.showError(this, "Name and password are required.");
                    return;
                }
                
                Customer customer = customerService.createAccount(name, password, phone, address, birthDate, balance);
                DialogFactory.showSuccess(this, "Account created successfully!\nAccount Number: " + customer.getAccNo());
                refreshAccountList();
                
            } catch (NumberFormatException e) {
                DialogFactory.showError(this, "Invalid number format for phone or balance.");
            }
        }
    }
    
    private void handleLogout() {
        boolean confirm = DialogFactory.showConfirmDialog(this, "Are you sure you want to log out?", "Logout");
        
        if (confirm) {
            dispose();
            CustomerSwingUI.launch();
        }
    }
    
    /**
     * Launch the Admin Swing UI
     */
    public static void launch() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AdminSwingUI app = new AdminSwingUI();
            app.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        launch();
    }
}
