package ui.panel;

import model.Customer;
import model.Transaction;
import service.CustomerService;
import service.TransactionService;
import ui.common.CardFactory;
import ui.common.ComponentFactory;
import ui.common.UIColors;
import ui.dialog.DialogFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Customer dashboard panel
 * Single Responsibility: Only handles customer dashboard UI
 */
public class CustomerDashboardPanel extends JPanel {
    
    private final TransactionService transactionService;
    private final CustomerService customerService;
    private Customer currentCustomer;
    
    private JLabel balanceLabel;
    private JPanel transactionListPanel;
    private Runnable onLogout;
    
    public CustomerDashboardPanel(TransactionService transactionService, CustomerService customerService) {
        this.transactionService = transactionService;
        this.customerService = customerService;
        
        setLayout(new BorderLayout());
        setBackground(UIColors.PRIMARY_BG);
        
        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);
    }
    
    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        updateDashboard();
    }
    
    public void setLogoutListener(Runnable onLogout) {
        this.onLogout = onLogout;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIColors.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(150, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(30));
        
        String[] menuItems = {"Account detail", "Withdraw", "Transfer", "Deposit", "Deactivate", "Report", "Log out"};
        
        for (String item : menuItems) {
            JButton menuBtn = ComponentFactory.createMenuButton(item, UIColors.SIDEBAR_BG, UIColors.WHITE);
            menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(5));
            
            final String menuItem = item;
            menuBtn.addActionListener(e -> handleMenuAction(menuItem));
        }
        
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }
    
    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIColors.PRIMARY_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(UIColors.PRIMARY_BG);
        
        // Balance card
        JPanel balanceCard = new JPanel(new BorderLayout());
        balanceCard.setBackground(UIColors.CARD_BG);
        balanceCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        balanceCard.setPreferredSize(new Dimension(0, 80));
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceLabel.setForeground(UIColors.WHITE);
        balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        balanceCard.add(balanceLabel, BorderLayout.CENTER);
        
        JLabel recentTransLabel = new JLabel("Recent Transactions");
        recentTransLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recentTransLabel.setForeground(UIColors.TEXT_COLOR);
        recentTransLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentTransLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        topSection.add(balanceCard);
        topSection.add(recentTransLabel);
        
        transactionListPanel = new JPanel();
        transactionListPanel.setLayout(new BoxLayout(transactionListPanel, BoxLayout.Y_AXIS));
        transactionListPanel.setBackground(UIColors.PRIMARY_BG);
        
        JScrollPane scrollPane = ComponentFactory.createStyledScrollPane(transactionListPanel, UIColors.PRIMARY_BG);
        
        content.add(topSection, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        
        return content;
    }
    
    public void updateDashboard() {
        if (currentCustomer == null) return;
        
        String formattedBalance = String.format("$%,.0f", currentCustomer.getBalance());
        balanceLabel.setText(formattedBalance);
        
        transactionListPanel.removeAll();
        
        List<Transaction> transactions = transactionService.getCustomerTransactions(currentCustomer.getId());
        
        for (Transaction t : transactions) {
            JPanel transactionCard = CardFactory.createCustomerTransactionCard(t, currentCustomer.getId());
            transactionListPanel.add(transactionCard);
            transactionListPanel.add(Box.createVerticalStrut(10));
        }
        
        if (transactions.isEmpty()) {
            JLabel noTransLabel = new JLabel("No transactions found");
            noTransLabel.setForeground(UIColors.TEXT_COLOR);
            noTransLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noTransLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            transactionListPanel.add(noTransLabel);
        }
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
    
    private void handleMenuAction(String action) {
        switch (action) {
            case "Account detail" -> DialogFactory.showAccountDetail(this, currentCustomer);
            case "Withdraw" -> handleWithdraw();
            case "Transfer" -> handleTransfer();
            case "Deposit" -> handleDeposit();
            case "Deactivate" -> handleDeactivate();
            case "Report" -> handleReport();
            case "Log out" -> handleLogout();
        }
    }
    
    private void handleWithdraw() {
        if (currentCustomer == null) return;
        
        double amount = DialogFactory.showWithdrawDialog(this);
        if (amount <= 0) return;
        
        if (currentCustomer.getBalance() < amount) {
            DialogFactory.showError(this, "Insufficient funds.");
            return;
        }
        
        Transaction t = transactionService.processWithdrawal(currentCustomer, amount);
        if (t != null) {
            DialogFactory.showSuccess(this, String.format(
                "Successfully withdrew $%.2f\nNew balance: $%.2f", 
                amount, currentCustomer.getBalance()));
            updateDashboard();
        }
    }
    
    private void handleTransfer() {
        if (currentCustomer == null) return;
        
        String[] transferData = DialogFactory.showTransferDialog(this);
        if (transferData == null) return;
        
        String recipientAccNo = transferData[0];
        double amount = Double.parseDouble(transferData[1]);
        
        if (currentCustomer.getBalance() < amount) {
            DialogFactory.showError(this, "Insufficient funds.");
            return;
        }
        
        Customer recipient = customerService.findByAccNo(recipientAccNo);
        if (recipient == null) {
            DialogFactory.showError(this, "Recipient account not found.");
            return;
        }
        
        boolean confirm = DialogFactory.showConfirmDialog(this,
            String.format("Transfer $%.2f to %s (%s)?", amount, recipient.getName(), recipient.getAccNo()),
            "Confirm Transfer");
        
        if (confirm) {
            Transaction t = transactionService.processTransfer(currentCustomer, recipient, amount);
            if (t != null) {
                DialogFactory.showSuccess(this, String.format(
                    "Successfully transferred $%.2f to %s\nNew balance: $%.2f", 
                    amount, recipient.getName(), currentCustomer.getBalance()));
                updateDashboard();
            }
        }
    }
    
    private void handleDeposit() {
        if (currentCustomer == null) return;
        
        double amount = DialogFactory.showDepositDialog(this);
        if (amount <= 0) return;
        
        Transaction t = transactionService.processDeposit(currentCustomer, amount);
        if (t != null) {
            DialogFactory.showSuccess(this, String.format(
                "Successfully deposited $%.2f\nNew balance: $%.2f", 
                amount, currentCustomer.getBalance()));
            updateDashboard();
        }
    }
    
    private void handleDeactivate() {
        boolean confirm = DialogFactory.showConfirmDialog(this,
            "Are you sure you want to deactivate your account?\nThis action cannot be undone.",
            "Deactivate Account");
        
        if (confirm) {
            DialogFactory.showSuccess(this, "Account deactivation request submitted.\nPlease contact customer service.");
        }
    }
    
    private void handleReport() {
        if (currentCustomer == null) return;
        
        List<Transaction> transactions = transactionService.getCustomerTransactions(currentCustomer.getId());
        DialogFactory.showCustomerReport(this, currentCustomer, transactions);
    }
    
    private void handleLogout() {
        boolean confirm = DialogFactory.showConfirmDialog(this, "Are you sure you want to log out?", "Logout");
        
        if (confirm && onLogout != null) {
            currentCustomer = null;
            onLogout.run();
        }
    }
}
