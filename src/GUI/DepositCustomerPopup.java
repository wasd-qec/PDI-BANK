import javax.swing.*;
import java.awt.*;

public class DepositCustomerPopup extends JDialog {

    public DepositCustomerPopup(JFrame parent) {
        super(parent, true);

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 450, 300, 35, 35));

        JPanel panel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 238, 228));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        add(panel);

        JLabel title = new JLabel("Deposit");
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setBounds(30, 20, 200, 30);
        panel.add(title);

        JPanel line = new JPanel();
        line.setBackground(new Color(150, 150, 150));
        line.setBounds(30, 60, 390, 1);
        panel.add(line);

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountLabel.setBounds(30, 80, 200, 25);
        panel.add(amountLabel);

        RoundedTextField amountField = new RoundedTextField(20, "Enter amount");
        amountField.setBackground(new Color(218, 186, 121));
        amountField.setBounds(30, 110, 390, 45);
        panel.add(amountField);

        RoundedButton cancelBtn = new RoundedButton("Cancel");
        cancelBtn.setBackground(new Color(108, 130, 173));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBounds(170, 200, 100, 40);
        panel.add(cancelBtn);

        RoundedButton depositBtn = new RoundedButton("Deposit");
        depositBtn.setBackground(new Color(8, 25, 64));
        depositBtn.setForeground(Color.WHITE);
        depositBtn.setBounds(285, 200, 120, 40);
        panel.add(depositBtn);

        // Cancel closes popup only
        cancelBtn.addActionListener(e -> dispose());

        // Deposit closes popup & shows success popup
        depositBtn.addActionListener(e -> {
            // (Optional future): handle deposit logic here
            // e.g. updateBalance(amountField.getText());

            dispose(); // close deposit popup

            // Show success popup
            new SuccesPopupCustomerDP(parent);
        });

        setVisible(true);
    }

    // Simple RoundedTextField for popup
    class RoundedTextField extends JTextField {
        private String placeholder;
        private int radius;

        RoundedTextField(int r, String placeholder) {
            this.radius = r;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
            if (getText().isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.drawString(placeholder, 12, getHeight() / 2 + 5);
            }
            g2.dispose();
        }
    }

    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}
