import javax.swing.*;
import java.awt.*;

public class TransactionSuccessPopup extends JDialog {

    public TransactionSuccessPopup(JFrame parent) {
        super(parent, true);
        setSize(400, 100);
        setUndecorated(true);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 450, 120, 35, 35));

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panel.setBounds(0, 0, 450, 170);
        add(panel);

        JLabel msg = new JLabel("Transaction created!");
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setVerticalAlignment(SwingConstants.CENTER);

        msg.setFont(new Font("Serif", Font.BOLD, 22));
        msg.setForeground(new Color(10, 32, 68));
        msg.setBounds(0, 50, 450, 40);
        panel.add(msg);

        setVisible(true);

        // auto close after 1.8 seconds
        Timer timer = new Timer(1400, e -> dispose());
        timer.setRepeats(false);
        timer.start();

        setVisible(true);
    }
}
