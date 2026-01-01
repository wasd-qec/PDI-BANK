package ui.common;

import java.awt.*;

/**
 * UI Color scheme constants
 * Single Responsibility: Only holds color configuration
 */
public final class UIColors {
    public static final Color PRIMARY_BG = new Color(102, 128, 144);      // Steel blue background
    public static final Color PANEL_BG = new Color(61, 82, 96);           // Darker panel background
    public static final Color SIDEBAR_BG = new Color(61, 82, 96);         // Sidebar background
    public static final Color TEXT_COLOR = new Color(79, 166, 183);       // Teal text color
    public static final Color WHITE = Color.WHITE;
    public static final Color BLACK = Color.BLACK;
    public static final Color BUTTON_BG = new Color(230, 230, 230);       // Light gray button
    public static final Color CARD_BG = new Color(75, 95, 110);           // Card background
    public static final Color HEADER_BG = new Color(192, 192, 192);       // Header background
    public static final Color TAB_ACTIVE = new Color(61, 82, 96);         // Active tab color
    public static final Color TAB_INACTIVE = WHITE;                        // Inactive tab color
    public static final Color CARD_BORDER = new Color(138, 43, 226);      // Purple border
    public static final Color GREEN = new Color(34, 139, 34);
    public static final Color RED = new Color(178, 34, 34);
    public static final Color LOGO_BG = new Color(37, 64, 97);
    
    private UIColors() {
        // Prevent instantiation
    }
}
