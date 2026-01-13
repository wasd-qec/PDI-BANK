package ui.dialog;

import Search.CustomerSearchCriteria;
import Search.TransactionSearchCriteria;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog factory for search and filter dialogs
 * Single Responsibility: Creates and shows search/filter dialogs
 */
public final class SearchFilterDialog {
    
    private SearchFilterDialog() {
        // Prevent instantiation
    }
    
    // ==================== CUSTOMER SEARCH DIALOG ====================
    
    /**
     * Shows a dialog for searching customers
     * @param parent the parent component
     * @return CustomerSearchCriteria with search terms, or null if cancelled
     */
    public static CustomerSearchCriteria showCustomerSearchDialog(Component parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField accNoField = new JTextField(20);
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        
        // Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel header = new JLabel("Search Customer By:");
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(header, gbc);
        
        gbc.gridwidth = 1;
        
        // Account Number
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        panel.add(accNoField, gbc);
        
        // ID
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        // Note
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JLabel note = new JLabel("<html><i>Leave fields empty to skip. Partial matches are supported.</i></html>");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        panel.add(note, gbc);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, "Search Customer",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String accNo = accNoField.getText().trim();
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            
            // Check if at least one field is filled
            if (accNo.isEmpty() && id.isEmpty() && name.isEmpty() && phone.isEmpty()) {
                return null;
            }
            
            return CustomerSearchCriteria.builder()
                .accNo(accNo.isEmpty() ? null : accNo)
                .id(id.isEmpty() ? null : id)
                .name(name.isEmpty() ? null : name)
                .phoneNumber(phone.isEmpty() ? null : phone)
                .build();
        }
        
        return null;
    }
    
    // ==================== CUSTOMER FILTER DIALOG ====================
    
    /**
     * Shows a dialog for filtering customers
     * @param parent the parent component
     * @return CustomerSearchCriteria with filter settings, or null if cancelled
     */
    public static CustomerSearchCriteria showCustomerFilterDialog(Component parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField nameField = new JTextField(15);
        JTextField dateFromField = new JTextField(10);
        JTextField dateToField = new JTextField(10);
        JTextField minBalanceField = new JTextField(10);
        JTextField maxBalanceField = new JTextField(10);
        JComboBox<String> activeCombo = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
        JTextField addressField = new JTextField(15);
        
        // Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel header = new JLabel("Filter Customers By:");
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(header, gbc);
        
        gbc.gridwidth = 1;
        
        // Name filter
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        // Active status
        gbc.gridx = 2;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        panel.add(activeCombo, gbc);
        
        // Date range
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Created From:"), gbc);
        gbc.gridx = 1;
        panel.add(dateFromField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("To:"), gbc);
        gbc.gridx = 3;
        panel.add(dateToField, gbc);
        
        // Balance range
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Min Balance:"), gbc);
        gbc.gridx = 1;
        panel.add(minBalanceField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Max Balance:"), gbc);
        gbc.gridx = 3;
        panel.add(maxBalanceField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(addressField, gbc);
        
        // Note
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        JLabel note = new JLabel("<html><i>Date format: YYYY-MM-DD. Leave empty to skip filter.</i></html>");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        panel.add(note, gbc);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, "Filter Customers",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            CustomerSearchCriteria.Builder builder = CustomerSearchCriteria.builder();
            
            String name = nameField.getText().trim();
            if (!name.isEmpty()) builder.nameFilter(name);
            
            String dateFrom = dateFromField.getText().trim();
            if (!dateFrom.isEmpty()) builder.createDateFrom(dateFrom);
            
            String dateTo = dateToField.getText().trim();
            if (!dateTo.isEmpty()) builder.createDateTo(dateTo);
            
            String minBalance = minBalanceField.getText().trim();
            if (!minBalance.isEmpty()) {
                try {
                    builder.minBalance(Double.parseDouble(minBalance));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Invalid minimum balance.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
            
            String maxBalance = maxBalanceField.getText().trim();
            if (!maxBalance.isEmpty()) {
                try {
                    builder.maxBalance(Double.parseDouble(maxBalance));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Invalid maximum balance.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
            
            String activeSelection = (String) activeCombo.getSelectedItem();
            if ("Active".equals(activeSelection)) {
                builder.active(true);
            } else if ("Inactive".equals(activeSelection)) {
                builder.active(false);
            }
            
            String address = addressField.getText().trim();
            if (!address.isEmpty()) builder.addressFilter(address);
            
            return builder.build();
        }
        
        return null;
    }
    
    // ==================== TRANSACTION SEARCH DIALOG ====================
    
    /**
     * Shows a dialog for searching transactions by ID
     * @param parent the parent component
     * @return TransactionSearchCriteria with search term, or null if cancelled
     */
    public static TransactionSearchCriteria showTransactionSearchDialog(Component parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField transactionIdField = new JTextField(25);
        
        // Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel header = new JLabel("Search Transaction By:");
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(header, gbc);
        
        gbc.gridwidth = 1;
        
        // Transaction ID
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Transaction ID:"), gbc);
        gbc.gridx = 1;
        panel.add(transactionIdField, gbc);
        
        // Note
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel note = new JLabel("<html><i>Enter full or partial transaction ID (e.g., TXN-XXXXXXXX)</i></html>");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        panel.add(note, gbc);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, "Search Transaction",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String transactionId = transactionIdField.getText().trim();
            
            if (transactionId.isEmpty()) {
                return null;
            }
            
            return TransactionSearchCriteria.builder()
                .transactionId(transactionId)
                .build();
        }
        
        return null;
    }
    
    // ==================== TRANSACTION FILTER DIALOG ====================
    
    /**
     * Shows a dialog for filtering transactions
     * @param parent the parent component
     * @return TransactionSearchCriteria with filter settings, or null if cancelled
     */
    public static TransactionSearchCriteria showTransactionFilterDialog(Component parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField minAmountField = new JTextField(10);
        JTextField maxAmountField = new JTextField(10);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"All", "DEPOSIT", "WITHDRAWAL", "TRANSFER"});
        JTextField receiverField = new JTextField(15);
        JTextField senderField = new JTextField(15);
        JTextField dateFromField = new JTextField(10);
        JTextField dateToField = new JTextField(10);
        
        // Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel header = new JLabel("Filter Transactions By:");
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(header, gbc);
        
        gbc.gridwidth = 1;
        
        // Amount range
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Min Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(minAmountField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Max Amount:"), gbc);
        gbc.gridx = 3;
        panel.add(maxAmountField, gbc);
        
        // Type
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);
        
        // Sender
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Sender ID:"), gbc);
        gbc.gridx = 1;
        panel.add(senderField, gbc);
        
        // Receiver
        gbc.gridx = 2;
        panel.add(new JLabel("Receiver ID:"), gbc);
        gbc.gridx = 3;
        panel.add(receiverField, gbc);
        
        // Date range
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Date From:"), gbc);
        gbc.gridx = 1;
        panel.add(dateFromField, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Date To:"), gbc);
        gbc.gridx = 3;
        panel.add(dateToField, gbc);
        
        // Note
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        JLabel note = new JLabel("<html><i>Date format: YYYY-MM-DD. Leave empty to skip filter.</i></html>");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        panel.add(note, gbc);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, "Filter Transactions",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            TransactionSearchCriteria.Builder builder = TransactionSearchCriteria.builder();
            
            String minAmount = minAmountField.getText().trim();
            if (!minAmount.isEmpty()) {
                try {
                    builder.minAmount(Double.parseDouble(minAmount));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Invalid minimum amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
            
            String maxAmount = maxAmountField.getText().trim();
            if (!maxAmount.isEmpty()) {
                try {
                    builder.maxAmount(Double.parseDouble(maxAmount));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Invalid maximum amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
            
            String typeSelection = (String) typeCombo.getSelectedItem();
            if (!"All".equals(typeSelection)) {
                builder.type(typeSelection);
            }
            
            String sender = senderField.getText().trim();
            if (!sender.isEmpty()) builder.senderId(sender);
            
            String receiver = receiverField.getText().trim();
            if (!receiver.isEmpty()) builder.receiverId(receiver);
            
            String dateFrom = dateFromField.getText().trim();
            if (!dateFrom.isEmpty()) builder.dateFrom(dateFrom);
            
            String dateTo = dateToField.getText().trim();
            if (!dateTo.isEmpty()) builder.dateTo(dateTo);
            
            return builder.build();
        }
        
        return null;
    }
    
    /**
     * Shows a combined search and filter dialog for customers with tabs
     * @param parent the parent component
     * @return CustomerSearchCriteria or null if cancelled
     */
    public static CustomerSearchCriteria showCustomerSearchFilterDialog(Component parent) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Search Panel
        JPanel searchPanel = createCustomerSearchPanel();
        tabbedPane.addTab("Search", searchPanel);
        
        // Filter Panel
        JPanel filterPanel = createCustomerFilterPanel();
        tabbedPane.addTab("Filter", filterPanel);
        
        int result = JOptionPane.showConfirmDialog(parent, tabbedPane, "Search & Filter Customers",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int selectedTab = tabbedPane.getSelectedIndex();
            
            if (selectedTab == 0) {
                // Search tab
                return extractSearchCriteriaFromPanel(searchPanel);
            } else {
                // Filter tab
                return extractFilterCriteriaFromPanel(filterPanel);
            }
        }
        
        return null;
    }
    
    /**
     * Shows a combined search and filter dialog for transactions with tabs
     * @param parent the parent component
     * @return TransactionSearchCriteria or null if cancelled
     */
    public static TransactionSearchCriteria showTransactionSearchFilterDialog(Component parent) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Search Panel
        JPanel searchPanel = createTransactionSearchPanel();
        tabbedPane.addTab("Search", searchPanel);
        
        // Filter Panel
        JPanel filterPanel = createTransactionFilterPanel();
        tabbedPane.addTab("Filter", filterPanel);
        
        int result = JOptionPane.showConfirmDialog(parent, tabbedPane, "Search & Filter Transactions",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int selectedTab = tabbedPane.getSelectedIndex();
            
            if (selectedTab == 0) {
                // Search tab
                return extractTransactionSearchCriteriaFromPanel(searchPanel);
            } else {
                // Filter tab
                return extractTransactionFilterCriteriaFromPanel(filterPanel);
            }
        }
        
        return null;
    }
    
    // Helper methods for creating panels
    private static JPanel createCustomerSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        JTextField accNoField = new JTextField(20);
        accNoField.setName("accNo");
        panel.add(accNoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(20);
        idField.setName("id");
        panel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setName("name");
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        phoneField.setName("phone");
        panel.add(phoneField, gbc);
        
        return panel;
    }
    
    private static JPanel createCustomerFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        nameField.setName("nameFilter");
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Created From:"), gbc);
        gbc.gridx = 1;
        JTextField dateFromField = new JTextField(10);
        dateFromField.setName("dateFrom");
        panel.add(dateFromField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Created To:"), gbc);
        gbc.gridx = 1;
        JTextField dateToField = new JTextField(10);
        dateToField.setName("dateTo");
        panel.add(dateToField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Min Balance:"), gbc);
        gbc.gridx = 1;
        JTextField minBalanceField = new JTextField(10);
        minBalanceField.setName("minBalance");
        panel.add(minBalanceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Max Balance:"), gbc);
        gbc.gridx = 1;
        JTextField maxBalanceField = new JTextField(10);
        maxBalanceField.setName("maxBalance");
        panel.add(maxBalanceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> activeCombo = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
        activeCombo.setName("active");
        panel.add(activeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        JTextField addressField = new JTextField(15);
        addressField.setName("address");
        panel.add(addressField, gbc);
        
        return panel;
    }
    
    private static JPanel createTransactionSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Transaction ID:"), gbc);
        gbc.gridx = 1;
        JTextField transactionIdField = new JTextField(25);
        transactionIdField.setName("transactionId");
        panel.add(transactionIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel note = new JLabel("<html><i>Enter full or partial transaction ID</i></html>");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        panel.add(note, gbc);
        
        return panel;
    }
    
    private static JPanel createTransactionFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Min Amount:"), gbc);
        gbc.gridx = 1;
        JTextField minAmountField = new JTextField(10);
        minAmountField.setName("minAmount");
        panel.add(minAmountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Max Amount:"), gbc);
        gbc.gridx = 1;
        JTextField maxAmountField = new JTextField(10);
        maxAmountField.setName("maxAmount");
        panel.add(maxAmountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"All", "DEPOSIT", "WITHDRAWAL", "TRANSFER"});
        typeCombo.setName("type");
        panel.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Sender ID:"), gbc);
        gbc.gridx = 1;
        JTextField senderField = new JTextField(15);
        senderField.setName("sender");
        panel.add(senderField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Receiver ID:"), gbc);
        gbc.gridx = 1;
        JTextField receiverField = new JTextField(15);
        receiverField.setName("receiver");
        panel.add(receiverField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Date From:"), gbc);
        gbc.gridx = 1;
        JTextField dateFromField = new JTextField(10);
        dateFromField.setName("dateFrom");
        panel.add(dateFromField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Date To:"), gbc);
        gbc.gridx = 1;
        JTextField dateToField = new JTextField(10);
        dateToField.setName("dateTo");
        panel.add(dateToField, gbc);
        
        return panel;
    }
    
    private static CustomerSearchCriteria extractSearchCriteriaFromPanel(JPanel panel) {
        CustomerSearchCriteria.Builder builder = CustomerSearchCriteria.builder();
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField field) {
                String value = field.getText().trim();
                if (!value.isEmpty()) {
                    switch (field.getName()) {
                        case "accNo" -> builder.accNo(value);
                        case "id" -> builder.id(value);
                        case "name" -> builder.name(value);
                        case "phone" -> builder.phoneNumber(value);
                    }
                }
            }
        }
        
        return builder.build();
    }
    
    private static CustomerSearchCriteria extractFilterCriteriaFromPanel(JPanel panel) {
        CustomerSearchCriteria.Builder builder = CustomerSearchCriteria.builder();
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField field) {
                String value = field.getText().trim();
                if (!value.isEmpty()) {
                    switch (field.getName()) {
                        case "nameFilter" -> builder.nameFilter(value);
                        case "dateFrom" -> builder.createDateFrom(value);
                        case "dateTo" -> builder.createDateTo(value);
                        case "minBalance" -> {
                            try { builder.minBalance(Double.parseDouble(value)); } catch (NumberFormatException ignored) {}
                        }
                        case "maxBalance" -> {
                            try { builder.maxBalance(Double.parseDouble(value)); } catch (NumberFormatException ignored) {}
                        }
                        case "address" -> builder.addressFilter(value);
                    }
                }
            } else if (comp instanceof JComboBox<?> combo && "active".equals(combo.getName())) {
                String selected = (String) combo.getSelectedItem();
                if ("Active".equals(selected)) builder.active(true);
                else if ("Inactive".equals(selected)) builder.active(false);
            }
        }
        
        return builder.build();
    }
    
    private static TransactionSearchCriteria extractTransactionSearchCriteriaFromPanel(JPanel panel) {
        TransactionSearchCriteria.Builder builder = TransactionSearchCriteria.builder();
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField field && "transactionId".equals(field.getName())) {
                String value = field.getText().trim();
                if (!value.isEmpty()) {
                    builder.transactionId(value);
                }
            }
        }
        
        return builder.build();
    }
    
    private static TransactionSearchCriteria extractTransactionFilterCriteriaFromPanel(JPanel panel) {
        TransactionSearchCriteria.Builder builder = TransactionSearchCriteria.builder();
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField field) {
                String value = field.getText().trim();
                if (!value.isEmpty()) {
                    switch (field.getName()) {
                        case "minAmount" -> {
                            try { builder.minAmount(Double.parseDouble(value)); } catch (NumberFormatException ignored) {}
                        }
                        case "maxAmount" -> {
                            try { builder.maxAmount(Double.parseDouble(value)); } catch (NumberFormatException ignored) {}
                        }
                        case "sender" -> builder.senderId(value);
                        case "receiver" -> builder.receiverId(value);
                        case "dateFrom" -> builder.dateFrom(value);
                        case "dateTo" -> builder.dateTo(value);
                    }
                }
            } else if (comp instanceof JComboBox<?> combo && "type".equals(combo.getName())) {
                String selected = (String) combo.getSelectedItem();
                if (!"All".equals(selected)) {
                    builder.type(selected);
                }
            }
        }
        
        return builder.build();
    }
}
