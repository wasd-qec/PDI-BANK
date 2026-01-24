package GUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import Object.Customer;
import Database.Report;
import Service.CustomerService;
import Search.SearchCustomer;
import Search.CustomerSearchCriteria;

public class HomePageAdminAccount extends JFrame {
    private Database.CustomerInter customerImple = new Database.CustomerImple();
    private CustomerService customerService = new CustomerService();
    private SearchCustomer searchCustomer = new SearchCustomer();
    private JPanel accountsPanel;
    private RoundedTextField searchBar;

    public HomePageAdminAccount() {
        
        setTitle("Admin - Account Management");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(40, 65, 105));

        // ========== LEFT SIDE PANEL ==========
        JPanel sidePanel = new JPanel(null);
        sidePanel.setBounds(0, 0, 200, 650);
        sidePanel.setBackground(new Color(8, 25, 64));
        add(sidePanel);

        // LOGO
        ImageIcon logo = new ImageIcon("src\\GUI\\TMB_Logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(45, 40, 110, 110);
        sidePanel.add(logoLabel);

        // ACCOUNT BUTTON
        RoundedButton accountBtn = new RoundedButton("Account");
        accountBtn.setBounds(28, 200, 145, 40);
        accountBtn.setBackground(new Color(218, 186, 121));
        sidePanel.add(accountBtn);

        // TRANSACTION BUTTON
        RoundedButton transactionBtn = new RoundedButton("Transaction");
        transactionBtn.setBounds(28, 250, 145, 40);
        transactionBtn.setBackground(new Color(108, 130, 173));
        sidePanel.add(transactionBtn);

        // Report BUTTON
        RoundedButton reportBtn = new RoundedButton("Report");
        reportBtn.setBounds(28, 300, 145, 40);
        reportBtn.setBackground(new Color(108, 130, 173));
        sidePanel.add(reportBtn);

        // LOG OUT
        JLabel logoutLabel = new JLabel("Log out");
        logoutLabel.setForeground(Color.WHITE);
        logoutLabel.setBounds(30, 580, 100, 30);
        sidePanel.add(logoutLabel);

        // ========== SEARCH BAR ==========
        searchBar = new RoundedTextField(20, "Search account number");
        searchBar.setBounds(240, 30, 600, 40);
        searchBar.setBackground(new Color(235, 235, 235));
        add(searchBar);

        // SEARCH BUTTON
        RoundedButton searchBtn = new RoundedButton("Search");
        searchBtn.setBounds(850, 30, 100, 40);
        searchBtn.setBackground(new Color(108, 130, 173));
        searchBtn.addActionListener(e -> performSearch());
        add(searchBtn);

        // ========== TOP BUTTONS ==========
        RoundedButton createAccBtn = new RoundedButton("Create account");
        createAccBtn.setBounds(240, 80, 150, 40);
        createAccBtn.setBackground(new Color(218, 186, 121));
        add(createAccBtn);

        RoundedButton filterBtn = new RoundedButton("Filter by");
        filterBtn.setBounds(400, 80, 120, 40);
        filterBtn.setBackground(new Color(218, 186, 121));
        add(filterBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh");
        refreshBtn.setBounds(530, 80, 100, 40);
        refreshBtn.setBackground(new Color(108, 130, 173));
        refreshBtn.addActionListener(e -> loadCustomers());
        add(refreshBtn);

        // ========== ACCOUNTS TITLE ==========
        JLabel accLabel = new JLabel("Accounts");
        accLabel.setForeground(Color.WHITE);
        accLabel.setFont(new Font("Serif", Font.BOLD, 20));
        accLabel.setBounds(240, 130, 200, 40);
        add(accLabel);

        // ========== SCROLLABLE ACCOUNTS PANEL ==========
        accountsPanel = new JPanel();
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));
        accountsPanel.setBackground(new Color(40, 65, 105));

        JScrollPane scrollPane = new JScrollPane(accountsPanel);
        scrollPane.setBounds(240, 170, 710, 420);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        // Load initial data
        loadCustomers();

        logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // ========== CLICK ACTIONS ==========
        accountBtn.addActionListener(e -> {
            // Already on accounts page
        });

        transactionBtn.addActionListener(e -> {
            dispose();
            new HomePageAdminTran();
        });

        reportBtn.addActionListener(e -> {
            showReportDialog();
        });
        
        createAccBtn.addActionListener(e -> {
            new CreateAccBT(this);
        });

        filterBtn.addActionListener(e -> {
            new FilterCustomersDialog(this);
        });

        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginSelection();
            }
        });

        setVisible(true);
    }

    private void loadCustomers() {
        accountsPanel.removeAll();
        List<Customer> accounts = customerImple.getAllCustomers();
        
        if (accounts != null && !accounts.isEmpty()) {
            for (Customer account : accounts) {
                JPanel accBox = createAccountBox(account);
                accountsPanel.add(accBox);
                accountsPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noDataLabel = new JLabel("No accounts found");
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            accountsPanel.add(noDataLabel);
        }
        
        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private void loadFilteredCustomers(List<Customer> accounts) {
        accountsPanel.removeAll();
        
        if (accounts != null && !accounts.isEmpty()) {
            for (Customer account : accounts) {
                JPanel accBox = createAccountBox(account);
                accountsPanel.add(accBox);
                accountsPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noDataLabel = new JLabel("No accounts found matching criteria");
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            accountsPanel.add(noDataLabel);
        }
        
        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private void performSearch() {
        String searchText = searchBar.getText().trim();
        if (searchText.isEmpty() || searchText.equals("Search account number")) {
            loadCustomers();
            return;
        }

        Customer customer = customerImple.getCustomerByAccNo(searchText);
        accountsPanel.removeAll();
        
        if (customer != null) {
            JPanel accBox = createAccountBox(customer);
            accountsPanel.add(accBox);
        } else {
            // Try searching by name
            List<Customer> customers = searchCustomer.findByName(searchText);
            if (!customers.isEmpty()) {
                for (Customer c : customers) {
                    JPanel accBox = createAccountBox(c);
                    accountsPanel.add(accBox);
                    accountsPanel.add(Box.createVerticalStrut(10));
                }
            } else {
                JLabel noDataLabel = new JLabel("No accounts found for: " + searchText);
                noDataLabel.setForeground(Color.WHITE);
                noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                accountsPanel.add(noDataLabel);
            }
        }
        
        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private JPanel createAccountBox(Customer account) {
        JPanel accBox = new JPanel(null);
        accBox.setPreferredSize(new Dimension(690, 60));
        accBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        accBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        accBox.setBackground(new Color(235, 235, 235));
        
        JLabel nameLabel = new JLabel(account.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(new Color(30, 50, 85));
        nameLabel.setBounds(20, 10, 150, 20);
        accBox.add(nameLabel);
        
        JLabel accNumLabel = new JLabel(account.getAccNo());
        accNumLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accNumLabel.setForeground(new Color(100, 100, 100));
        accNumLabel.setBounds(20, 35, 150, 15);
        accBox.add(accNumLabel);

        // Status indicator
        JLabel statusLabel = new JLabel(account.isActive() ? "Active" : "Inactive");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(account.isActive() ? new Color(34, 139, 34) : new Color(220, 20, 60));
        statusLabel.setBounds(180, 35, 60, 15);
        accBox.add(statusLabel);
        
        String balanceStr = String.format("$%.2f", account.getBalance());
        JLabel balanceLabel = new JLabel(balanceStr);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        if (account.getBalance() < 0) {
            balanceLabel.setForeground(new Color(220, 20, 60)); // Red for negative
        } else {
            balanceLabel.setForeground(new Color(34, 139, 34)); // Green for positive
        }
        balanceLabel.setBounds(480, 20, 100, 25);
        accBox.add(balanceLabel);

        // Action buttons
        JButton viewBtn = new JButton("View");
        viewBtn.setBounds(590, 15, 70, 30);
        viewBtn.setBackground(new Color(108, 130, 173));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> showAccountDetails(account));
        accBox.add(viewBtn);
        
        return accBox;
    }

    private void showAccountDetails(Customer account) {
        JDialog detailDialog = new JDialog(this, "Account Details", true);
        detailDialog.setSize(450, 420);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setResizable(false);
        detailDialog.getContentPane().setBackground(new Color(245, 240, 235));
        detailDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Account Details");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 200, 25);
        detailDialog.add(titleLabel);

        String[][] details = {
            {"Account Number", account.getAccNo()},
            {"Customer ID", account.getID()},
            {"Name", account.getName()},
            {"Phone Number", String.valueOf(account.getPhoneNumber())},
            {"Address", account.getAddress() != null ? account.getAddress() : "N/A"},
            {"Birth Date", account.getBirthDate() != null ? account.getBirthDate() : "N/A"},
            {"Account Created", account.getCreateDate()},
            {"Balance", String.format("$%.2f", account.getBalance())},
            {"Status", account.isActive() ? "Active" : "Deactivated"}
        };

        int yPos = 60;
        for (String[] detail : details) {
            JLabel labelName = new JLabel(detail[0] + ":");
            labelName.setForeground(new Color(30, 50, 85));
            labelName.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelName.setBounds(20, yPos, 150, 20);
            detailDialog.add(labelName);

            JLabel labelValue = new JLabel(detail[1]);
            labelValue.setForeground(new Color(100, 100, 100));
            labelValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            labelValue.setBounds(180, yPos, 250, 20);
            detailDialog.add(labelValue);

            yPos += 28;
        }

        // Action buttons
        RoundedButton editBtn = new RoundedButton("Edit");
        editBtn.setBounds(10, 330, 100, 35);
        editBtn.setBackground(new Color(108, 130, 173));
        editBtn.addActionListener(e -> {
            detailDialog.dispose();
            showEditProfileDialog(account);
        });
        detailDialog.add(editBtn);

        RoundedButton activateBtn = new RoundedButton(account.isActive() ? "Deactivate" : "Activate");
        activateBtn.setBounds(140, 330, 110, 35);
        activateBtn.setBackground(account.isActive() ? new Color(220, 20, 60) : new Color(34, 139, 34));
        activateBtn.addActionListener(e -> {
            if (account.isActive()) {
                customerImple.DeactivateCustomer(account.getAccNo());
            } else {
                customerImple.ActivateCustomer(account.getAccNo());
            }
            detailDialog.dispose();
            loadCustomers();
            JOptionPane.showMessageDialog(this, 
                "Account " + (account.isActive() ? "deactivated" : "activated") + " successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        detailDialog.add(activateBtn);

        RoundedButton deleteBtn = new RoundedButton("Delete");
        deleteBtn.setBounds(280, 330, 100, 35);
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this customer account?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                customerImple.delete(account.getID());
                detailDialog.dispose();
                loadCustomers();
                JOptionPane.showMessageDialog(this,
                    "Customer account deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        detailDialog.add(deleteBtn);
        
        detailDialog.setVisible(true);
    }

    // ========== RoundedPanel ==========
    class RoundedPanel extends JPanel {
        private int radius;

        RoundedPanel(int r) {
            radius = r;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    // ========== RoundedButton ==========
    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setOpaque(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ========== RoundedTextField (search bar) ==========
    class RoundedTextField extends JTextField {
        private String placeholder;
        private int radius;

        RoundedTextField(int r, String placeholder) {
            this.radius = r;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                    }
                }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);

            if (getText().isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.setFont(getFont());
                g2.drawString(placeholder, 12, getHeight() / 2 + 5);
            }
            g2.dispose();
        }
    }

    class CreateAccBT extends JDialog {
        private JTextField userField;
        private JTextField accField;
        private JTextField passField;
        private JTextField idField;
        private JTextField phoneField;
        private JTextField addressField;
        private JTextField birthField;
        private JTextField balanceField;

        public CreateAccBT(JFrame parent) {
            super(parent, "Creating Account", true);
            setSize(510, 550);
            setLocationRelativeTo(parent);
            setResizable(false);
            setLayout(null);
            getContentPane().setBackground(Color.WHITE);

            // Title
            JLabel title = new JLabel("Creating Account");
            title.setFont(new Font("Serif", Font.BOLD, 28));
            title.setForeground(new Color(30, 50, 85));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setBounds(0, 15, 500, 40);
            add(title);

            // Divider line
            JPanel line = new JPanel();
            line.setBackground(new Color(30, 50, 85));
            line.setBounds(30, 62, 440, 1);
            add(line);

            int yPos = 75;
            int fieldHeight = 30;
            int labelHeight = 18;
            int gap = 55;

            // Customer ID
            JLabel idLabel = new JLabel("Customer ID");
            idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            idLabel.setForeground(new Color(30, 50, 85));
            idLabel.setBounds(30, yPos, 200, labelHeight);
            add(idLabel);
            idField = createStyledField("Enter customer ID");
            idField.setBounds(30, yPos + labelHeight, 210, fieldHeight);
            add(idField);

            // Account Number
            JLabel accLabel = new JLabel("Account Number");
            accLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            accLabel.setForeground(new Color(30, 50, 85));
            accLabel.setBounds(260, yPos, 200, labelHeight);
            add(accLabel);
            accField = createStyledField("Enter account number");
            accField.setBounds(260, yPos + labelHeight, 210, fieldHeight);
            add(accField);

            yPos += gap;

            // Username
            JLabel userLabel = new JLabel("Username");
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            userLabel.setForeground(new Color(30, 50, 85));
            userLabel.setBounds(30, yPos, 200, labelHeight);
            add(userLabel);
            userField = createStyledField("Enter username");
            userField.setBounds(30, yPos + labelHeight, 440, fieldHeight);
            add(userField);

            yPos += gap;

            // Password
            JLabel passLabel = new JLabel("Password");
            passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            passLabel.setForeground(new Color(30, 50, 85));
            passLabel.setBounds(30, yPos, 200, labelHeight);
            add(passLabel);
            passField = createStyledField("Enter password");
            passField.setBounds(30, yPos + labelHeight, 440, fieldHeight);
            add(passField);

            yPos += gap;

            // Phone Number
            JLabel phoneLabel = new JLabel("Phone Number");
            phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            phoneLabel.setForeground(new Color(30, 50, 85));
            phoneLabel.setBounds(30, yPos, 200, labelHeight);
            add(phoneLabel);
            phoneField = createStyledField("Enter phone number");
            phoneField.setBounds(30, yPos + labelHeight, 210, fieldHeight);
            add(phoneField);

            // Initial Balance
            JLabel balanceLabel = new JLabel("Initial Balance");
            balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            balanceLabel.setForeground(new Color(30, 50, 85));
            balanceLabel.setBounds(260, yPos, 200, labelHeight);
            add(balanceLabel);
            balanceField = createStyledField("0.00");
            balanceField.setBounds(260, yPos + labelHeight, 210, fieldHeight);
            add(balanceField);

            yPos += gap;

            // Address
            JLabel addressLabel = new JLabel("Address");
            addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            addressLabel.setForeground(new Color(30, 50, 85));
            addressLabel.setBounds(30, yPos, 200, labelHeight);
            add(addressLabel);
            addressField = createStyledField("Enter address");
            addressField.setBounds(30, yPos + labelHeight, 440, fieldHeight);
            add(addressField);

            yPos += gap;

            // Birth Date
            JLabel birthLabel = new JLabel("Birth Date (YYYY-MM-DD)");
            birthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            birthLabel.setForeground(new Color(30, 50, 85));
            birthLabel.setBounds(30, yPos, 200, labelHeight);
            add(birthLabel);
            birthField = createStyledField("1990-01-01");
            birthField.setBounds(30, yPos + labelHeight, 210, fieldHeight);
            add(birthField);

            yPos += gap + 15;

            // Cancel Button
            RoundedButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBounds(200, yPos, 110, 35);
            cancelBtn.setBackground(new Color(70, 90, 130));
            cancelBtn.setForeground(Color.WHITE);
            cancelBtn.addActionListener(e -> dispose());
            add(cancelBtn);

            // Create Button
            RoundedButton createBtn = new RoundedButton("Create");
            createBtn.setBounds(320, yPos, 110, 35);
            createBtn.setBackground(new Color(8, 25, 64));
            createBtn.setForeground(Color.WHITE);
            createBtn.addActionListener(e -> createAccount());
            add(createBtn);

            setVisible(true);
        }

        private JTextField createStyledField(String placeholder) {
            JTextField field = new JTextField();
            field.setBackground(new Color(218, 186, 121));
            field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            field.setText(placeholder);
            field.setForeground(new Color(100, 100, 100));
            
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setForeground(new Color(100, 100, 100));
                        field.setText(placeholder);
                    }
                }
            });
            return field;
        }

        private void createAccount() {
            try {
                String id = idField.getText().trim();
                String accNo = accField.getText().trim();
                String name = userField.getText().trim();
                String password = passField.getText().trim();
                String phoneStr = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String birthDate = birthField.getText().trim();
                String balanceStr = balanceField.getText().trim();

                // Validation
                if (id.isEmpty() || id.equals("Enter customer ID")) {
                    JOptionPane.showMessageDialog(this, "Please enter customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (accNo.isEmpty() || accNo.equals("Enter account number")) {
                    JOptionPane.showMessageDialog(this, "Please enter account number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (name.isEmpty() || name.equals("Enter username")) {
                    JOptionPane.showMessageDialog(this, "Please enter username.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.isEmpty() || password.equals("Enter password")) {
                    JOptionPane.showMessageDialog(this, "Please enter password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if ID or AccNo already exists
                if (customerImple.existsid(id)) {
                    JOptionPane.showMessageDialog(this, "Customer ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (customerImple.existsAccNo(accNo)) {
                    JOptionPane.showMessageDialog(this, "Account number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int phone = 0;
                if (!phoneStr.isEmpty() && !phoneStr.equals("Enter phone number")) {
                    phone = Integer.parseInt(phoneStr);
                }

                double balance = 0.0;
                if (!balanceStr.isEmpty() && !balanceStr.equals("0.00")) {
                    balance = Double.parseDouble(balanceStr);
                }

                if (address.equals("Enter address")) address = "";
                if (birthDate.equals("1990-01-01")) birthDate = "1990-01-01";

                Customer newCustomer = customerService.createCustomerAccount(
                    id, accNo, name, password, phone, address, balance, birthDate
                );

                if (newCustomer != null) {
                    customerImple.save(newCustomer);
                    dispose();
                    loadCustomers();
                    JOptionPane.showMessageDialog(HomePageAdminAccount.this, 
                        "Account created successfully!\nAccount Number: " + accNo, 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for phone and balance.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void showEditProfileDialog(Customer customer) {
        JDialog editDialog = new JDialog(this, "Edit Profile", true);
        editDialog.setSize(420, 420);
        editDialog.setLocationRelativeTo(this);
        editDialog.setResizable(false);
        editDialog.getContentPane().setBackground(new Color(245, 240, 235));
        editDialog.setLayout(null);

        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 50, 85));
        titleLabel.setBounds(20, 20, 200, 25);
        editDialog.add(titleLabel);

        // Name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(new Color(30, 50, 85));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nameLabel.setBounds(20, 60, 150, 15);
        editDialog.add(nameLabel);

        JTextField nameField = new JTextField(customer.getName());
        nameField.setBounds(20, 80, 380, 30);
        nameField.setBackground(new Color(218, 186, 121));
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editDialog.add(nameField);

        // ID
        JLabel idLabel = new JLabel("Customer ID");
        idLabel.setForeground(new Color(30, 50, 85));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        idLabel.setBounds(20, 120, 150, 15);
        editDialog.add(idLabel);

        JTextField idField = new JTextField(customer.getID());
        idField.setBounds(20, 140, 180, 30);
        idField.setBackground(new Color(218, 186, 121));
        idField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editDialog.add(idField);

        // Birth Date
        JLabel birthLabel = new JLabel("Birth Date (YYYY-MM-DD)");
        birthLabel.setForeground(new Color(30, 50, 85));
        birthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        birthLabel.setBounds(220, 120, 180, 15);
        editDialog.add(birthLabel);

        JTextField birthField = new JTextField(customer.getBirthDate() != null ? customer.getBirthDate() : "");
        birthField.setBounds(220, 140, 180, 30);
        birthField.setBackground(new Color(218, 186, 121));
        birthField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editDialog.add(birthField);

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setForeground(new Color(30, 50, 85));
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        phoneLabel.setBounds(20, 180, 150, 15);
        editDialog.add(phoneLabel);

        JTextField phoneField = new JTextField(String.valueOf(customer.getPhoneNumber()));
        phoneField.setBounds(20, 200, 380, 30);
        phoneField.setBackground(new Color(218, 186, 121));
        phoneField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editDialog.add(phoneField);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setForeground(new Color(30, 50, 85));
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        addressLabel.setBounds(20, 240, 150, 15);
        editDialog.add(addressLabel);

        JTextField addressField = new JTextField(customer.getAddress() != null ? customer.getAddress() : "");
        addressField.setBounds(20, 260, 380, 30);
        addressField.setBackground(new Color(218, 186, 121));
        addressField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editDialog.add(addressField);

        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBounds(80, 310, 100, 35);
        cancelBtn.setBackground(new Color(108, 130, 173));
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> editDialog.dispose());
        editDialog.add(cancelBtn);

        RoundedButton saveBtn = new RoundedButton("Save");
        saveBtn.setBackground(new Color(8, 25, 64));
        saveBtn.setBounds(240, 310, 100, 35);
        saveBtn.addActionListener(e -> {
            try {
                String newName = nameField.getText().trim();
                String newId = idField.getText().trim();
                String newBirth = birthField.getText().trim();
                String newPhone = phoneField.getText().trim();
                String newAddress = addressField.getText().trim();

                // Validation
                if (newName.isEmpty() || newId.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
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
                customer.setName(newName);
                customer.setID(newId);
                customer.setBirthDate(newBirth);
                customer.setPhoneNumber(phoneNumber);
                customer.setAddress(newAddress);

                // Save to database using updateCustomerPro
                customerImple.updateCustomerPro(customer);

                // Optionally refresh customer data from database (avoid reassigning lambda-captured var)
                Customer refreshed = customerImple.getCustomerByAccNo(customer.getAccNo());

                // Refresh UI
                loadCustomers();

                editDialog.dispose();
                JOptionPane.showMessageDialog(this, "Profile Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Failed to update profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        editDialog.add(saveBtn);

        editDialog.setVisible(true);
    }
    class FilterCustomersDialog extends JDialog {
    
        public FilterCustomersDialog(JFrame parent) {
            super(parent, "Filter Customers", true);
            setSize(550, 480);
            setLocationRelativeTo(parent);
            setResizable(false);
            setLayout(null);
            getContentPane().setBackground(new Color(245, 240, 235));
            
            // Title
            JLabel titleLabel = new JLabel("Filter Customers By:");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(30, 50, 85));
            titleLabel.setBounds(30, 20, 300, 25);
            add(titleLabel);
            
            // Name
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            nameLabel.setForeground(new Color(30, 50, 85));
            nameLabel.setBounds(30, 60, 100, 20);
            add(nameLabel);

            JTextField nameField = new JTextField();
            nameField.setBounds(30, 85, 220, 35);
            nameField.setBackground(new Color(218, 186, 121));
            nameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(nameField);

            // Status
            JLabel statusLabel = new JLabel("Status:");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusLabel.setForeground(new Color(30, 50, 85));
            statusLabel.setBounds(280, 60, 100, 20);
            add(statusLabel);

            String[] statusOptions = {"All", "Active", "Deactivated"};
            JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
            statusCombo.setBounds(280, 85, 240, 35);
            statusCombo.setBackground(new Color(218, 186, 121));
            add(statusCombo);

            // Created From
            JLabel createdLabel = new JLabel("Created From:");
            createdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            createdLabel.setForeground(new Color(30, 50, 85));
            createdLabel.setBounds(30, 135, 100, 20);
            add(createdLabel);

            JTextField createdFromField = new JTextField();
            createdFromField.setBounds(30, 160, 220, 35);
            createdFromField.setBackground(new Color(218, 186, 121));
            createdFromField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(createdFromField);

            // Created To
            JLabel toLabel = new JLabel("To:");
            toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            toLabel.setForeground(new Color(30, 50, 85));
            toLabel.setBounds(280, 135, 100, 20);
            add(toLabel);

            JTextField createdToField = new JTextField();
            createdToField.setBounds(280, 160, 240, 35);
            createdToField.setBackground(new Color(218, 186, 121));
            createdToField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(createdToField);

            // Min Balance
            JLabel minBalanceLabel = new JLabel("Min Balance:");
            minBalanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            minBalanceLabel.setForeground(new Color(30, 50, 85));
            minBalanceLabel.setBounds(30, 210, 100, 20);
            add(minBalanceLabel);

            JTextField minBalanceField = new JTextField();
            minBalanceField.setBounds(30, 235, 220, 35);
            minBalanceField.setBackground(new Color(218, 186, 121));
            minBalanceField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(minBalanceField);

            // Max Balance
            JLabel maxBalanceLabel = new JLabel("Max Balance:");
            maxBalanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            maxBalanceLabel.setForeground(new Color(30, 50, 85));
            maxBalanceLabel.setBounds(280, 210, 100, 20);
            add(maxBalanceLabel);

            JTextField maxBalanceField = new JTextField();
            maxBalanceField.setBounds(280, 235, 240, 35);
            maxBalanceField.setBackground(new Color(218, 186, 121));
            maxBalanceField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(maxBalanceField);

            // Address
            JLabel addressLabel = new JLabel("Address:");
            addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            addressLabel.setForeground(new Color(30, 50, 85));
            addressLabel.setBounds(30, 285, 100, 20);
            add(addressLabel);

            JTextField addressField = new JTextField();
            addressField.setBounds(30, 310, 490, 35);
            addressField.setBackground(new Color(218, 186, 121));
            addressField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(addressField);

            // Info text
            JLabel infoLabel = new JLabel("Date format: YYYY-MM-DD. Leave empty to skip filter.");
            infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            infoLabel.setForeground(new Color(100, 100, 100));
            infoLabel.setBounds(30, 355, 490, 15);
            add(infoLabel);

            // Cancel Button
            RoundedButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBounds(220, 385, 120, 38);
            cancelBtn.setBackground(new Color(108, 130, 173));
            cancelBtn.addActionListener(e -> dispose());
            add(cancelBtn);

            // OK Button
            RoundedButton okBtn = new RoundedButton("OK");
            okBtn.setBounds(360, 385, 120, 38);
            okBtn.setBackground(new Color(8, 25, 64));
            okBtn.addActionListener(e -> {
                CustomerSearchCriteria.Builder builder = CustomerSearchCriteria.builder();

                String nameFilter = nameField.getText().trim();
                if (!nameFilter.isEmpty()) builder.nameFilter(nameFilter);

                String status = (String) statusCombo.getSelectedItem();
                if ("Active".equals(status)) builder.active(true);
                else if ("Deactivated".equals(status)) builder.active(false);

                String createFrom = createdFromField.getText().trim();
                if (!createFrom.isEmpty()) builder.createDateFrom(createFrom);

                String createTo = createdToField.getText().trim();
                if (!createTo.isEmpty()) builder.createDateTo(createTo);

                String minBal = minBalanceField.getText().trim();
                if (!minBal.isEmpty()) {
                    try {
                        builder.minBalance(Double.parseDouble(minBal));
                    } catch (NumberFormatException ex) {}
                }

                String maxBal = maxBalanceField.getText().trim();
                if (!maxBal.isEmpty()) {
                    try {
                        builder.maxBalance(Double.parseDouble(maxBal));
                    } catch (NumberFormatException ex) {}
                }

                String addressFilter = addressField.getText().trim();
                if (!addressFilter.isEmpty()) builder.addressFilter(addressFilter);

                CustomerSearchCriteria criteria = builder.build();
                List<Customer> results = searchCustomer.filter(criteria);
                loadFilteredCustomers(results);
                dispose();
            });
            add(okBtn);
            
            setVisible(true);
        }
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
        double totalDeposit = report.getTotalDeposit();
        double totalWithdrawal = report.getTotalWithdrawal();
        double totalTransfer = report.getTotalTransfer();

        int totalUsers = report.getTotalUsers();
        int DeactivatedUsers = report.getDeactivatedUsers();
        int ActiveUsers = report.getActiveUsers();

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

        JLabel inLabel = new JLabel("Active Users");
        inLabel.setFont(new Font("Serif", Font.BOLD, 14));
        inLabel.setForeground(new Color(30, 50, 85));
        inLabel.setBounds(20, 12, 120, 20);
        summaryPanel.add(inLabel);

        JLabel inValue = new JLabel(String.format("%d", ActiveUsers));
        inValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inValue.setForeground(new Color(34, 139, 34));
        inValue.setBounds(20, 34, 200, 20);
        summaryPanel.add(inValue);

        JLabel outLabel = new JLabel("Deactivated Users");
        outLabel.setFont(new Font("Serif", Font.BOLD, 14));
        outLabel.setForeground(new Color(30, 50, 85));
        outLabel.setBounds(240, 12, 120, 20);
        summaryPanel.add(outLabel);

        JLabel outValue = new JLabel(String.format("%d", DeactivatedUsers));
        outValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        outValue.setForeground(new Color(220, 20, 60));
        outValue.setBounds(240, 34, 200, 20);
        summaryPanel.add(outValue);

        JLabel balLabel = new JLabel("Total Users");
        balLabel.setFont(new Font("Serif", Font.BOLD, 14));
        balLabel.setForeground(new Color(30, 50, 85));
        balLabel.setBounds(460, 12, 140, 20);
        summaryPanel.add(balLabel);

        JLabel balValue = new JLabel(String.format("%d", totalUsers));
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

        JLabel tinLabel = new JLabel("Transfer :");
        tinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tinLabel.setForeground(new Color(30, 50, 85));
        tinLabel.setBounds(20, 90, 200, 20);
        valuesPanel.add(tinLabel);

        JLabel tinVal = new JLabel(String.format("$%.2f", totalTransfer));
        tinVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tinVal.setForeground(new Color(34, 139, 34));
        tinVal.setBounds(220, 90, 200, 20);
        valuesPanel.add(tinVal);


        JLabel balLabel2 = new JLabel("Current Balance:");
        balLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        balLabel2.setForeground(new Color(30, 50, 85));
        balLabel2.setBounds(20, 160, 200, 20);
        valuesPanel.add(balLabel2);

        JLabel balVal2 = new JLabel(String.format("$%.2f", report.getTotalBalance()));
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

            double depR = report.getTotalDeposit(sStr, eStr);
            double wdrR = report.getTotalWithdrawal(sStr, eStr);
            double tinR = report.getTotalTransfer(sStr, eStr);

            depVal.setText(String.format("$%.2f", depR));
            wdrVal.setText(String.format("$%.2f", wdrR));
            tinVal.setText(String.format("$%.2f", tinR));
        });

        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.setBounds(300, 430, 100, 35);
        closeBtn.addActionListener(e -> reportDialog.dispose());
        reportDialog.add(closeBtn);

        reportDialog.setVisible(true);
    }
    public static void main(String[] args) {
        new HomePageAdminAccount();
    }
}
