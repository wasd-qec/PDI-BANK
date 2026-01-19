import javax.swing.*;
import java.awt.*;

public class DeactivateCustomPopup extends JDialog {

    public DeactivateCustomPopup(JFrame parent) {
        super(parent, true);

        setSize(500, 220);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 500, 220, 30, 30));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        add(panel);

        JLabel title = new JLabel("Information");
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(35, 25, 300, 30);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(35, 65, 430, 1);
        panel.add(line);

        JLabel msg = new JLabel("You have successfully deactivated your account.");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msg.setForeground(new Color(10, 32, 68));
        msg.setBounds(35, 90, 430, 30);
        panel.add(msg);

        JButton confirmBtn = new JButton("Confirm") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(8, 25, 64));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setContentAreaFilled(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setBounds(200, 140, 90, 35);
        confirmBtn.addActionListener(e -> dispose());
        panel.add(confirmBtn);

        setVisible(true);
    }
}
