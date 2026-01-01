package ui.common;

import model.Transaction;
import model.Customer;

import javax.swing.*;
import java.awt.*;

/**
 * Factory for creating card components (account cards, transaction cards)
 * Single Responsibility: Only creates card UI components
 */
public final class CardFactory {
    
    private CardFactory() {
        // Prevent instantiation
    }
    
    /**
     * Creates a transaction card for customer view
     */
    public static JPanel createCustomerTransactionCard(Transaction t, String currentCustomerId) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIColors.CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(0, 90));
        
        // Left side - Transaction details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(UIColors.CARD_BG);
        
        JLabel idLabel = new JLabel(t.getTransactionId() + " - " + t.getType());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(UIColors.WHITE);
        
        JLabel senderLabel = new JLabel("From: " + t.getSenderId());
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        senderLabel.setForeground(new Color(180, 180, 180));
        
        JLabel receiverLabel = new JLabel("To: " + t.getReceiverId());
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        receiverLabel.setForeground(new Color(180, 180, 180));
        
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
            (t.getType().equals("TRANSFER") && t.getReceiverId().equals(currentCustomerId))) {
            amountText = String.format("+$%.2f", t.getAmount());
            amountColor = new Color(100, 200, 100);
        } else {
            amountText = String.format("-$%.2f", t.getAmount());
            amountColor = new Color(200, 100, 100);
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
     * Creates a transaction card for admin view
     */
    public static JPanel createAdminTransactionCard(Transaction t) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(217, 217, 217));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, UIColors.CARD_BORDER),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(0, 90));
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(217, 217, 217));
        
        JLabel idLabel = new JLabel(t.getTransactionId() + " - " + t.getType());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(UIColors.BLACK);
        
        JLabel senderLabel = new JLabel("From: " + t.getSenderId());
        senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        senderLabel.setForeground(new Color(80, 80, 80));
        
        JLabel receiverLabel = new JLabel("To: " + t.getReceiverId());
        receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        receiverLabel.setForeground(new Color(80, 80, 80));
        
        JLabel timestampLabel = new JLabel(t.getTimestamp());
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timestampLabel.setForeground(new Color(120, 120, 120));
        
        detailsPanel.add(idLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(senderLabel);
        detailsPanel.add(receiverLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(timestampLabel);
        
        String amountText;
        Color amountColor;
        
        if (t.getType().equals("DEPOSIT")) {
            amountText = String.format("+$%.2f", t.getAmount());
            amountColor = UIColors.GREEN;
        } else if (t.getType().equals("WITHDRAWAL")) {
            amountText = String.format("-$%.2f", t.getAmount());
            amountColor = UIColors.RED;
        } else {
            amountText = String.format("$%.2f", t.getAmount());
            amountColor = new Color(0, 100, 200);
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
     * Creates an account card for admin view
     */
    public static JPanel createAccountCard(Customer account, Runnable onViewClick) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(217, 217, 217));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, UIColors.CARD_BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setPreferredSize(new Dimension(0, 60));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        infoPanel.setBackground(new Color(217, 217, 217));
        
        JLabel nameLabel = new JLabel(account.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(UIColors.BLACK);
        
        JLabel accNoLabel = new JLabel(" - " + account.getAccNo());
        accNoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accNoLabel.setForeground(new Color(100, 100, 100));
        
        infoPanel.add(nameLabel);
        infoPanel.add(accNoLabel);
        
        JLabel balanceLabel = new JLabel(String.format("$%,.2f", account.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balanceLabel.setForeground(UIColors.GREEN);
        
        JButton viewBtn = new JButton("View");
        viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        viewBtn.setPreferredSize(new Dimension(60, 25));
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> onViewClick.run());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(217, 217, 217));
        rightPanel.add(balanceLabel);
        rightPanel.add(viewBtn);
        
        card.add(infoPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
}
