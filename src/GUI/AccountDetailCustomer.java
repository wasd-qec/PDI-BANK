import javax.swing.*;
import java.awt.*;

public class AccountDetailCustomer extends JDialog {

    public AccountDetailCustomer(JFrame parent) {
        super(parent, true);

        setSize(600, 450);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 600, 450, 35, 35));

        // Main rounded panel
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228)); // beige
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setOpaque(false);
        panel.setBounds(0, 0, 600, 450);
        add(panel);

        JLabel title = new JLabel("Account Detail", SwingConstants.LEFT);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(new Color(10, 32, 68));
        title.setBounds(40, 30, 400, 40);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(140, 140, 140));
        line.setBounds(40, 75, 520, 1);
        panel.add(line);

        // Left field labels
        String[] labels = {
                "Account number", "Customer ID", "Name", "Phone Number", "Address",
                "Birth Date", "Account Created", "Balance", "Status"
        };

        // Dummy values for example — replace later with DB values
        String[] values = {
                "ABC001", "ABC001", "Heng", "5551234567", "100 Bank Street, New York",
                "1985-06-20", "2025-01-01", "$33,501", "Active"
        };

        int y = 100;
        for (int i = 0; i < labels.length; i++) {
            JLabel left = new JLabel(labels[i]);
            left.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            left.setForeground(new Color(10, 32, 68));
            left.setBounds(60, y, 200, 25);
            panel.add(left);

            JLabel right = new JLabel(values[i]);
            right.setFont(new Font("Segoe UI", Font.BOLD, 16));
            right.setForeground(new Color(206, 140, 0));
            right.setBounds(260, y, 300, 25);
            panel.add(right);

            y += 32;
        }

        // Close Button
        JButton closeBtn = new JButton("Close") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(120, 130, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBounds(250, 400, 100, 35);
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn);

        setVisible(true);
    }
}
