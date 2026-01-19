import javax.swing.*;
import java.awt.*;

public class DeactivateAccCustomer extends JDialog {

    public DeactivateAccCustomer(JFrame parent) {
        super(parent, true);

        setSize(500, 240);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 500, 240, 30, 30));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setBounds(0, 0, 500, 240);
        add(panel);

        JLabel title = new JLabel("Deactivate Account");
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(35, 25, 400, 30);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(35, 65, 430, 1);
        panel.add(line);

        JLabel msg = new JLabel(
                "<html>Are you sure you want to deactivate your account?<br>This action cannot be undone.</html>");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msg.setForeground(new Color(10, 32, 68));
        msg.setBounds(35, 85, 440, 50);
        panel.add(msg);

        // Cancel Button
        JButton cancelBtn = new JButton("Cancel") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(120, 130, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setBounds(150, 160, 90, 35);
        cancelBtn.addActionListener(e -> dispose());
        panel.add(cancelBtn);

        // Confirm Button
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
        confirmBtn.setBounds(260, 160, 90, 35);

        // Action on confirm
        confirmBtn.addActionListener(e -> {
            dispose();
            // TODO: Perform deactivation logic here
            new DeactivateCustomPopup(parent); // optional success popup, or redirect to logout
        });
        panel.add(confirmBtn);

        setVisible(true);
    }
}
