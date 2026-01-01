package ui.dialog;

import model.Customer;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Factory for creating dialog boxes
 * Single Responsibility: Only creates and shows dialogs
 */
public final class DialogFactory {
    
    private DialogFactory() {
        // Prevent instantiation
    }
    
    /**
     * Shows account details dialog for customer
     */
    public static void showAccountDetail(Component parent, Customer customer) {
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
            customer.getAccNo(),
            customer.getName(),
            customer.getId(),
            customer.getBalance(),
            customer.getPhoneNumber(),
            customer.getAddress(),
            customer.getBirthDate(),
            customer.getCreateDate()
        );
        
        JOptionPane.showMessageDialog(parent, details, "Account Details", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Shows withdraw dialog and returns the amount entered
     * @return the amount to withdraw, or -1 if cancelled/invalid
     */
    public static double showWithdrawDialog(Component parent) {
        return showAmountDialog(parent, "Withdraw", "Enter amount to withdraw:");
    }
    
    /**
     * Shows deposit dialog and returns the amount entered
     * @return the amount to deposit, or -1 if cancelled/invalid
     */
    public static double showDepositDialog(Component parent) {
        return showAmountDialog(parent, "Deposit", "Enter amount to deposit:");
    }
    
    private static double showAmountDialog(Component parent, String title, String message) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel label = new JLabel(message);
        JTextField amountField = new JTextField();
        panel.add(label);
        panel.add(amountField);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, title, 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(parent, "Amount must be positive.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                return amount;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid amount.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return -1;
    }
    
    /**
     * Shows transfer dialog
     * @return String array with [recipientAccNo, amount] or null if cancelled
     */
    public static String[] showTransferDialog(Component parent) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        JLabel recipientLabel = new JLabel("Recipient Account Number:");
        JTextField recipientField = new JTextField();
        JLabel amountLabel = new JLabel("Amount to Transfer:");
        JTextField amountField = new JTextField();
        
        panel.add(recipientLabel);
        panel.add(recipientField);
        panel.add(amountLabel);
        panel.add(amountField);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, "Transfer", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String recipientAccNo = recipientField.getText().trim();
            String amountStr = amountField.getText().trim();
            
            if (recipientAccNo.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please enter recipient account.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(parent, "Amount must be positive.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                return new String[] { recipientAccNo, amountStr };
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid amount.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
    
    /**
     * Shows customer report dialog
     */
    public static void showCustomerReport(Component parent, Customer customer, List<Transaction> transactions) {
        double totalIn = 0;
        double totalOut = 0;
        
        for (Transaction t : transactions) {
            if (t.getType().equals("DEPOSIT") || 
                (t.getType().equals("TRANSFER") && t.getReceiverId().equals(customer.getId()))) {
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
        report.append("<p><b>Account:</b> ").append(customer.getAccNo()).append("</p>");
        report.append("<p><b>Name:</b> ").append(customer.getName()).append("</p>");
        report.append("<p><b>Current Balance:</b> $").append(String.format("%,.2f", customer.getBalance())).append("</p>");
        report.append("<hr>");
        report.append("<h3>Transaction Summary</h3>");
        report.append("<p><b>Total Transactions:</b> ").append(transactions.size()).append("</p>");
        report.append("<p style='color: green; font-size: 14px;'><b>Total In:</b> $").append(String.format("%,.2f", totalIn)).append("</p>");
        report.append("<p style='color: red; font-size: 14px;'><b>Total Out:</b> $").append(String.format("%,.2f", totalOut)).append("</p>");
        report.append("</body></html>");
        
        JOptionPane.showMessageDialog(parent, report.toString(), "Account Report", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Shows admin transaction report dialog
     */
    public static void showTransactionReport(Component parent, List<Transaction> transactions) {
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
        
        JOptionPane.showMessageDialog(parent, report.toString(), "Transaction Report", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Shows search dialog
     * @return the search term, or null if cancelled
     */
    public static String showSearchDialog(Component parent, String title, String message) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel label = new JLabel(message);
        JTextField searchField = new JTextField();
        panel.add(label);
        panel.add(searchField);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, title, 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String searchTerm = searchField.getText().trim();
            return searchTerm.isEmpty() ? null : searchTerm;
        }
        return null;
    }
    
    /**
     * Shows confirmation dialog
     * @return true if user confirmed
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Shows error message
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows success message
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows warning message
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
