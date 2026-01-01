package ui.panel;

import ui.common.ComponentFactory;
import ui.common.UIColors;

import javax.swing.*;
import java.awt.*;

/**
 * Login panel component
 * Single Responsibility: Only handles login UI
 */
public class LoginPanel extends JPanel {
    
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginBtn;
    private final JButton backBtn;
    
    public LoginPanel(String title, String usernameLabel, boolean showBackButton) {
        setLayout(new BorderLayout());
        setBackground(UIColors.PRIMARY_BG);
        
        // Back button at top-left
        if (showBackButton) {
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.setBackground(UIColors.PRIMARY_BG);
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            
            backBtn = new JButton("Back");
            backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            backBtn.setPreferredSize(new Dimension(70, 28));
            topPanel.add(backBtn);
            
            add(topPanel, BorderLayout.NORTH);
        } else {
            backBtn = null;
        }
        
        // Center login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIColors.PRIMARY_BG);
        
        JPanel loginForm = new JPanel();
        loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.Y_AXIS));
        loginForm.setBackground(UIColors.PRIMARY_BG);
        loginForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Logo
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UIColors.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        usernameField = new JTextField();
        JPanel usernamePanel = ComponentFactory.createLabeledTextField(usernameLabel, usernameField, UIColors.PRIMARY_BG);
        
        // Password field
        passwordField = new JPasswordField();
        JPanel passwordPanel = ComponentFactory.createLabeledTextField("Password", passwordField, UIColors.PRIMARY_BG);
        
        // Login button
        loginBtn = ComponentFactory.createStyledButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add Enter key listener
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
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
    
    public void addLoginListener(Runnable action) {
        loginBtn.addActionListener(e -> action.run());
    }
    
    public void addBackListener(Runnable action) {
        if (backBtn != null) {
            backBtn.addActionListener(e -> action.run());
        }
    }
}
