package ui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Factory for creating consistent UI components
 * Single Responsibility: Only creates styled UI components
 */
public final class ComponentFactory {
    
    private ComponentFactory() {
        // Prevent instantiation
    }
    
    /**
     * Creates the ABA logo panel
     */
    public static JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(UIColors.LOGO_BG);
        logoPanel.setPreferredSize(new Dimension(60, 45));
        logoPanel.setMaximumSize(new Dimension(60, 45));
        
        JLabel logoLabel = new JLabel("ABA");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(UIColors.WHITE);
        logoPanel.add(logoLabel);
        
        return logoPanel;
    }
    
    /**
     * Creates a styled button
     */
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(150, 35));
        button.setMaximumSize(new Dimension(150, 35));
        button.setBackground(UIColors.BUTTON_BG);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200, 200, 200));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIColors.BUTTON_BG);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a menu button for sidebars
     */
    public static JButton createMenuButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(130, 30));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(UIColors.TEXT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(textColor);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a tab button
     */
    public static JButton createTabButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        
        if (isActive) {
            button.setBackground(UIColors.TAB_ACTIVE);
            button.setForeground(UIColors.WHITE);
        } else {
            button.setBackground(UIColors.TAB_INACTIVE);
            button.setForeground(UIColors.BLACK);
        }
        
        return button;
    }
    
    /**
     * Creates a labeled text field panel
     */
    public static JPanel createLabeledTextField(String labelText, JTextField textField, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgColor);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(UIColors.TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textField.setPreferredSize(new Dimension(300, 35));
        textField.setMaximumSize(new Dimension(300, 35));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textField);
        
        return panel;
    }
    
    /**
     * Creates a scroll pane with common styling
     */
    public static JScrollPane createStyledScrollPane(JPanel content, Color bgColor) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBackground(bgColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
}
