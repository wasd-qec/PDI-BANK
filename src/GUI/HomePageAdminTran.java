package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import Object.Transaction;
import Object.TransactionSearchCriteria;
import Database.SearchTransaction;

public class HomePageAdminTran extends JFrame {
    private Database.TransactionInterface transactionImple = new Database.TransactionImplement();
    private SearchTransaction searchTransaction = new SearchTransaction();
    private JPanel transactionsPanel;
    private RoundedTextField searchBar;

    public HomePageAdminTran() {
        
        setTitle("Admin - Transaction Management");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(40, 65, 105));

        JPanel sidePanel = new JPanel(null);
        sidePanel.setBounds(0, 0, 200, 650);
        sidePanel.setBackground(new Color(8, 25, 64));
        add(sidePanel);

        ImageIcon logo = new ImageIcon("src\\GUI\\TMB_Logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(45, 40, 110, 110);
        sidePanel.add(logoLabel);

        RoundedButton accountBtn = new RoundedButton("Account");
        accountBtn.setBounds(28, 200, 145, 40);
        accountBtn.setBackground(new Color(108, 130, 173));
        sidePanel.add(accountBtn);

        RoundedButton transactionBtn = new RoundedButton("Transaction");
        transactionBtn.setBounds(28, 250, 145, 40);
        transactionBtn.setBackground(new Color(218, 186, 121));
        sidePanel.add(transactionBtn);

        RoundedButton reportBtn = new RoundedButton("Report");
        reportBtn.setBounds(28, 300, 145, 40);
        reportBtn.setBackground(new Color(108, 130, 173));
        sidePanel.add(reportBtn);

        JLabel logoutLabel = new JLabel("Log out");
        logoutLabel.setForeground(Color.WHITE);
        logoutLabel.setBounds(30, 580, 100, 30);
        sidePanel.add(logoutLabel);

        searchBar = new RoundedTextField(20, "Search transaction ID");
        searchBar.setBounds(240, 30, 600, 40);
        searchBar.setBackground(new Color(235, 235, 235));
        add(searchBar);

        RoundedButton searchBtn = new RoundedButton("Search");
        searchBtn.setBounds(850, 30, 100, 40);
        searchBtn.setBackground(new Color(108, 130, 173));
        searchBtn.addActionListener(e -> performSearch());
        add(searchBtn);

        RoundedButton createTranBtn = new RoundedButton("Create Transaction");
        createTranBtn.setBounds(240, 80, 200, 40);
        createTranBtn.setBackground(new Color(218, 186, 121));
        createTranBtn.addActionListener(e -> new TransactionTypeModal(this));
        add(createTranBtn);

        RoundedButton filterBtn = new RoundedButton("Filter by");
        filterBtn.setBounds(450, 80, 120, 40);
        filterBtn.setBackground(new Color(218, 186, 121));
        add(filterBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh");
        refreshBtn.setBounds(580, 80, 100, 40);
        refreshBtn.setBackground(new Color(108, 130, 173));
        refreshBtn.addActionListener(e -> loadTransactions());
        add(refreshBtn);

        JLabel tranLabel = new JLabel("Transactions");
        tranLabel.setForeground(Color.WHITE);
        tranLabel.setFont(new Font("Serif", Font.BOLD, 20));
        tranLabel.setBounds(240, 130, 200, 40);
        add(tranLabel);

        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        transactionsPanel.setBackground(new Color(40, 65, 105));

        JScrollPane scrollPane = new JScrollPane(transactionsPanel);
        scrollPane.setBounds(240, 170, 710, 420);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        loadTransactions();

        logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        accountBtn.addActionListener(e -> {
            dispose();
            new HomePageAdminAccount();
        });

        transactionBtn.addActionListener(e -> {
        });

        reportBtn.addActionListener(e -> {
            showReportDialog();
        });

        filterBtn.addActionListener(e -> {
            new FilterTransactionsDialog(this);
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

    private void loadTransactions() {
        transactionsPanel.removeAll();
        List<Transaction> transactions = transactionImple.ShowAllTransaction();
        
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction trans : transactions) {
                JPanel transBox = createTransactionBox(trans);
                transactionsPanel.add(transBox);
                transactionsPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noDataLabel = new JLabel("No transactions found");
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionsPanel.add(noDataLabel);
        }
        
        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    private void loadFilteredTransactions(List<Transaction> transactions) {
        transactionsPanel.removeAll();
        
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction trans : transactions) {
                JPanel transBox = createTransactionBox(trans);
                transactionsPanel.add(transBox);
                transactionsPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noDataLabel = new JLabel("No transactions found matching criteria");
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionsPanel.add(noDataLabel);
        }
        
        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    private void performSearch() {
        String searchText = searchBar.getText().trim();
        if (searchText.isEmpty() || searchText.equals("Search transaction ID")) {
            loadTransactions();
            return;
        }

        Transaction transaction = searchTransaction.findById(searchText);
        transactionsPanel.removeAll();
        
        if (transaction != null) {
            JPanel transBox = createTransactionBox(transaction);
            transactionsPanel.add(transBox);
        } else {
            JLabel noDataLabel = new JLabel("No transaction found for: " + searchText);
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionsPanel.add(noDataLabel);
        }
        
        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    void refreshTransactions() {
        loadTransactions();
    }

    private JPanel createTransactionBox(Transaction trans) {
        JPanel transBox = new JPanel(null);
        transBox.setPreferredSize(new Dimension(690, 60));
        transBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        transBox.setBackground(new Color(235, 235, 235));
        
        JLabel typeLabel = new JLabel(trans.getType());
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(new Color(30, 50, 85));
        typeLabel.setBounds(20, 8, 100, 18);
        transBox.add(typeLabel);
        
        JLabel txnIdLabel = new JLabel("ID: " + trans.getTransactionID());
        txnIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txnIdLabel.setForeground(new Color(80, 80, 80));
        txnIdLabel.setBounds(20, 28, 250, 13);
        transBox.add(txnIdLabel);
        
        JLabel senderLabel = new JLabel("Sender: " + trans.getSenderID());
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        senderLabel.setForeground(new Color(80, 80, 80));
        senderLabel.setBounds(20, 42, 250, 13);
        transBox.add(senderLabel);
        
        String amountStr;
        if (trans.getType().equals("DEPOSIT")) {
            amountStr = String.format("+$%.2f", trans.getAmount());
        } else if (trans.getType().equals("WITHDRAW")) {
            amountStr = String.format("-$%.2f", trans.getAmount());
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
        
        amountLabel.setBounds(530, 8, 100, 18);
        transBox.add(amountLabel);
        
        JLabel dateLabel = new JLabel(trans.getTimestamp());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setBounds(440, 28, 170, 12);
        transBox.add(dateLabel);
        
        JLabel receiverLabel = new JLabel("Receiver: " + trans.getReceiverID());
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        receiverLabel.setForeground(new Color(100, 100, 100));
        receiverLabel.setBounds(440, 42, 170, 12);
        transBox.add(receiverLabel);
        
        return transBox;
    }

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

    class FilterTransactionsDialog extends JDialog {
    
        public FilterTransactionsDialog(JFrame parent) {
            super(parent, "Filter Transactions", true);
            setSize(550, 480);
            setLocationRelativeTo(parent);
            setResizable(false);
            setLayout(null);
            getContentPane().setBackground(new Color(245, 240, 235));
            
            JLabel titleLabel = new JLabel("Filter Transactions By:");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(30, 50, 85));
            titleLabel.setBounds(30, 20, 300, 25);
            add(titleLabel);
            JLabel minAmountLabel = new JLabel("Min Amount:");
            minAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            minAmountLabel.setForeground(new Color(30, 50, 85));
            minAmountLabel.setBounds(30, 60, 100, 20);
            add(minAmountLabel);

            JTextField minAmountField = new JTextField();
            minAmountField.setBounds(30, 85, 220, 35);
            minAmountField.setBackground(new Color(218, 186, 121));
            minAmountField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(minAmountField);

            // Max Amount
            JLabel maxAmountLabel = new JLabel("Max Amount:");
            maxAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            maxAmountLabel.setForeground(new Color(30, 50, 85));
            maxAmountLabel.setBounds(280, 60, 100, 20);
            add(maxAmountLabel);

            JTextField maxAmountField = new JTextField();
            maxAmountField.setBounds(280, 85, 240, 35);
            maxAmountField.setBackground(new Color(218, 186, 121));
            maxAmountField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(maxAmountField);
            
            // Type
            JLabel typeLabel = new JLabel("Type:");
            typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            typeLabel.setForeground(new Color(30, 50, 85));
            typeLabel.setBounds(30, 135, 100, 20);
            add(typeLabel);

            String[] typeOptions = {"All", "DEPOSIT", "WITHDRAW", "TRANSFER"};
            JComboBox<String> typeCombo = new JComboBox<>(typeOptions);
            typeCombo.setBounds(30, 160, 220, 35);
            typeCombo.setBackground(new Color(218, 186, 121));
            add(typeCombo);

            // Sender ID
            JLabel senderLabel = new JLabel("Sender ID:");
            senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            senderLabel.setForeground(new Color(30, 50, 85));
            senderLabel.setBounds(30, 210, 100, 20);
            add(senderLabel);

            JTextField senderField = new JTextField();
            senderField.setBounds(30, 235, 220, 35);
            senderField.setBackground(new Color(218, 186, 121));
            senderField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(senderField);

            // Receiver ID
            JLabel receiverLabel = new JLabel("Receiver ID:");
            receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            receiverLabel.setForeground(new Color(30, 50, 85));
            receiverLabel.setBounds(280, 210, 100, 20);
            add(receiverLabel);

            JTextField receiverField = new JTextField();
            receiverField.setBounds(280, 235, 240, 35);
            receiverField.setBackground(new Color(218, 186, 121));
            receiverField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(receiverField);

            // Date From
            JLabel dateFromLabel = new JLabel("Date From:");
            dateFromLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateFromLabel.setForeground(new Color(30, 50, 85));
            dateFromLabel.setBounds(30, 285, 100, 20);
            add(dateFromLabel);

            JTextField dateFromField = new JTextField();
            dateFromField.setBounds(30, 310, 220, 35);
            dateFromField.setBackground(new Color(218, 186, 121));
            dateFromField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(dateFromField);

            // Date To
            JLabel dateToLabel = new JLabel("Date To:");
            dateToLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateToLabel.setForeground(new Color(30, 50, 85));
            dateToLabel.setBounds(280, 285, 100, 20);
            add(dateToLabel);

            JTextField dateToField = new JTextField();
            dateToField.setBounds(280, 310, 240, 35);
            dateToField.setBackground(new Color(218, 186, 121));
            dateToField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(dateToField);

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
                TransactionSearchCriteria.Builder builder = TransactionSearchCriteria.builder();

                String minAmt = minAmountField.getText().trim();
                if (!minAmt.isEmpty()) {
                    try {
                        builder.minAmount(Double.parseDouble(minAmt));
                    } catch (NumberFormatException ex) {}
                }

                String maxAmt = maxAmountField.getText().trim();
                if (!maxAmt.isEmpty()) {
                    try {
                        builder.maxAmount(Double.parseDouble(maxAmt));
                    } catch (NumberFormatException ex) {}
                }

                String type = (String) typeCombo.getSelectedItem();
                if (!"All".equals(type)) builder.type(type);

                String senderId = senderField.getText().trim();
                if (!senderId.isEmpty()) builder.senderId(senderId);

                String receiverId = receiverField.getText().trim();
                if (!receiverId.isEmpty()) builder.receiverId(receiverId);

                String dateFrom = dateFromField.getText().trim();
                if (!dateFrom.isEmpty()) builder.dateFrom(dateFrom);

                String dateTo = dateToField.getText().trim();
                if (!dateTo.isEmpty()) builder.dateTo(dateTo);

                TransactionSearchCriteria criteria = builder.build();
                List<Transaction> results = searchTransaction.filter(criteria);
                loadFilteredTransactions(results);
                dispose();
            });
            add(okBtn);
            
            setVisible(true);
        }
    }
    private void showReportDialog() {
        new AdminReportDialog(this);
    }
    public static void main(String[] args) {
        new HomePageAdminTran();
    }
}
