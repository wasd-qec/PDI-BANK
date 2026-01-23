package GUI.Components;

import javax.swing.*;
import java.awt.*;

public class RoundedPasswordField extends JPasswordField {
    private int radius;
    private String placeholder;

    public RoundedPasswordField(int radius, String placeholder) {
        this.radius = radius;
        this.placeholder = placeholder;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);

        if (getPassword().length == 0 && placeholder != null) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(getFont());
            g2.drawString(placeholder, 12, getHeight() / 2 + 5);
        }
        g2.dispose();
    }
}
