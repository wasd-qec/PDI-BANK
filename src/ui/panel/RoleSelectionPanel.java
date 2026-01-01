package ui.panel;

import ui.common.ComponentFactory;
import ui.common.UIColors;

import javax.swing.*;
import java.awt.*;

/**
 * Role selection panel (first screen)
 * Single Responsibility: Only handles role selection UI
 */
public class RoleSelectionPanel extends JPanel {
    
    private final JButton customerBtn;
    private final JButton adminBtn;
    
    public RoleSelectionPanel() {
        setLayout(new BorderLayout());
        setBackground(UIColors.PRIMARY_BG);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIColors.PRIMARY_BG);
        
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(UIColors.PANEL_BG);
        loginBox.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        loginBox.setPreferredSize(new Dimension(300, 280));
        
        // Logo
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // "Login as" text
        JLabel loginAsLabel = new JLabel("Login as");
        loginAsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        loginAsLabel.setForeground(UIColors.TEXT_COLOR);
        loginAsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Customer button
        customerBtn = ComponentFactory.createStyledButton("Customer");
        customerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Admin button
        adminBtn = ComponentFactory.createStyledButton("Admin");
        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components
        loginBox.add(logoPanel);
        loginBox.add(Box.createVerticalStrut(20));
        loginBox.add(loginAsLabel);
        loginBox.add(Box.createVerticalStrut(30));
        loginBox.add(customerBtn);
        loginBox.add(Box.createVerticalStrut(15));
        loginBox.add(adminBtn);
        
        centerPanel.add(loginBox);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public void addCustomerListener(Runnable action) {
        customerBtn.addActionListener(e -> action.run());
    }
    
    public void addAdminListener(Runnable action) {
        adminBtn.addActionListener(e -> action.run());
    }
}
