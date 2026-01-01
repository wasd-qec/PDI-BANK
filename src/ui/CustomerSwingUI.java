package ui;

import model.Customer;
import repository.CustomerRepository;
import repository.CustomerRepositoryImpl;
import repository.TransactionRepository;
import repository.TransactionRepositoryImpl;
import security.PasswordEncryption;
import service.CustomerService;
import service.TransactionService;
import ui.dialog.DialogFactory;
import ui.panel.CustomerDashboardPanel;
import ui.panel.LoginPanel;
import ui.panel.RoleSelectionPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Customer Swing UI - Main frame for customer interface
 * Single Responsibility: Only coordinates customer UI screens
 */
public class CustomerSwingUI extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    // Services (dependencies injected)
    private final CustomerService customerService;
    private final TransactionService transactionService;
    
    // Panels
    private CustomerDashboardPanel dashboardPanel;
    private LoginPanel loginPanel;
    
    public CustomerSwingUI() {
        // Initialize dependencies
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        
        this.customerService = new CustomerService(customerRepository, passwordEncryption);
        this.transactionService = new TransactionService(transactionRepository, customerRepository);
        
        initializeFrame();
        initializePanels();
    }
    
    private void initializeFrame() {
        setTitle("ABA Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer);
    }
    
    private void initializePanels() {
        // Role selection panel
        RoleSelectionPanel rolePanel = new RoleSelectionPanel();
        rolePanel.addCustomerListener(() -> cardLayout.show(mainContainer, "LOGIN"));
        rolePanel.addAdminListener(() -> {
            dispose();
            AdminSwingUI.launch();
        });
        
        // Login panel
        loginPanel = new LoginPanel("Login Your Account", "Account Number", true);
        loginPanel.addBackListener(() -> cardLayout.show(mainContainer, "ROLE_SELECTION"));
        loginPanel.addLoginListener(this::handleLogin);
        
        // Dashboard panel
        dashboardPanel = new CustomerDashboardPanel(transactionService, customerService);
        dashboardPanel.setLogoutListener(() -> cardLayout.show(mainContainer, "ROLE_SELECTION"));
        
        // Add panels
        mainContainer.add(rolePanel, "ROLE_SELECTION");
        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(dashboardPanel, "DASHBOARD");
        
        cardLayout.show(mainContainer, "ROLE_SELECTION");
    }
    
    private void handleLogin() {
        String accNo = loginPanel.getUsername();
        String password = loginPanel.getPassword();
        
        if (accNo.isEmpty() || password.isEmpty()) {
            DialogFactory.showWarning(this, "Please enter both account number and password.");
            return;
        }
        
        Customer customer = customerService.authenticate(accNo, password);
        if (customer != null) {
            loginPanel.clearFields();
            dashboardPanel.setCustomer(customer);
            cardLayout.show(mainContainer, "DASHBOARD");
        } else {
            DialogFactory.showError(this, "Invalid account number or password.");
        }
    }
    
    /**
     * Launch the Customer Swing UI
     */
    public static void launch() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CustomerSwingUI app = new CustomerSwingUI();
            app.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        launch();
    }
}
